package com.milkbukkit.localshops.modules.permission.plugins;


import org.bukkit.entity.Player;

import com.milkbukkit.localshops.modules.permission.Permission;

public class Permission_None implements Permission {
    private String name = "Local Fallback Permissions";

    @Override
    public boolean isEnabled() {
        // This method is essentially static, it is always enabled if LS is!
        return true;
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        // Allow OPs to everything
        if(player.isOp()) {
            return true;
        }
        
        // Allow everyone user & manager
        if(permission.startsWith("localshops.user") || permission.startsWith("localshops.manager")) {
            return true;
        }
        
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

}
