package maint.service;

import java.lang.invoke.MethodHandles;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import maint.session.UserSession;

/**
 * Base service class.
 *
 * @author administrator
 */
@Service
public class BaseService {

  /** ロガー */
  protected static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** メッセージ */
  @Autowired
  protected MessageSource messageSource;

  /** メッセージResource */
  protected static ResourceBundle resourceBundle =
      ResourceBundle.getBundle("message", Locale.ENGLISH);

  /**
   * get a message by property name.
   *
   * @param name
   * @return
   */
  public String getMessageByPropertyName(String name) {
    return resourceBundle.getString(name);
  }

  /**
   * メッセージ取得
   *
   * @param key メッセージKEY
   * @return メッセージ
   */
  protected String getMessage(String key) {
    return messageSource.getMessage(key, null, Locale.JAPAN);
  }

  /**
   * UserSession情報の取得
   *
   * @return UserSession
   */
  public UserSession getUserSession() {
    return (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
