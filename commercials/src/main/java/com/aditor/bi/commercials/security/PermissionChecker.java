package com.aditor.bi.commercials.security;


import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.domain.ActionType;
import com.aditor.bi.commercials.domain.Permission;
import com.aditor.bi.commercials.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aditor.bi.commercials.domain.ActionType.*;

@Service
public class PermissionChecker {
    public boolean isAllowedTo(Commercial commercial, User user, ActionType actionType) {
        Map<ActionType, List<Permission>> userPermissionMap = new HashMap<>();
        user.getPermissions().forEach(permission -> {
            userPermissionMap.putIfAbsent(permission.getAction(), new ArrayList<>());
            userPermissionMap.get(permission.getAction()).add(permission);
        });

        if(!user.getRole().equals(User.ROLE_ADMIN)) {
            if(userPermissionMap.get(ALL) != null) {
                if(userPermissionMap.get(ALL).stream().anyMatch((permission) -> permissionMatchesCommercial(permission, commercial))) {
                    return true;
                }
            }
            if(userPermissionMap.get(actionType) == null)
                return false;
            return userPermissionMap.get(actionType).stream()
                    .anyMatch((permission) -> permissionMatchesCommercial(permission, commercial));
        }

        return true;
    }

    private boolean permissionMatchesCommercial(Permission permission, Commercial commercial) {
        return (permission.getOfferId().equals("*") || permission.getOfferId().equals(commercial.getOfferId())) &&
                (permission.getMediaSource().equals("*") || permission.getMediaSource().equals(commercial.getMediaSource()));
    }
}
