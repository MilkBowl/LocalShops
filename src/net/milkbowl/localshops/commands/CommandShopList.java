/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package net.milkbowl.localshops.commands;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.comparator.ShopSortByName;
import net.milkbowl.localshops.objects.GlobalShop;
import net.milkbowl.localshops.objects.LocalShop;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.Shop;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShopList extends Command {

    public CommandShopList(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopList(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        int idWidth = Config.getUuidMinLength() + 1;
        if (idWidth < 4) {
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

        if (sender instanceof Player) {
            isPlayer = true;
        }

        if (isPlayer) {
            sender.sendMessage(String.format("%-" + idWidth + "s  %s", "Id", "Name"));
        } else {
            sender.sendMessage(String.format("%-" + idWidth + "s  %-25s %s", "Id", "Name", "Owner"));
        }

        List<Shop> shops = plugin.getShopManager().getAllShops();
        Collections.sort(shops, new ShopSortByName());

        //What is this here for?
        if (isGlobal && !canUseCommand(PermType.ADMIN_GLOBAL)) {
            // send nice message
        }

        Iterator<Shop> it = shops.iterator();
        while (it.hasNext()) {
            Shop shop = it.next();
            if (isGlobal) {
                if (!(shop instanceof GlobalShop)) {
                    continue;
                }
            } else {
                if (!(shop instanceof LocalShop)) {
                    continue;
                }

                if (!showAll && isPlayer && !isShopController(shop)) {
                    continue;
                }
            }

            if (isPlayer) {
                if (shop instanceof GlobalShop) {
                    if (((GlobalShop) shop).getWorlds().size() == 0) {
                        sender.sendMessage(String.format("%-" + idWidth + "s  %s *", shop.getShortUuidString(), shop.getName()));
                    } else {
                        sender.sendMessage(String.format("%-" + idWidth + "s  %s", shop.getShortUuidString(), shop.getName()));
                    }
                } else {
                    sender.sendMessage(String.format("%-" + idWidth + "s  %s", shop.getShortUuidString(), shop.getName()));
                }
            } else {
                if (shop instanceof GlobalShop) {
                    if (((GlobalShop) shop).getWorlds().size() == 0) {
                        sender.sendMessage(String.format("%-" + idWidth + "s  %-25s %s *", shop.getShortUuidString(), shop.getName(), shop.getOwner()));
                    } else {
                        sender.sendMessage(String.format("%-" + idWidth + "s  %-25s %s", shop.getShortUuidString(), shop.getName(), shop.getOwner()));
                    }
                } else {
                    sender.sendMessage(String.format("%-" + idWidth + "s  %-25s %s", shop.getShortUuidString(), shop.getName(), shop.getOwner()));
                }
            }
        }
        return true;
    }
}
