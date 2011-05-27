package com.milkbukkit.localshops.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.util.GenericFunctions;

public class ShopCommandExecutor implements CommandExecutor {

    private final LocalShops plugin;
    private final Logger log = Logger.getLogger("Minecraft");
    private static Map<String, CommandTypeInfo> commandTypeMap = new HashMap<String, CommandTypeInfo>();
    static {
        commandTypeMap.put("admin", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandAdminSet.class, false, false, false));
        commandTypeMap.put("add", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopAdd.class, true, true, false));
        commandTypeMap.put("addloc", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopLink.class, false, true, false));
        commandTypeMap.put("browse", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopBrowse.class, true, true, false));
        commandTypeMap.put("bro", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopBrowse.class, true, true, false));
        commandTypeMap.put("buy", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopBuy.class, true, true, false));
        commandTypeMap.put("create", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopCreate.class, true, true, true));
        commandTypeMap.put("debug", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopDebug.class, true, true, false));
        commandTypeMap.put("del", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopDestroy.class, true, true, true));
        commandTypeMap.put("delete", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopDestroy.class, true, true, true));
        commandTypeMap.put("destroy", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopDestroy.class, true, true, true));
        commandTypeMap.put("find", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopFind.class, true, false, false));
        commandTypeMap.put("help", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopHelp.class, true, true, false));
        commandTypeMap.put("info", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopInfo.class, true, true, false));
        commandTypeMap.put("link", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopLink.class, false, true, false));
        commandTypeMap.put("list", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopList.class, true, true, false));
        commandTypeMap.put("move", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopMove.class, true, false, true));
        commandTypeMap.put("remove", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopRemove.class, true, true, false));
        commandTypeMap.put("removeloc", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopUnlink.class, false, true, false));
        commandTypeMap.put("search", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopSearch.class, true, true, false));
        commandTypeMap.put("select", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopSelect.class, true, false, false));
        commandTypeMap.put("sell", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopSell.class, true, true, false));
        commandTypeMap.put("set", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopSet.class, true, true, false));
        commandTypeMap.put("unlink", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopUnlink.class, false, true, false));
        commandTypeMap.put("version", new CommandTypeInfo(com.milkbukkit.localshops.commands.CommandShopVersion.class, true, true, false));
    }
    
    public ShopCommandExecutor(LocalShops plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String type = null;
        boolean global = false;
        String user = "CONSOLE";
        
        if (sender instanceof Player) {
            user = ((Player) sender).getName();
        }

        String cmdString = null;
        if(commandLabel.equalsIgnoreCase("lsadmin")) {
            cmdString = GenericFunctions.join(args, " ");
            type = "admin";
        } else if (commandLabel.equalsIgnoreCase("buy")) {
            cmdString = "buy " + GenericFunctions.join(args, " ");
            type = "buy";
        } else if (commandLabel.equalsIgnoreCase("sell")) {
            cmdString = "sell " + GenericFunctions.join(args, " ");
            type = "sell";
        } else if (commandLabel.equalsIgnoreCase("gbuy")) {
            cmdString = "buy " + GenericFunctions.join(args, " ");
            type = "buy";
            global = true;
        } else if (commandLabel.equalsIgnoreCase("gsell")) {
            cmdString = "sell" + GenericFunctions.join(args, " ");
            type = "sell";
            global = true;
        } else {
            if (args.length > 0) {
                cmdString = GenericFunctions.join(args, " ");
                type = args[0].toLowerCase();
            } else if (command.getName().equalsIgnoreCase("gshop")){
                return (new CommandShopHelp(plugin, commandLabel, sender, args, true)).process();
            } else {
                return (new CommandShopHelp(plugin, commandLabel, sender, args, false)).process();
            }
            
            if(commandLabel.equalsIgnoreCase("gshop")) {
                global = true;
            }
        }

        CommandTypeInfo cInfo = commandTypeMap.get(type);
        if (cInfo == null) {
            commandTypeMap.get("help").getCommandInstance(plugin, commandLabel, sender, cmdString, global).process();
            return true;
        } else {
            com.milkbukkit.localshops.commands.Command cmd = cInfo.getCommandInstance(plugin, commandLabel, sender, cmdString, global);
            if (cmd != null) {
                boolean cVal = cmd.process();
                if (cVal && cInfo.checkPlayerPositions) {
                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                        plugin.playerListener.checkPlayerPosition(player);
                    }
                }

                log.info(String.format("[%s] %s issued %s command: %s", plugin.getDescription().getName(), user, global ? "global" : "local", cmd.getCommand()));

                return cVal;
            } else {
                return false;
            }
        }
    }
}
