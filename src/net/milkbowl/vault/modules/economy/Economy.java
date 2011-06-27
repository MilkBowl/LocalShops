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

package net.milkbowl.vault.modules.economy;

import java.util.logging.Logger;

public interface Economy {
    
    public static final Logger log = Logger.getLogger("Minecraft");

    public boolean isEnabled();
    public String getName();
    public String format(double amount);
    public EconomyResponse getBalance(String playerName);
    public EconomyResponse withdrawPlayer(String playerName, double amount);
    public EconomyResponse depositPlayer(String playerName, double amount);
}