package com.aditor.bi.commercials;


import com.aditor.bi.commercials.domain.Permission;
import com.aditor.bi.commercials.domain.User;
import com.aditor.bi.commercials.domain.dto.UserDTO;
import com.aditor.bi.commercials.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Secured(User.ROLE_ADMIN)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<User> getList() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/current")
    public User currentUser(@AuthenticationPrincipal User user) {
        return user;
    }

    @Secured(User.ROLE_ADMIN)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public User createUser(@RequestBody UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.username);
        if(user == null) {
            user = new User();
            user.setUsername(userDTO.username);
            user.setPassword(new BCryptPasswordEncoder().encode(userDTO.password));
            user.setPermissions(userDTO.permissions);
            for(Permission permission : user.getPermissions()) {
                permission.setUser(user);
            }

            user = userRepository.saveAndFlush(user);
        }

        return user;
    }

    @Secured(User.ROLE_ADMIN)
    @RequestMapping(value = "/{userId}/permissions", method = RequestMethod.GET)
    public List<Permission> getUserPermissions(@PathVariable Long userId) {
        return userRepository.findOne(userId).getPermissions();
    }

    @Secured(User.ROLE_ADMIN)
    @RequestMapping(value = "/{userId}/permissions", method = RequestMethod.PUT)
    public List<Permission> setUserPermissions(@PathVariable Long userId, @RequestBody List<Permission> permissions) {
        User user = userRepository.findOne(userId);
        user.getPermissions().clear();
        user = userRepository.saveAndFlush(user);

        for(Permission permission : permissions) {
            permission.setUser(user);
        }
        user.getPermissions().addAll(permissions);

        return userRepository.saveAndFlush(user).getPermissions();
    }

    @Secured(User.ROLE_ADMIN)
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        User user = userRepository.findOne(userId);
        if(!user.getRole().equals(User.ROLE_ADMIN)) {
            userRepository.delete(userId);
        }
    }
}
