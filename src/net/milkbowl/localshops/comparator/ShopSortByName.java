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
package net.milkbowl.localshops.comparator;

import java.io.Serializable;
import java.util.Comparator;

import net.milkbowl.localshops.objects.Shop;

public class ShopSortByName implements Comparator<Shop>, Serializable {

    private static final long serialVersionUID = 30000L;

    @Override
    public int compare(Shop o1, Shop o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
