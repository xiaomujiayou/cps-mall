package com.xm.cpsmall.module.user.serialize.ex;

import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleEx extends SuUserEntity {
    private List<RolePermissionEx> roleExes;
}
