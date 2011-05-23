/**
 * 
 */
package net.centerleft.localshops;

import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * @author sleaker
 *
 */
public class ShopsWorldListener extends WorldListener{
    private LocalShops plugin;
    
    ShopsWorldListener (LocalShops plugin) {
        this.plugin = plugin;
    }
    
    public void onWorldLoad(WorldLoadEvent event) {
        LocalShops.setupGlobalShop(event.getWorld().getName());
    }
}
