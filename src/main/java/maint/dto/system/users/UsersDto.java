package maint.dto.system.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * ユーザDTO
 *
 * @author administrator
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class UsersDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 6782781001195551092L;

  /** メール */
  private String email;
  /** ユーザ名 */
  private String userName;
  /** 姓 */
  private String lastName;
  /** 名 */
  private String firstName;
  /** パスワード */
  private String password;
  /** 権限名 */
  private String permRoleName;

}
