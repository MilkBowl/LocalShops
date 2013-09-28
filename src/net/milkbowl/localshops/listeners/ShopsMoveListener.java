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

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.PlayerData;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class ShopsMoveListener implements Listener {

    private LocalShops plugin = null;

    public ShopsMoveListener(LocalShops plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVehicleMove(VehicleMoveEvent event) {
        //We only check if a player has entered a shop if he has changed a full block.
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ()) {
            return;
        }

        Player player = null;
        Entity entity = event.getVehicle().getPassenger();
        if (entity instanceof Player) {
            player = (Player) entity;
        } else {
            return;
        }

        plugin.checkPlayerPosition(player);
    }
    

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
        }

        plugin.checkPlayerPosition(event.getPlayer(), event.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        //We only check if a player has entered a shop if he has changed a full block.
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ()) {
            return;
        }

        plugin.checkPlayerPosition(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        plugin.checkPlayerPosition(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.checkPlayerPosition(event.getPlayer());
    }
}
