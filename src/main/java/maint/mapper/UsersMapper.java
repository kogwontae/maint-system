package maint.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import maint.dto.system.users.SearchUsersDto;
import maint.dto.system.users.UsersDetailDto;
import maint.dto.system.users.UsersDto;
import maint.entity.UsersEntity;

/**
 * 管理者MAPPER
 *
 * @author administrator
 */
@Mapper
public interface UsersMapper {

  /**
   * メールから管理者情報取得
   *
   * @param email メール
   * @return 管理者情報
   */
  UsersEntity getUserByLoginInfo(String email);

  /**
   * ユーザ数取得
   *
   * @param dto ユーザ検索DTO
   * @return ユーザ数
   */
  Long countUsersBySearch(SearchUsersDto dto);

  /**
   * ユーザ一覧取得
   *
   * @param dto ユーザ検索DTO
   * @return ユーザ一覧
   */
  List<UsersDetailDto> getUsersBySearch(SearchUsersDto dto);

  /**
   * ユーザ情報取得
   *
   * @param Id PK
   * @return ユーザ情報
   */
  UsersDetailDto getUserByPk(Long Id);

  /**
   * メールによるユーザ数取得(メール重複チェック用)
   *
   * @param pk      PK
   * @param email メール
   * @return ユーザ数
   */
  int countUsersByEmail(@Param("userId") Long pk, @Param("email") String email);

  /**
   * 登録
   *
   * @param usersEntity ユーザENTITY
   * @return 登録数
   */
  int registerUsers(UsersEntity usersEntity);

  /**
   * 変更
   *
   * @param usersEntity ユーザENTITY
   * @return 変更数
   */
  int updateUsers(UsersEntity usersEntity);

  /**
   * ユーザパスワード取得
   *
   * @param userId ユーザID（PK）
   * @return パスワード
   */
  String getUserPasswordByPK(Long userId);

  /**
   * ユーザパスワード変更処理
   *
   * @param entity ユーザEntity
   * @return 処理件数
   */
  int updateUserPassword(UsersEntity entity);

  /**
   * userIdからユーザDTO取得
   *
   * @param userId ユーザID
   * @return ユーザDTO
   */
  UsersDto getUserByEmail(Long userId);

  /**
   * ユーザパスワード変更処理（再発行）
   *
   * @param entity ユーザEntity
   * @return 処理件数
   */
  int updateUserPasswordReissue(UsersEntity entity);
}
