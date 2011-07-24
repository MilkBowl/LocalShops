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
package net.milkbowl.localshops.objects;

public enum PermType {

    ADMIN_LOCAL("localshops.admin.local"),
    ADD("localshops.manager.add"),
    BUY("localshops.local.buy"),
    CREATE("localshops.manager.create"),
    CREATE_FREE("localshops.free.create"),
    DESTROY("localshops.manager.destroy"),
    HELP(""),
    BROWSE("localshops.local.browse"),
    MOVE("localshops.manager.move"),
    MOVE_FREE("localshops.free.move"),
    REMOVE("localshops.manager.remove"),
    SEARCH(null),
    SELECT("localshops.manager.select"),
    SELL("localshops.local.sell"),
    SET_OWNER("localshops.manager.set.owner"),
    SET("localshops.manager.set"),
    GLOBAL_BUY("localshops.global.buy"),
    GLOBAL_SELL("localshops.global.sell"),
    ADMIN_GLOBAL("localshops.admin.global"),
    ADMIN_SERVER("localshops.admin.server"),
    GLOBAL_BROWSE("localshops.global.browse"),
    MULTI_LOCATION("localshops.local.multilocation");
    String permissions = null;

    PermType(String permissions) {
        this.permissions = permissions;
    }

    public String get() {
        return permissions;
    }
}
