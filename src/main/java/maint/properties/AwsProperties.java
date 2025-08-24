package maint.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * AWS Properties
 *
 * @author administrator
 */
@Component
@ConfigurationProperties(prefix = "aws")
@Data
public class AwsProperties {

  /** SESプロファイル */
  private String sesProfile;
  /** 送信者名 */
  private String fromName;
  /** 送信者のメールアドレス */
  private String adminEmail;

}
