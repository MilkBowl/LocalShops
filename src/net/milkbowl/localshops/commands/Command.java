/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send
 * a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View,
 * California, 94041, USA.
 * 
 */

package net.milkbowl.localshops.commands;

import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Logger;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.ResourceManager;
import net.milkbowl.localshops.objects.ItemInfo;
import net.milkbowl.localshops.objects.PlayerData;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.ShopLocation;
import net.milkbowl.localshops.util.GenericFunctions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public abstract class Command {

    // Attributes
    protected LocalShops plugin = null;
    protected String commandLabel = null;
    protected CommandSender sender = null;
    protected String command = null;
    protected static String DECIMAL_REGEX = "(\\d+\\.\\d+)|(\\d+\\.)|(\\.\\d+)|(\\d+)";
    protected static final Logger log = Logger.getLogger("Minecraft");
    protected boolean isGlobal = false;

    // Command Types Enum
    public static enum CommandTypes {
        ADMIN_LOCAL(0, new String[] { "localshops.admin.local" }),
        ADD(1, new String[] { "localshops.manager.add" }),
        BUY(2, new String[] { "localshops.local.buy" }),
        CREATE(3, new String[] { "localshops.manager.create" }),
        CREATE_FREE(4, new String[] { "localshops.free.create" }),
        DESTROY(5, new String[] { "localshops.manager.destroy" }),
        HELP(6, new String[] {}),
        BROWSE(7, new String[] { "localshops.local.browse" }),
        MOVE(8, new String[] { "localshops.manager.move" }),
        MOVE_FREE(9, new String[] { "localshops.free.move" }),
        REMOVE(10, new String[] { "localshops.manager.remove" }),
        SEARCH(11, new String[] {}),
        SELECT(12, new String[] { "localshops.manager.select" }),
        SELL(13, new String[] { "localshops.local.sell" }),
        SET_OWNER(14, new String[] { "localshops.manager.set.owner" }),
        SET(15, new String[] { "localshops.manager.set" }),
        GLOBAL_BUY(16, new String[] { "localshops.global.buy" }),
        GLOBAL_SELL(17, new String[] { "localshops.global.sell" } ),
        ADMIN_GLOBAL(18, new String[] { "localshops.admin.global" } ),
        ADMIN_SERVER(19, new String[] { "localshops.admin.server" }),
        GLOBAL_BROWSE(20, new String[] {"localshops.global.browse" }),
        MULTI_LOCATION(21, new String[] {"localshops.local.multilocation"});

        int id = -1;
        String[] permissions = null;

        CommandTypes(int id) {
            this.id = id;
        }

        CommandTypes(int id, String[] permissions) {
            this(id);
            this.permissions = permissions;
        }

        public int getId() {
            return id;
        }

        public String[] getPermissions() {
            return permissions;
        }
    }

    public Command(LocalShops plugin, String commandLabel, CommandSender sender, String command){
        this.plugin = plugin;
        this.commandLabel = commandLabel;
        this.sender = sender;
        this.command = command.trim();
    }

    public Command(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal){
        this.plugin = plugin;
        this.commandLabel = commandLabel;
        this.sender = sender;
        this.command = command.trim();
        this.isGlobal = isGlobal;
    }

    public Command(LocalShops plugin, String commandLabel, CommandSender sender, String[] args) {
        this(plugin, commandLabel, sender, GenericFunctions.join(args, " ").trim());
    }

    public Command(LocalShops plugin, String commandLabel, CommandSender sender, String[] args, boolean isGlobal) {
        this(plugin, commandLabel, sender, GenericFunctions.join(args, " ").trim(), isGlobal);
    }

    public String getCommand() {
        return command;
    }

    public boolean process() {
        // Does nothing and needs to be overloaded by subclasses
        return false;
    }

    protected boolean canUseCommand(CommandTypes type) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // check if admin first
            if (isGlobal) {
                for (String permission : CommandTypes.ADMIN_GLOBAL.getPermissions()) {
                    if (LocalShops.VAULT.getPermission().playerHasPermission(player, permission)) {
                        return true;
                    }
                }
            } else { 
                for (String permission : CommandTypes.ADMIN_LOCAL.getPermissions()) {
                    //Make sure this isn't a server command before allowing access.
                    if (LocalShops.VAULT.getPermission().playerHasPermission(player, permission) && !(this instanceof net.milkbowl.localshops.commands.CommandAdminSet))
                        return true;
                }
            }
            // fail back to provided permissions second
            for (String permission : type.getPermissions()) {
                if (!LocalShops.VAULT.getPermission().playerHasPermission(player, permission)) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    protected boolean canCreateShop(String playerName) {
        if ((canUseCommand(CommandTypes.ADMIN_LOCAL) && !isGlobal) || (canUseCommand(CommandTypes.ADMIN_GLOBAL) && isGlobal )) {
            return true;
        } else if (( plugin.getShopManager().numOwnedShops(playerName) < Config.getPlayerMaxShops() || Config.getPlayerMaxShops() < 0) && canUseCommand(CommandTypes.CREATE)) {
            return true;
        }

        return false;
    }

    protected boolean canModifyShop(Shop shop) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            // If owner, true
            if(shop.getOwner().equals(player.getName())) {
                return true;
            }
            // If manager, true
            if(shop.getManagers().contains(player.getName())) {
                return true;
            }
            // If admin, true
            if(canUseCommand(CommandTypes.ADMIN_LOCAL) && shop instanceof net.milkbowl.localshops.objects.LocalShop) {
                return true;
            } else if (canUseCommand(CommandTypes.ADMIN_GLOBAL) && shop instanceof net.milkbowl.localshops.objects.GlobalShop) {
                return true;
            }

            return false;
        } else {
            // Console, true
            return true;
        }
    }

    protected void givePlayerItem(ItemStack item, int amount) {
        Player player = (Player) sender;

        int maxStackSize = 64;

        // fill all the existing stacks first
        for (int i : player.getInventory().all(item.getType()).keySet()) {
            if (amount == 0)
                continue;
            ItemStack thisStack = player.getInventory().getItem(i);
            if (thisStack.getType().equals(item.getType()) && thisStack.getDurability() == item.getDurability()) {
                if (thisStack.getAmount() < maxStackSize) {
                    int remainder = maxStackSize - thisStack.getAmount();
                    if (remainder <= amount) {
                        amount -= remainder;
                        thisStack.setAmount(maxStackSize);
                    } else {
                        thisStack.setAmount(maxStackSize - remainder + amount);
                        amount = 0;
                    }
                }
            }

        }

        for (int i = 0; i < 36; i++) {
            ItemStack thisSlot = player.getInventory().getItem(i);
            if (thisSlot == null || thisSlot.getType() == Material.AIR) {
                if (amount == 0)
                    continue;
                if (amount >= maxStackSize) {
                    item.setAmount(maxStackSize);
                    player.getInventory().setItem(i, item);
                    amount -= maxStackSize;
                } else {
                    item.setAmount(amount);
                    player.getInventory().setItem(i, item);
                    amount = 0;
                }
            }
        }

        while (amount > 0) {
            if (amount >= maxStackSize) {
                item.setAmount(maxStackSize);
                amount -= maxStackSize;
            } else {
                item.setAmount(amount - maxStackSize);
                amount = 0;
            }
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }

    }

    /**
     * Returns true if the player is in the shop manager list or is the shop
     * owner
     * 
     * @param player
     * @param shop
     * @return
     */
    protected boolean isShopController(Shop shop) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (shop.getOwner().equalsIgnoreCase(player.getName()))
                return true;
            if (shop.getManagers() != null) {
                for (String manager : shop.getManagers()) {
                    if (player.getName().equalsIgnoreCase(manager)) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return true;
        }
    }

    protected int countItemsInInventory(PlayerInventory inventory, ItemStack item) {
        int totalAmount = 0;
        boolean isDurable = LocalShops.getItemList().isDurable(item);

        for (Integer i : inventory.all(item.getType()).keySet()) {
            ItemStack thisStack = inventory.getItem(i);
            if (isDurable) {
                int damage = calcDurabilityPercentage(thisStack);
                if (damage > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0)
                    continue;
            } else {
                if (thisStack.getDurability() != item.getDurability())
                    continue;
            }
            totalAmount += thisStack.getAmount();
        }

        return totalAmount;
    }

    protected static int calcDurabilityPercentage(ItemStack item) {

        // calc durability prcnt
        short damage;
        if (item.getType() == Material.IRON_SWORD) {
            damage = (short) ((double) item.getDurability() / 250 * 100);
        } else {
            damage = (short) ((double) item.getDurability() / (double) item.getType().getMaxDurability() * 100);
        }

        return damage;
    }

    protected int removeItemsFromInventory(PlayerInventory inventory, ItemStack item, int amount) {

        boolean isDurable = LocalShops.getItemList().isDurable(item);

        // remove number of items from player adding stock
        for (int i : inventory.all(item.getType()).keySet()) {
            if (amount == 0)
                continue;
            ItemStack thisStack = inventory.getItem(i);
            if (isDurable) {
                int damage = calcDurabilityPercentage(thisStack);
                if (damage > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0)
                    continue;
            } else {
                if (thisStack.getDurability() != item.getDurability())
                    continue;
            }

            int foundAmount = thisStack.getAmount();
            if (amount >= foundAmount) {
                amount -= foundAmount;
                inventory.setItem(i, null);
            } else {
                thisStack.setAmount(foundAmount - amount);
                inventory.setItem(i, thisStack);
                amount = 0;
            }
        }

        return amount;

    }


    protected int countAvailableSpaceForItemInInventory(PlayerInventory inventory, ItemInfo item) {
        int count = 0;
        for (ItemStack thisSlot : inventory.getContents()) {
            if (thisSlot == null || thisSlot.getType() == Material.AIR) {
                count += 64;
                continue;
            }
            if (thisSlot.getTypeId() == item.typeId && thisSlot.getDurability() == item.subTypeId) {
                count += 64 - thisSlot.getAmount();
            }
        }

        return count;
    }

    protected boolean notifyPlayers(Shop shop, String[] messages) {
        Iterator<PlayerData> it = plugin.getPlayerData().values().iterator();
        while(it.hasNext()) {
            PlayerData p = it.next();
            if(p.shopList.contains(shop.getUuid())) {
                Player thisPlayer = plugin.getServer().getPlayer(p.playerName);
                for(String message : messages) {
                    thisPlayer.sendMessage(message);
                }
            }
        }
        return true;
    }

    protected Shop getCurrentShop(Player player) {
        Shop shop = null;
        UUID shopUuid = null;
        PlayerData pData = plugin.getPlayerData().get(player.getName());
        // Get Current Shop
        if (isGlobal) {
            shop = plugin.getShopManager().getGlobalShop(player.getWorld());
        } else if (!isGlobal) {
            shopUuid = pData.getCurrentShop();
            if (shopUuid != null) {
                shop = plugin.getShopManager().getLocalShop(shopUuid);
            }
        }

        return shop;
    }
    
    //Checks if a player has permission to access the shop
    public boolean hasShopAccess(Player player, Shop shop) {
        //Assume shop allows access to anyone if Sets are empty.
        if (shop.getUserSet().isEmpty() && shop.getGroupSet().isEmpty())
            return true;
        else if (shop.getUserSet().contains(player.getName()))
            return true;
        
        for (String groupName : shop.getGroupSet())
            if ( LocalShops.VAULT.getPermission().playerInGroup(player.getWorld().getName(), player.getName(), groupName) )
                return true;
        
        return false;
    }
    
    public ShopLocation getNewShopLoc(Player player) {
    	PlayerData pData = plugin.getPlayerData().get(player.getName());
    	 if (pData.isSelecting()) {
             if (GenericFunctions.calculateCuboidSize(pData.getPositionA(), pData.getPositionB(), Config.getShopSizeMaxWidth(), Config.getShopSizeMaxHeight()) == null) {
                 String size = Config.getShopSizeMaxWidth() + "x" + Config.getShopSizeMaxHeight() + "x" + Config.getShopSizeMaxWidth();
                 player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_SELECTION_PROB_SIZE, new String[] { "%SIZE%" }, new Object[] { size }));
                 return null;
             }
             
             
             if (pData.getPositionA() == null || pData.getPositionB() == null) {
                 player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_SELECTION_PROB_ONLY_ONE_POINT));
                 return null;
             } else
                 return new ShopLocation(pData.getPositionA(), pData.getPositionB());
         } else {
        	 int x1 = 0, x2 = 0, z1 = 0, z2 = 0;
             // get current position
             Location loc = player.getLocation();
             int x = loc.getBlockX();
             int y = loc.getBlockY();
             int z = loc.getBlockZ();
             
             if (Config.getShopSizeDefWidth() % 2 == 0) {
                 x1 = x - (Config.getShopSizeDefWidth() / 2);
                 x2 = x + (Config.getShopSizeDefWidth() / 2);
                 z1 = z - (Config.getShopSizeDefWidth() / 2);
                 z2 = z + (Config.getShopSizeDefWidth() / 2);
             } else {
                 x1 = x - (Config.getShopSizeDefWidth() / 2) + 1;
                 x2 = x + (Config.getShopSizeDefWidth() / 2);
                 z1 = z - (Config.getShopSizeDefWidth() / 2) + 1;
                 z2 = z + (Config.getShopSizeDefWidth() / 2);
             }
             //generate the new shopLocation
             return new ShopLocation(x1, y - 1, z1, x2, y + Config.getShopSizeDefHeight() - 1, z2);
         }
    }
}
