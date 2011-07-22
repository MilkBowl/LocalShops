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

package net.milkbowl.localshops.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.commands.ShopCommandExecutor;
import net.milkbowl.localshops.objects.LocalShop;
import net.milkbowl.localshops.objects.PlayerData;
import net.milkbowl.localshops.objects.ShopSign;
import net.milkbowl.localshops.util.GenericFunctions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


/**
 * Handle events for all Player related events
 * 
 * @author Jonbas
 */
public class ShopsPlayerListener extends PlayerListener {
    private LocalShops plugin;

    // Logging
    private static final Logger log = Logger.getLogger("Minecraft");
    
    // List of items that can be consumed by right click
    private static final Set<Material> consumables = new HashSet<Material>();
    static {
        consumables.add(Material.APPLE);
        consumables.add(Material.GOLDEN_APPLE);
        consumables.add(Material.MUSHROOM_SOUP);
        consumables.add(Material.PORK);
        consumables.add(Material.BREAD);
        consumables.add(Material.COOKED_FISH);
        consumables.add(Material.RAW_FISH);
        consumables.add(Material.COOKIE);
    }

    public ShopsPlayerListener(LocalShops plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        String playerName = player.getName();
        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
        }
        Location eventBlockLoc = event.getClickedBlock().getLocation();
        LocalShop shop = plugin.getShopManager().getLocalShop(eventBlockLoc);
        //If user Right clicks a sign try to buy/sell from it.
        if (((event.getClickedBlock().getType().equals(Material.WALL_SIGN) || event.getClickedBlock().getType().equals(Material.SIGN_POST)) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK))  && shop != null) {
            // Ignore consumables & send message to a player
            if(consumables.contains(player.getItemInHand().getType())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_AQUA + "Oops!  You can't eat while working with shops!");
                return;
            }
            
            for (ShopSign sign : shop.getSigns()) {
            	if (sign == null)
            		continue;
                if (sign.getLoc().equals(eventBlockLoc)) {
                	//Check for null sign-type? We should NEVER have this issue
                	if (sign.getType() == null) {
                		log.log(Level.WARNING, "[LocalShops] - Null Shop Sign detected, report error. Sign info: {0}", sign.toString());
                		continue;
                	}
                    if (sign.getType().equals(ShopSign.SignType.BUY)) {
                        ShopCommandExecutor.commandTypeMap.get("buy").getCommandInstance(plugin, "buy", event.getPlayer(), "buy " + sign.getItemName()+ " " + sign.getAmount(), false).process();
                        //TODO: Remove when bukkit fixes inventory updating
                        try {
                            player.updateInventory();
                        } catch (Exception e) {}
                        return;
                    } else if (sign.getType().equals(ShopSign.SignType.SELL)) {
                        ShopCommandExecutor.commandTypeMap.get("sell").getCommandInstance(plugin, "sell", event.getPlayer(), "sell " + sign.getItemName() + " " + sign.getAmount(), false).process();
                        player.updateInventory();
                        return;
                    } else {
                        //Stop the loop if it's not a Buy/Sell Sign - only 1 possible sign location match
                        break;
                    }
                }
            }

        } 
        // If our user is select & is not holding an item, selection time
        if (plugin.getPlayerData().get(playerName).isSelecting() && (player.getItemInHand().getType() == Material.AIR || player.getItemInHand().getType() == Material.STICK)) {
        	PlayerData pData = plugin.getPlayerData().get(playerName);
            Location loc = event.getClickedBlock().getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                String size = null;
                pData.setPositionA(loc);
                if (pData.getPositionB() != null)
                    size = GenericFunctions.calculateCuboidSize(pData.getPositionA(), pData.getPositionB(), Config.getShopSizeMaxWidth(), Config.getShopSizeMaxHeight());
                if(size != null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "First Position " + ChatColor.LIGHT_PURPLE + x + " " + y + " " + z + ChatColor.DARK_AQUA + " size " + ChatColor.LIGHT_PURPLE + size);
                } else {
                    player.sendMessage(ChatColor.DARK_AQUA + "First Position " + ChatColor.LIGHT_PURPLE + x + " " + y + " " + z);
                }

                if(pData.getPositionA() != null && pData.getPositionB() == null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "Now, right click to select the far upper corner for the shop.");
                } else if(pData.getPositionA() != null && pData.getPositionB() != null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "Type " + ChatColor.WHITE + "/shop create [Shop Name]" + ChatColor.DARK_AQUA + ", if you're happy with your selection, otherwise keep selecting!");
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                pData.setPositionB(loc);
                String size = GenericFunctions.calculateCuboidSize(pData.getPositionA(), pData.getPositionB(), Config.getShopSizeMaxWidth(), Config.getShopSizeMaxHeight());
                if(size != null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "Second Position " + ChatColor.LIGHT_PURPLE + x + " " + y + " " + z + ChatColor.DARK_AQUA + " size " + ChatColor.LIGHT_PURPLE + size);
                } else {
                    player.sendMessage(ChatColor.DARK_AQUA + "Second Position " + ChatColor.LIGHT_PURPLE + x + " " + y + " " + z);
                }

                if(pData.getPositionB() != null && pData.getPositionA() == null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "Now, left click to select the bottom corner for a shop.");
                } else if(pData.getPositionA() != null && pData.getPositionB() != null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "Type " + ChatColor.WHITE + "/shop create [Shop Name]" + ChatColor.DARK_AQUA + ", if you're happy with your selection, otherwise keep selecting!");
                }
            }
        }

    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
        }

        plugin.checkPlayerPosition(player);
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().remove(playerName);
        }
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().remove(playerName);
        }
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
        }

        plugin.checkPlayerPosition(player, event.getTo());
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
        }

        plugin.checkPlayerPosition(player);
    }

    @Override
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
        }

        plugin.checkPlayerPosition(player);
    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
        }

        plugin.checkPlayerPosition(player);
    }
}
