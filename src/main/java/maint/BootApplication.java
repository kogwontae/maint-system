package maint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Boot Application class.
 *
 * @author masaya.tanaka
 */
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 10800)
public class BootApplication implements WebMvcConfigurer {

  @Autowired
  private MessageSource messageSource;

  public static void main(String[] args) {
    SpringApplication.run(BootApplication.class, args);
  }

  /**
   * LocalValidatorFactoryBeanのsetValidationMessageSourceで
   * バリデーションメッセージをValidationMessages.propertiesからSpringの MessageSource(messages.properties)に上書きする
   *
   * @return localValidatorFactoryBean
   */
  @Bean
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    localValidatorFactoryBean.setValidationMessageSource(messageSource);
    return localValidatorFactoryBean;
  }

  @Override
  public org.springframework.validation.Validator getValidator() {
    return validator();
  }

  /**
   * Spring SessionがAWS上のRedisのCONFIGを実行しないようにする
   *
   * @return Spring SessionがAWS上のRedisのCONFIGを実行しない設定
   */
  @Bean
  public static ConfigureRedisAction configureRedisAction() {
    return ConfigureRedisAction.NO_OP;
  }

}
