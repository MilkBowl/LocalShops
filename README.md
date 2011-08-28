# LocalShops - A cuboid shop plugin for Bukkit
### http://forums.bukkit.org/threads/17131/

## Installing
Simple copy LocalShops.jar and Vault.jar to your <bukkit-directory>/plugins/ and then start
Bukkit!  Most configuration of the plugin can be done via in-game commands.
For those adventurous or for changes requiring editing of the config file, it
is required to edit the config while the plugin is not running.  The best way
is to stop Bukkit, edit the file, and then start Bukkit.  It is not supported
to edit while the plugin is running!


## Vault
Vault provides an abstraction layer for common Permissions and Economic plugins
using the Bukkit Services API to provide ease of use for server administrators,
and plugin developers alike.  Vault is required for LocalShops.


## Permissions
LocalShops has different types of shops that can be used, global and local.
Global shops are accessible and configurable based upon a world locale, while
Local shops are accessible and configurable based upon a cuboid within a world.
Permissions are separated to allow for different permission levels and as such
the type needs to be included.

 * localshops.(local|global).*
   - Users can buy and sell to shops but not make or manage them.
   - Sub nodes are:
     - localshops.(local|global).buy
     - localshops.(local|global).sell
     - localshops.(local|global).browse
 * localshops.manager.*
   - Owners and Managers can create, move or destroy LOCAL shops and manage them (using the set commands)
     - Sub nodes are:
       - localshops.manager.add
       - localshops.manager.create
       - localshops.manager.destroy
       - localshops.manager.move
       - localshops.manager.remove
       - localshops.manager.select
       - localshops.manager.set
       - localshops.manager.set.owner
 * localshops.admin.(local|global|server)
   - Allows administrative access to override manager permissions or to set serverwide settings
   - localshops.admin.global is required for a player to create the global shop for the world. 
     - **Any managers added to the shop will be able to manage it as if it was a local shop.**
 * localshops.free.create
   - Removes the cost associated with creating a shop.
 * localshops.free.move
   - Removes the cost associated with moving a shop.


## License
Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
 
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.


## Building
LocalShops provides an Ant build script (build.xml) which should be used when
building LocalShops.  Other methods may work, but are not supported or
documented.  To learn more about Ant, visit http://ant.apache.org.


## Jenkins
MilkBowl has an automated build server at http://ci.milkbowl.net which builds
LocalShops as necessary.


## Dependencies
LocalShops depends upon Vault (https://github.com/MilkBowl/Vault) for an
abstraction layer for Permissions, read README.vault for additional info and
documentation.  To update, simply pull the latest source code from their git
repository.
