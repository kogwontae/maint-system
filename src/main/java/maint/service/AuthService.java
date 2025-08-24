package maint.service;

import maint.dto.baseInfo.agencies.AgencySelectDto;
import maint.mapper.AgenciesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import maint.dto.UserPermissionDto;
import maint.entity.UsersEntity;
import maint.exception.AdminAuthException;
import maint.mapper.PermissionRoleDetailsMapper;
import maint.mapper.UsersMapper;
import maint.message.AdminAuthErrorMessage;
import maint.utils.ExObjectUtils;
import maint.utils.PasswordUtils;

import java.util.List;

/**
 * ログインSERVICE
 *
 * @author administrator
 */
@Service
public class AuthService extends BaseService {

  /** 管理者MAPPER */
  @Autowired
  private UsersMapper usersMapper;

  /** 事業者MAPPER */
  @Autowired
  private AgenciesMapper agenciesMapper;

  /** パスワードUTILS */
  @Autowired
  private PasswordUtils passwordUtils;

  /** 権限詳細MAPPER */
  @Autowired
  private PermissionRoleDetailsMapper permissionRoleDetailsMapper;

  /**
   * 管理者情報取得
   *
   * @param email メール
   * @return 管理者情報
   */
  public UsersEntity getUserInfo(String email, String loginPass) {
    // 管理者Entitiy取得
    UsersEntity usersEntity = usersMapper.getUserByLoginInfo(email);

    // 該当アカウントがない場合
    if (ExObjectUtils.isBlank(usersEntity)) {
      throw new AdminAuthException("Not Exist Account", AdminAuthErrorMessage.NOT_EXIST);
    }

    // パスワードチェック
    if (!passwordUtils.isMatchPassword(loginPass, usersEntity.getPassword())) {
      throw new AdminAuthException("Login Error", AdminAuthErrorMessage.LOGIN_FAIL);
    }

    return usersEntity;
  }

  /**
   * ユーザ権限リスト取得
   *
   * @param permRoleId 権限ロールID
   * @return ユーザ権限リスト
   */
  public List<UserPermissionDto> getPermissionList(Long permRoleId) {
    return permissionRoleDetailsMapper.getPermissionListByPk(permRoleId);
  }

  /**
   * 全事業者取得
   *
   * @return 事業者リスト
   */
  public List<AgencySelectDto> getAllAgency() {
    return agenciesMapper.getAllAgenciesForSelectOption();
  }

  /**
   * 事業者名取得
   *
   * @param businessCorpId 事業者ID
   * @return 事業者名
   */
  public String getBusinessCorpNameByBusinessCorpId(Long businessCorpId) {
    return agenciesMapper.getBusinessCorpNameByBusinessCorpId(businessCorpId);
  }
}
