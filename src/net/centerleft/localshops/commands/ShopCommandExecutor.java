package net.centerleft.localshops.commands;

import java.util.logging.Logger;

import net.centerleft.localshops.LocalShops;
import net.centerleft.localshops.Search;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommandExecutor implements CommandExecutor {
    
    private final LocalShops plugin;
    private final Logger log = Logger.getLogger("Minecraft");
    
    public ShopCommandExecutor(LocalShops plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String type = null;
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
        } else {
            if (args.length > 0) {
                cmdString = Search.join(args, " ");
                type = args[0];
            } else {
                return (new CommandShopHelp(plugin, commandLabel, sender, args)).process();
            }
        }

        String commandName = command.getName().toLowerCase();
        
        net.centerleft.localshops.commands.Command cmd = null;
        boolean checkPlayerPos = false;

        if (commandName.equalsIgnoreCase("lshop") || commandLabel.equalsIgnoreCase("buy") || commandLabel.equalsIgnoreCase("sell")) {
            if (type.equalsIgnoreCase("search")) {
                cmd = new CommandShopSearch(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("find")) {
                cmd = new CommandShopFind(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("debug")) {
                cmd = new CommandShopDebug(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("create")) {
                cmd = new CommandShopCreate(plugin, commandLabel, sender, cmdString);
                checkPlayerPos = true;
            } else if (type.equalsIgnoreCase("destroy")) {
                cmd = new CommandShopDestroy(plugin, commandLabel, sender, cmdString);
                checkPlayerPos = true;
            } else if (type.equalsIgnoreCase("move")) {
                cmd = new CommandShopMove(plugin, commandLabel, sender, cmdString);
                checkPlayerPos = true;
            } else if (type.equalsIgnoreCase("browse") || type.equalsIgnoreCase("bro")) {
                cmd = new CommandShopBrowse(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("sell")) {
                cmd = new CommandShopSell(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("add")) {
                cmd = new CommandShopAdd(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("remove")) {
                cmd = new CommandShopRemove(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("buy")) {
                cmd = new CommandShopBuy(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("set")) {
                cmd = new CommandShopSet(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("select")) {
                cmd = new CommandShopSelect(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("list")) {
                cmd = new CommandShopList(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("info")) {
                cmd = new CommandShopInfo(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("version")) {
                cmd = new CommandShopVersion(plugin, commandLabel, sender, cmdString);
            } else if (type.equalsIgnoreCase("global")) {
                cmd = new CommandShopVersion(plugin, commandLabel, sender, cmdString);
            } else {
                cmd = new CommandShopHelp(plugin, commandLabel, sender, cmdString);
            }
            
            log.info(String.format("[%s] %s issued: %s", plugin.getDescription().getName(), user, cmd.getCommand()));
            boolean cVal = cmd.process();
            if(cVal && checkPlayerPos) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    plugin.playerListener.checkPlayerPosition(player);
                }
            }
            
            return cVal;
        }
        return false;
    }
}
