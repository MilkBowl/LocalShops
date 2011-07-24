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
package net.milkbowl.localshops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class SortedProperties extends Properties {

    private static final long serialVersionUID = 1L;

    /**
     * Overrides, called by the store method.
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized Enumeration<Object> keys() {
        Enumeration keysEnum = super.keys();
        List keyList = new ArrayList();
        while (keysEnum.hasMoreElements()) {
            keyList.add(keysEnum.nextElement());
        }
        Collections.sort(keyList);
        return Collections.enumeration(keyList);
    }
}
