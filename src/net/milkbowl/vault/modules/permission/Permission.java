/**
 * Copyright 2011 Morgan Humes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.milkbowl.vault.modules.permission;

import java.util.logging.Logger;

import org.bukkit.entity.Player;

public interface Permission {
    
    public static final Logger log = Logger.getLogger("Minecraft");

    public String getName();
    public boolean isEnabled();
    public boolean hasPermission(Player player, String permission);
    public boolean inGroup(String worldName, String playerName, String groupName);
    public int getInfoInt(String world, String playerName, String node);

}
