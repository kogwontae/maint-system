package maint.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import maint.entity.PermissionRolesEntity;
import maint.mapper.PermissionRolesMapper;

import java.util.List;

/**
 * 共通SERVICE
 *
 * @author cho hyeonbi
 */
@Service
public class CommonService extends BaseService {

  /** 権限 MAPPER */
  @Autowired
  PermissionRolesMapper permissionRolesMapper;


  /**
   * 権限リスト取得
   *
   * @return 権限一覧
   */
  public List<PermissionRolesEntity> getPermissionRolesList() {
    return permissionRolesMapper.getPermissionRoles();
  }

}
