package maint.controller;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import maint.message.CommonMessage;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import maint.dto.ApiResultDto;
import maint.exception.CommonDbException;
import maint.message.UsersMessage;
import maint.service.system.UsersService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * API共通 コントローラ
 *
 * @author cho hyeonbi
 */
@RestController
@Validated
@RequestMapping(value = "/api")
public class CommonController extends BaseController {

  /** ロガー */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** ユーザSERVICE */
  @Autowired
  UsersService usersService;

  /**
   * パスワード変更
   *
   * @param currentPass 現在のパスワード
   * @param newPass     新パスワード
   * @param request     HttpServletRequest
   * @param response    HttpServletResponse
   * @return ResponseEntity
   */
  @PostMapping(value = "/password-change/")
  public ResponseEntity<String> changePass(@RequestParam("currentPass") String currentPass,
      @RequestParam("newPass") String newPass, HttpServletRequest request,
      HttpServletResponse response) {

    final HttpHeaders httpHeaders = new HttpHeaders();

    // 現在のユーザーセッションからログインIDを取得
    String email = usersService.getUserSession().getEmail();

    // 現パスワード入力値チェック
    if (!usersService.isValidPasswordForm(currentPass, email)) {
      return new ResponseEntity<String>(new Gson().toJson(new ApiResultDto(false,
          getMessage(UsersMessage.FAIL_INPUT_CURRENT_PASSWORD.getMessage()))), httpHeaders,
          HttpStatus.OK);
    }

    // 新パスワード入力値チェック
    if (!usersService.isValidPasswordForm(newPass, email)) {
      return new ResponseEntity<String>(new Gson().toJson(
          new ApiResultDto(false, getMessage(UsersMessage.FAIL_INPUT_NEW_PASSWORD.getMessage()))),
          httpHeaders, HttpStatus.OK);
    }

    try {
      // 現在のパスワード一致チェック
      if (!usersService.validationCurrentPassword(currentPass)) {
        return new ResponseEntity<String>(new Gson().toJson(
            new ApiResultDto(false, getMessage(UsersMessage.FAIL_INVALID_PASSWORD.getMessage()))),
            httpHeaders, HttpStatus.OK);
      }
      // パスワード変更処理
      usersService.changePassword(newPass, request, response);
      return new ResponseEntity<String>(new Gson().toJson(new ApiResultDto(true,
          getMessage(UsersMessage.SUCCESS_CHANGE_NEW_PASSWORD.getMessage()))), httpHeaders,
          HttpStatus.OK);
    } catch (Throwable e) {
      LOGGER.error("【パスワード変更API】エラー　currentPass:" + currentPass + "newPass:" + newPass,
          e.getMessage());
      throw new CommonDbException("【ユーザ管理】変更エラー");
    }
  }

  /**
   * 事業者変更
   *
   * @param businessCorpId 事業者ID
   * @return ResponseEntity
   */
  @PostMapping(value = "/agency-change/")
  public ResponseEntity<?> changeAgency(@RequestParam("businessCorpId") Long businessCorpId) {
    Map<String, String> response = new HashMap<>();
    try {
      // 現在の認証情報を取得
      Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
      UserSession userSession = (UserSession) currentAuth.getPrincipal();

      // ユーザーセッション内の事業者IDを更新
      userSession.setBusinessCorpId(businessCorpId);

      // 更新後のユーザー情報で認証情報を再生成
      Authentication newAuth = new UsernamePasswordAuthenticationToken(
          userSession,
          currentAuth.getCredentials(),
          currentAuth.getAuthorities()
      );

      // 認証情報をセキュリティコンテキストに設定
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      // セッションにセキュリティコンテキストを再設定（セッションが存在する場合）
      HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
          .getRequest().getSession(false);
      if (ExObjectUtils.isNotBlank(session)) {
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
      }

      // 正常終了メッセージを返却
      response.put("textStatus", "success");
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      // エラー発生時の処理
      response.put("textStatus", "failure");
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
