package maint.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import jakarta.servlet.DispatcherType;
import maint.handler.CustomAuthenticationFailureHandler;
import maint.provider.CustomAuthenticationProvider;

/**
 * WebSecurityMultiConfig
 *
 * @author administrator
 */
@Configuration
@EnableWebSecurity
public class WebSecurityMultiConfig {

  /** 独自認証 */
  @Autowired
  CustomAuthenticationProvider authenticationProvider;

  /**
   * security設定
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // コントローラで画面ファイル名をリターンし、ViewResolverが動作してページを遷移する場合、以下の設定必要
    http.authorizeHttpRequests(
        (authorizeHttpRequests) -> authorizeHttpRequests.dispatcherTypeMatchers(
            DispatcherType.FORWARD).permitAll());

    // 認証なしでのアクセス（CSRF有効)
    http.authorizeHttpRequests(
        (authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers("/css/**", "/fonts/**",
            "/img/**", "/js/**", "/page/**", "/auth/login", "/auth/login/error",
            "/auth/session-timeout", "/error/*").permitAll());

    // 認証なしでのアクセス以外は、認証後でないとアクセス出来ない設定
    http.authorizeHttpRequests(
        (authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().authenticated());

    // ログイン処理
    http.formLogin((formLogin) -> formLogin.loginPage("/auth/login")
        .defaultSuccessUrl("/auth/login/success", true)// ログイン成功
        .failureHandler(new CustomAuthenticationFailureHandler()).permitAll() // ログイン失敗
        .usernameParameter("email").passwordParameter("loginPass"));

    // ログアウト処理
    http.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
        .permitAll().logoutSuccessUrl("/auth/login") // ログアウト成功
        .invalidateHttpSession(true)); // セッションを無効にする

    // セッション切れの遷移先設定
    http.sessionManagement(
        (sessionManagement) -> sessionManagement.invalidSessionUrl("/auth/session-timeout"));

    return http.build();
  }

  /**
   * authenticationManager生成<br> Springの内部動作により、上記で作成したCustomAuthenticationProviderが自動的に設定される
   *
   * @param authenticationConfiguration AuthenticationConfiguration
   * @return AuthenticationManager
   * @throws Exception
   */
  @Bean
  AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}
