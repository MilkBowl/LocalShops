package com.milkbukkit.localshops.commands;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Shop;
import com.milkbukkit.localshops.comparator.ShopSortByName;

public class CommandShopList extends Command {

    public CommandShopList(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }
    
    public CommandShopList(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        int idWidth = Config.getUuidMinLength() + 1;
        if(idWidth < 4) {
            idWidth = 4;
        }

        boolean showAll = false;
        boolean isPlayer = false;

        // list all
        Pattern pattern = Pattern.compile("(?i)list\\s+(.*)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            showAll = true;
        }        

        if(sender instanceof Player) {
            isPlayer = true;
        }

        if(isPlayer) {
            sender.sendMessage(String.format("%-"+idWidth+"s  %s", "Id", "Name"));
        } else {
            sender.sendMessage(String.format("%-"+idWidth+"s  %-25s %s", "Id", "Name", "Owner"));
        }
        
        List<Shop> shops = plugin.getShopManager().getAllShops();
        Collections.sort(shops, new ShopSortByName());
        
        Iterator<Shop> it = shops.iterator();
        while(it.hasNext()) {
            Shop shop = it.next();
            if(!showAll && isPlayer && !isShopController(shop)) {
                if (!shop.isGlobal())
                    continue;
            } else if (isGlobal) {
                if(!shop.isGlobal())
                    continue;
            }
            
            if(isPlayer) {
                sender.sendMessage(String.format("%-"+idWidth+"s  %s", shop.getShortUuidString(), shop.getName()));
            } else {
                sender.sendMessage(String.format("%-"+idWidth+"s  %-25s %s", shop.getShortUuidString(), shop.getName(), shop.getOwner()));
            }
        }
        return true;
    }
}
