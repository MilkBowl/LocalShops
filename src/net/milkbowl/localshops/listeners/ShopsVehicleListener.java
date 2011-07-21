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
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

/**
 *
 * @author mhumes
 */
public class ShopsVehicleListener extends VehicleListener {
    private LocalShops plugin = null;

    public ShopsVehicleListener(LocalShops plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onVehicleMove(VehicleMoveEvent event) {
        Player player = null;
        Entity entity = event.getVehicle().getPassenger();
        if(entity instanceof Player) {
            player = (Player) entity;
        } else {
            return;
        }
        String playerName = player.getName();

        if (!plugin.getPlayerData().containsKey(playerName)) {
            plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
        }

        plugin.checkPlayerPosition(player, event.getTo());
    }
}
