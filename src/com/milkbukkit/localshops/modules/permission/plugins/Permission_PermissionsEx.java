package com.milkbukkit.localshops.modules.permission.plugins;


import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.milkbukkit.localshops.modules.permission.Permission;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Permission_PermissionsEx implements Permission {
    private String name = "PermissionsEx";
    private Plugin plugin = null;
    private PluginManager pluginManager = null;
    private PermissionsEx permission = null;
    private PermissionServerListener permissionServerListener = null;

    public Permission_PermissionsEx(Plugin plugin) {
        this.plugin = plugin;
        pluginManager = this.plugin.getServer().getPluginManager();

        permissionServerListener = new PermissionServerListener(this);

        this.pluginManager.registerEvent(Type.PLUGIN_ENABLE, permissionServerListener, Priority.Monitor, plugin);
        this.pluginManager.registerEvent(Type.PLUGIN_DISABLE, permissionServerListener, Priority.Monitor, plugin);

        // Load Plugin in case it was loaded before
        if (permission == null) {
            Plugin perms = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
            if (perms != null) {
                if (perms.isEnabled()) {
                    permission = (PermissionsEx) perms;
                    log.info(String.format("[%s][Permission] %s hooked.", plugin.getDescription().getName(), name));
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        if(permission == null) {
            return false;
        } else {
            return permission.isEnabled();
        }
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        return this.permission.has(player, permission);
    }

    private class PermissionServerListener extends ServerListener {
        Permission_PermissionsEx permission = null;

        public PermissionServerListener(Permission_PermissionsEx permission) {
            this.permission = permission;
        }

        public void onPluginEnable(PluginEnableEvent event) {
            if (permission.permission == null) {
                Plugin perms = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");

                if (perms != null) {
                    if (perms.isEnabled()) {
                        permission.permission = (PermissionsEx) perms;
                        log.info(String.format("[%s][Permission] %s hooked.", plugin.getDescription().getName(), permission.name));
                    }
                }
            }
        }

        public void onPluginDisable(PluginDisableEvent event) {
            if (permission.permission != null) {
                if (event.getPlugin().getDescription().getName().equals("PermissionsEx")) {
                    permission.permission = null;
                    log.info(String.format("[%s][Permission] %s un-hooked.", plugin.getDescription().getName(), permission.name));
                }
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
