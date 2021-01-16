package com.xm.cpsmall.module.user.serialize.ex;

import com.xm.cpsmall.module.user.serialize.entity.SuPermissionEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuRoleEntity;
import lombok.Data;

import java.util.List;

@Data
public class RolePermissionEx extends SuRoleEntity {
    private List<SuPermissionEntity> suPermissionEntities;
}
