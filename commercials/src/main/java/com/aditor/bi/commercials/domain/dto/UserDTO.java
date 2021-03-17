package com.aditor.bi.commercials.domain.dto;

import com.aditor.bi.commercials.domain.Permission;

import java.util.List;

public class UserDTO {
    public String username;
    public String password;
    public List<Permission> permissions;
}
