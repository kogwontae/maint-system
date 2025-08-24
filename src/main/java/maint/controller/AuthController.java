package maint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.util.UriComponentsBuilder;
import maint.controller.ErrorController.StatusCode;
import maint.form.LoginForm;
import maint.utils.ExObjectUtils;

/**
 * ログイン画面コントローラ
 *
 * @author administrator
 */
@Controller
@RequestMapping(value = "/auth")
@SessionAttributes(names = "loginForm")
public class AuthController extends BaseController {

  /** ログイン画面PATH */
  private static final String LOGIN_PAGE_PATH = "auth/index";

  /**
   * ログインフォーム作成
   *
   * @param loginForm ログインフォーム
   * @return ログインフォーム
   */
  @ModelAttribute("loginForm")
  public LoginForm setRequestForm(LoginForm loginForm) {
    return loginForm;
  }

  /**
   * ログイン画面初期表示
   *
   * @param model Model
   * @return ログイン画面
   */
  @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
  public String index(Model model) {
    model.addAttribute("loginForm", new LoginForm());
    // ログイン画面を表示する
    return LOGIN_PAGE_PATH;
  }

  /**
   * ログイン失敗
   *
   * @param model Model
   * @param form  ログインフォーム
   * @return ログイン画面
   */
  @RequestMapping(value = "/login/error", method = {RequestMethod.GET, RequestMethod.POST})
  public String loginError(Model model, @ModelAttribute("loginForm") LoginForm form) {
    execCommonProc(model, true);
    if (ExObjectUtils.isNotBlank(form.getErrorMessageKey())) {
      this.setErrorMsg(form.getErrorMessageKey());
      form.setErrorMessageKey(null);
    }
    model.addAttribute("loginForm", form);
    // ログイン画面を表示する
    return LOGIN_PAGE_PATH;
  }

  /**
   * ログイン成功時の遷移
   *
   * @param model   Model
   * @param status  SessionStatus
   * @param builder UriComponentsBuilder
   * @return トップ画面
   */
  @RequestMapping(value = "/login/success", method = {RequestMethod.GET, RequestMethod.POST})
  public String login(Model model, SessionStatus status, UriComponentsBuilder builder) {
    // セッションのログインフォームを破棄
    status.setComplete();
    return "redirect:" + builder.path("/").build().toUri().toString();
  }

  /**
   * セッション切れ
   *
   * @param model   Model
   * @param status  SessionStatus
   * @param builder UriComponentsBuilder
   * @return セッション切れ画面
   */
  @GetMapping("/session-timeout")
  public String sessionTimeout(Model model, SessionStatus status, UriComponentsBuilder builder) {
    return "redirect:" + UriComponentsBuilder.fromUriString(StatusCode.LOGIN_TIME_OUT.getPath())
        .build().toUri().toString();
  }

}
