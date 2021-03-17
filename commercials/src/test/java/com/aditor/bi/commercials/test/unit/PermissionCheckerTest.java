package com.aditor.bi.commercials.test.unit;


import com.aditor.bi.commercials.domain.*;
import com.aditor.bi.commercials.security.PermissionChecker;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PermissionCheckerTest {

    @Test
    public void testBasicAllPermission() {
        User user = new User();
        user.setPermissions(Arrays.asList(new Permission("*", "*", ActionType.ALL)));

        Commercial commercial = new Commercial("A", null, EventType.INSTALL, null, null, null);

        PermissionChecker permissionChecker = new PermissionChecker();
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.READ));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.UPDATE));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.CREATE));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.DELETE));
    }

    @Test
    public void testOnlyRead() {
        User user = new User();
        user.setPermissions(Arrays.asList(new Permission("A", "*", ActionType.READ)));

        Commercial commercial = new Commercial("A", null, EventType.INSTALL, null, null, null);

        PermissionChecker permissionChecker = new PermissionChecker();
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.READ));
        Assert.assertFalse(permissionChecker.isAllowedTo(commercial, user, ActionType.UPDATE));
        Assert.assertFalse(permissionChecker.isAllowedTo(commercial, user, ActionType.CREATE));
        Assert.assertFalse(permissionChecker.isAllowedTo(commercial, user, ActionType.DELETE));
    }

    @Test
    public void testOnlyUpdate() {
        User user = new User();
        user.setPermissions(Arrays.asList(new Permission("A", "*", ActionType.READ), new Permission("A", "*", ActionType.UPDATE)));

        Commercial commercial = new Commercial("A", null, EventType.INSTALL, null, null, null);

        PermissionChecker permissionChecker = new PermissionChecker();
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.READ));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.UPDATE));
        Assert.assertFalse(permissionChecker.isAllowedTo(commercial, user, ActionType.CREATE));
        Assert.assertFalse(permissionChecker.isAllowedTo(commercial, user, ActionType.DELETE));
    }

    @Test
    public void testCreateAndUpdate() {
        User user = new User();
        user.setPermissions(Arrays.asList(new Permission("A", "*", ActionType.READ),
                new Permission("A", "*", ActionType.UPDATE),
                new Permission("A", "*", ActionType.CREATE)));

        Commercial commercial = new Commercial("A", null, EventType.INSTALL, null, null, null);

        PermissionChecker permissionChecker = new PermissionChecker();
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.READ));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.UPDATE));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.CREATE));
        Assert.assertFalse(permissionChecker.isAllowedTo(commercial, user, ActionType.DELETE));
    }

    @Test
    public void testAllPermissions() {
        User user = new User();
        user.setPermissions(Arrays.asList(new Permission("A", "*", ActionType.ALL)));

        Commercial commercial = new Commercial("A", null, EventType.INSTALL, null, null, null);

        PermissionChecker permissionChecker = new PermissionChecker();
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.READ));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.UPDATE));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.CREATE));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercial, user, ActionType.DELETE));
    }

    @Test
    public void testMatching() {
        User user = new User();
        user.setPermissions(Arrays.asList(new Permission("A", "*", ActionType.ALL)));

        Commercial commercialA = new Commercial("A", null, EventType.INSTALL, null, null, null);
        Commercial commercialB = new Commercial("B", null, EventType.INSTALL, null, null, null);

        PermissionChecker permissionChecker = new PermissionChecker();
        Assert.assertTrue(permissionChecker.isAllowedTo(commercialA, user, ActionType.READ));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercialA, user, ActionType.UPDATE));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercialA, user, ActionType.CREATE));
        Assert.assertTrue(permissionChecker.isAllowedTo(commercialA, user, ActionType.DELETE));

        Assert.assertFalse(permissionChecker.isAllowedTo(commercialB, user, ActionType.READ));
        Assert.assertFalse(permissionChecker.isAllowedTo(commercialB, user, ActionType.UPDATE));
        Assert.assertFalse(permissionChecker.isAllowedTo(commercialB, user, ActionType.CREATE));
        Assert.assertFalse(permissionChecker.isAllowedTo(commercialB, user, ActionType.DELETE));
    }
}
