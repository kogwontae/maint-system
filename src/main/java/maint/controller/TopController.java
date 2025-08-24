package maint.controller;

import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import maint.session.UserSession;

/**
 * トップページ用コントローラ
 *
 * @author administrator
 */
@Controller
@RequestMapping(value = "/")
public class TopController extends BaseController {

  private static final String INDEX_PAGE = "index";

  /**
   * トップページ
   *
   * @param model MODEL
   * @return トップページ
   */
  @RequestMapping(value = "", method = RequestMethod.GET)
  public String index(Model model) {

    model.addAttribute("errorMsgList", new ArrayList<>());
    model.addAttribute("successMsgList", new ArrayList<>());

    // パスワード変更フラグ取得
    UserSession userSession =
        (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    model.addAttribute("passChangeFlag", userSession.getPasswordChangedFlag());

    return INDEX_PAGE;
  }

}
