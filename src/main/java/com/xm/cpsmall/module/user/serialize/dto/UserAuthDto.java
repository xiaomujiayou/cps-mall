package com.xm.cpsmall.module.user.serialize.dto;

import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.serialize.ex.UserRoleEx;
import lombok.Data;

import java.util.List;

@Data
public class UserAuthDto extends SuUserEntity {
    private List<UserRoleEx> userRoleExes;
}
