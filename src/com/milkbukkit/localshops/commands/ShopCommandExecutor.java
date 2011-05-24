package com.milkbukkit.localshops.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Search;

public class ShopCommandExecutor implements CommandExecutor {

    private final LocalShops plugin;
    private final Logger log = Logger.getLogger("Minecraft");
    private static Map<String, SubCommandInfo> subCommandMap = new HashMap<String, SubCommandInfo>();
    static {
        subCommandMap.put("add", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopAdd.class, true, true, false));
        subCommandMap.put("browse", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopBrowse.class, true, true, false));
        subCommandMap.put("buy", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopBuy.class, true, true, false));
        subCommandMap.put("create", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopCreate.class, true, true, true));
        subCommandMap.put("debug", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopDebug.class, true, true, false));
        subCommandMap.put("destroy", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopDestroy.class, true, true, true));
        subCommandMap.put("find", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopFind.class, true, false, false));
        subCommandMap.put("help", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopHelp.class, true, true, false));
        subCommandMap.put("info", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopInfo.class, true, true, false));
        subCommandMap.put("link", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopLink.class, false, true, false));
        subCommandMap.put("list", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopList.class, true, false, false));
        subCommandMap.put("move", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopMove.class, true, false, true));
        subCommandMap.put("remove", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopRemove.class, true, true, false));
        subCommandMap.put("search", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopSearch.class, true, true, false));
        subCommandMap.put("select", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopSelect.class, true, false, false));
        subCommandMap.put("sell", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopSell.class, true, true, false));
        subCommandMap.put("set", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopSet.class, true, true, false));
        subCommandMap.put("unlink", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopUnlink.class, false, true, false));
        subCommandMap.put("version", new SubCommandInfo(com.milkbukkit.localshops.commands.CommandShopVersion.class, true, true, false));
        
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
        if (commandLabel.equalsIgnoreCase("buy")) {
            cmdString = "buy " + Search.join(args, " ");
            type = "buy";
        } else if (commandLabel.equalsIgnoreCase("sell")) {
            cmdString = "sell " + Search.join(args, " ");
            type = "sell";
        } else if (commandLabel.equalsIgnoreCase("gbuy")) {
            cmdString = "buy " + Search.join(args, " ");
            type = "buy";
            global = true;
        } else if (commandLabel.equalsIgnoreCase("gsell")) {
            cmdString = "sell" + Search.join(args, " ");
            type = "sell";
            global = true;
        } else {
            if (args.length > 0) {
                cmdString = Search.join(args, " ");
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

        SubCommandInfo cInfo = subCommandMap.get(type);
        com.milkbukkit.localshops.commands.Command cmd = cInfo.getCommandInstance(plugin, commandLabel, sender, cmdString, global);
        if (cmd != null) {
            boolean cVal = cmd.process();
            if (cVal && cInfo.checkPlayerPositions) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    plugin.playerListener.checkPlayerPosition(player);
                }
            }

            log.info(String.format("[%s] %s issued %s command: %s", plugin.getDescription().getName(), global ? "global" : "local", user, cmd.getCommand()));
            
            return cVal;
        } else {
            return false;
        }
    }
}
