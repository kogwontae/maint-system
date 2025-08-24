package maint.dto.system.users;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.properties.AwsProperties;

/**
 * 業務WEBユーザ向けメール本文DTO
 *
 * @author administrator
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class UserMailContentsDto implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = -5493414118635310451L;

  /** Eメール */
  private String email;
  /** ユーザ名 */
  private String userName;
  /** パスワード */
  private String password;
  /** 権限名 */
  private String permRoleName;
  /** 返信用メールアドレス */
  private String replyTo;

  /**
   * コンストラクタ
   *
   * @param dto           ユーザ DTO
   * @param awsProperties AWS Properties
   * @param password      パスワード
   */
  public UserMailContentsDto(UsersDto dto, AwsProperties awsProperties, String password) {
    this.email = dto.getEmail();
    this.userName = dto.getLastName() + dto.getFirstName();
    this.password = password;
    this.permRoleName = dto.getPermRoleName();
    this.replyTo = awsProperties.getAdminEmail();
  }

}
