package maint.service.system;

import maint.enums.PwdChangedFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import maint.dto.ListPager;
import maint.dto.system.users.SearchUsersDto;
import maint.dto.system.users.UserMailContentsDto;
import maint.dto.system.users.UsersDetailDto;
import maint.dto.system.users.UsersDto;
import maint.entity.UsersEntity;
import maint.enums.PwdSendMail;
import maint.enums.UserMailDivisionEnums;
import maint.exception.CommonDbException;
import maint.form.users.UsersDetailForm;
import maint.form.users.UsersSearchForm;
import maint.mapper.UsersMapper;
import maint.properties.AwsProperties;
import maint.service.BaseService;
import maint.session.UserSession;
import maint.utils.AwsUtils;
import maint.utils.BaseMailUtils;
import maint.utils.ExObjectUtils;
import maint.utils.PasswordUtils;

/**
 * ユーザSERVICE
 *
 * @author ryuichi.kashiwai
 */
@Service
public class UsersService extends BaseService {

  /** パスワードUTIL */
  @Autowired
  PasswordUtils passwordUtil;

  /** ユーザ MAPPER */
  @Autowired
  UsersMapper usersMapper;

  /** Base Mail Util */
  @Autowired
  private BaseMailUtils baseMailUtils;

  /** AWS Properties */
  @Autowired
  private AwsProperties awsProperties;

  /**
   * ユーザ一覧取得
   *
   * @param form  検索FORM
   * @param pager ListPager
   */
  public void getUsers(UsersSearchForm form, ListPager pager) {
    SearchUsersDto dto = new SearchUsersDto(form, pager.getStartIndex(),getUserSession());
    pager.setTotalListSize(
        usersMapper.countUsersBySearch(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(usersMapper.getUsersBySearch(dto));
    }
  }

  /**
   * ユーザ情報取得
   *
   * @param id PK
   * @return ユーザ情報
   */
  public UsersDetailDto getUserByPk(Long id) {
    return usersMapper.getUserByPk(id);
  }

  /**
   * ユーザ件数取得
   *
   * @param pk      PK
   * @param email ログインID
   * @return ユーザ件数
   */
  public boolean countUsers(Long pk, String email) {
    return usersMapper.countUsersByEmail(pk, email) > 0;
  }

  /**
   * ユーザ登録
   *
   * @param form 詳細FORM
   * @return ユーザID
   */
  @Transactional
  public Long insert(UsersDetailForm form) {
    // ユーザ登録
    UsersEntity usersEntity = new UsersEntity(form);

    // 登録失敗時、エラーとする
    if (usersMapper.registerUsers(usersEntity) != 1) {
      throw new CommonDbException("【アカウント管理】登録エラー");
    }

    return usersEntity.getUserId();
  }

  /**
   * ユーザ変更
   *
   * @param form 詳細FORM
   */
  @Transactional
  public void update(UsersDetailForm form) {
    // ユーザー変更
    UsersEntity usersEntity = new UsersEntity(form);

    if (usersMapper.updateUsers(usersEntity) != 1) {
      throw new CommonDbException("【アカウント管理】変更エラー");
    }
  }

  /**
   * パスワード取得
   *
   * @param form 詳細FORM
   * @return パスワード
   */
  private String getPassword(UsersDetailForm form) {
    String password = form.getPassword();

    if (PwdSendMail.SEND_MAIL.getValue().equals(form.getPwdSendMailFlag())) {
      password = PasswordUtils.getRandomPassword();
    }
    return password;
  }

  /**
   * パスワード形式チェック
   *
   * @param pass    パスワード文字列
   * @param email ログイン
   * @return true:正常、false:エラー
   */
  public boolean isValidPasswordForm(String pass, String email) {
    return PasswordUtils.isStrongPassword(pass) && !PasswordUtils.equalsToEmail(pass, email);
  }

  /**
   * 現在のパスワードがDBと一致するかチェック
   *
   * @param currentPass 現在のパスワード
   * @return True：一致、False：不一致
   */
  public boolean validationCurrentPassword(String currentPass) {
    return passwordUtil.isMatchPassword(currentPass,
        usersMapper.getUserPasswordByPK(getUserSession().getUserId()));
  }

  /**
   * ユーザーパスワード変更処理
   *
   * @param newPass  新パスワード
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   */
  @Transactional
  public void changePassword(String newPass, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    // ユーザーの変更
    UserSession userSession = getUserSession();
    UsersEntity usersEntity = new UsersEntity();
    usersEntity.setSession(userSession);
    usersEntity.setUserId(userSession.getUserId());
    usersEntity.setPassword(PasswordUtils.getBCryptStr(newPass));

    if (usersMapper.updateUserPassword(usersEntity) != 1) {
      throw new CommonDbException("【ユーザ管理】パスワード変更エラー");
    }
    // セッション情報の変更
    userSession.setPasswordChangedFlag(PwdChangedFlag.CHANGED.getValue());
    // セッション情報の変更
    writeAutheticationUserSession(userSession, request, response);
  }

  /**
   * セッション情報の上書き
   *
   * @param newUserSession 新セッション情報
   * @param request        HttpServletRequest
   * @param response       HttpServletResponse
   */
  private void writeAutheticationUserSession(UserSession newUserSession, HttpServletRequest request,
      HttpServletResponse response) {
    // 現在のセッション情報の取得
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

    // 新しい認証情報を作成
    Authentication newAuth =
        new UsernamePasswordAuthenticationToken(newUserSession, currentAuth.getCredentials(),
            currentAuth.getAuthorities());

    // SecurityContextを更新
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(newAuth);

    // セッションに明示的に保存（Spring Security 6.xでは必要）
    SecurityContextRepository repo = new HttpSessionSecurityContextRepository();
    repo.saveContext(context, request, response);
  }

  /**
   * ユーザへメール送信
   *
   * @param userId       ユーザID
   * @param enums        メール区分
   * @throws Exception
   */
  @Transactional
  public void passwordReissue(Long userId, UserMailDivisionEnums enums) throws Exception {
    try {
      // ユーザ情報取得
      UsersDto usersDto = usersMapper.getUserByEmail(userId);
      if (ExObjectUtils.isBlank(usersDto)) {
        throw new CommonDbException("【ユーザ管理】ユーザ情報取得エラー");
      }

      // パスワード生成
      String password = PasswordUtils.getRandomPassword();
      updateUserPassword(userId, enums.getValue(), password);

      UserMailContentsDto mailContentDto =
          new UserMailContentsDto(usersDto, awsProperties, password);
      // コンテンツ作成
      String contents = baseMailUtils.createContents(enums.getFtlFileName(), mailContentDto);
      // メール送信
      AwsUtils.sendMailTextType(mailContentDto.getEmail(), enums.getSubject(), contents);

    } catch (Exception e) {
      LOGGER.error("【業務WEBユーザ向けメール送信】システムエラー", e);
      throw e;
    }
  }

  /**
   * ユーザパスワード更新
   *
   * @param userId       ユーザID
   * @param mailDivision メール区分
   * @param password     パスワード
   */
  private void updateUserPassword(Long userId, String mailDivision, String password) {
    if (usersMapper.updateUserPasswordReissue(
        new UsersEntity(userId, PasswordUtils.getBCryptStr(password))) != 1) {
      throw new CommonDbException(
          "【ユーザ管理】パスワード変更エラーuserId = " + userId + " mailDivision = " + mailDivision);
    }
  }
}
