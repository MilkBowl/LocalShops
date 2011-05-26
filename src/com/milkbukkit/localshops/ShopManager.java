package com.milkbukkit.localshops;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.bukkit.Location;

public class ShopManager {
    private LocalShops plugin = null;
    private Map<UUID, Shop> shops = Collections.synchronizedMap(new HashMap<UUID, Shop>());

    // Logging
    private final Logger log = Logger.getLogger("Minecraft");

    public ShopManager(LocalShops plugin) {
        this.plugin = plugin;
    }

    public Shop getShop(UUID uuid) {
        return shops.get(uuid);
    }

    public Shop getShop(String partialUuid) {       
        Iterator<Shop> it = shops.values().iterator();
        while (it.hasNext()) {
            Shop cShop = it.next();
            if(cShop.getUuid().toString().matches(".*"+partialUuid.toLowerCase()+"$")) {
                return cShop;
            }
        }

        return null;
    }

    public Shop getShop(Location loc) {
        return getShop(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public Shop getShop(String world, int x, int y, int z) {
        for(Shop shop : shops.values()) {
            if (shop.isGlobal() || !shop.getWorld().equals(world)) {
                continue;
            }
            
            ShopLocation[] sLocs = shop.getLocations();
            if(sLocs.length != 2) {
                continue;
            }

            if(shop.containsPoint(world, x, y, z)) {
                return shop;
            }
        }
        return null;
    }

    public boolean shopPositionOk(int[] xyzA, int[] xyzB, String worldName) {
        // make sure coords are in right order
        for (int i = 0; i < 3; i++) {
            if (xyzA[i] > xyzB[i]) {
                int temp = xyzA[i];
                xyzA[i] = xyzB[i];
                xyzB[i] = temp;
            }
        }

        // Need to test every position to account for variable shop sizes

        for (int x = xyzA[0]; x <= xyzB[0]; x++) {
            for (int z = xyzA[2]; z <= xyzB[2]; z++) {
                for (int y = xyzA[1]; y <= xyzB[1]; y++) {
                    for(Shop shop : shops.values()) {
                        if(shop.containsPoint(worldName, x, y, z)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void addShop(Shop shop) {
        if(Config.getSrvDebug()) {
            log.info(String.format("[%s] Adding %s", plugin.pdfFile.getName(), shop.toString()));
        }
        String uuid = shop.getUuid().toString();
        while (true) {
            if (Config.uuidListContains(uuid.substring(uuid.length() - Config.getUuidMinLength()))) {
                calcShortUuidSize();
            } else {
                Config.addUuidList(uuid.substring(uuid.length() - Config.getUuidMinLength()));
                break;
            }
        }
        shops.put(shop.getUuid(), shop);
    }

    private void calcShortUuidSize() {
        if(Config.getUuidMinLength() < 36) {
            Config.incrementUuidMinLength();
        }
        Config.clearUuidList();
        Iterator<Shop> it = shops.values().iterator();
        while (it.hasNext()) {
            Shop cShop = it.next();
            String cUuid = cShop.getUuid().toString();
            String sUuid = cUuid.substring(cUuid.length() - Config.getUuidMinLength());
            if (Config.uuidListContains(sUuid)) {
                calcShortUuidSize();
            } else {
                Config.addUuidList(sUuid);
            }
        }
    }

    public List<Shop> getAllShops() {
        return new ArrayList<Shop>(shops.values());
    }

    public int getNumShops() {
        return shops.size();
    }

    public int numOwnedShops(String playerName) {
        int numShops = 0;
        for ( Shop shop : shops.values() ) {
            if (shop.getOwner().equals(playerName) ) {
                numShops++;
            }
        }
        return numShops;
    }

    public void loadShops(File shopsDir) {
        if(Config.getSrvDebug()) {
            log.info(String.format("[%s] %s.%s", plugin.pdfFile.getName(), "ShopData", "loadShops(File shopsDir)"));
        }

        File[] shopsList = shopsDir.listFiles();
        for (File file : shopsList) {

            if(Config.getSrvDebug()) {
                log.info(String.format("[%s] Loading Shop file \"%s\".", plugin.pdfFile.getName(), file.toString()));
            }
            Shop shop = null;

            // Determine if filename is a UUID or not
            if(file.getName().matches("^(\\{{0,1}([0-9a-fA-F]){8}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){12}\\}{0,1})\\.shop$")) {
                try {
                    shop = loadShop(file);
                } catch(Exception e) {
                    // log error
                    log.info(String.format("[%s] Error loading Shop file \"%s\", ignored.", plugin.pdfFile.getName(), file.toString()));
                }
            } else {
                // Convert old format & delete the file...immediately save using the new format (will generate a new UUID for this shop)
                shop = convertShopOldFormat(file);                
            }

            // Check if not null, and add to world
            if (shop != null) {
                if(Config.getSrvDebug()) {
                    log.info(String.format("[%s] Loaded %s", plugin.pdfFile.getName(), shop.toString()));
                }
                if (shop.isGlobal()) {
                    Config.globalShopsAdd(shop.getWorld(), shop.getUuid());
                } 
                plugin.getShopManager().addShop(shop);
            } else {
                log.warning(String.format("[%s] Failed to load Shop file: \"%s\"", plugin.pdfFile.getName(), file.getName()));
            }
        }

    }

    public Shop convertShopOldFormat(File file) {
        if(Config.getSrvDebug()) {
            log.info(String.format("[%s] %s.%s", plugin.pdfFile.getName(), "ShopData", "loadShopOldFormat(File file)"));
        }

        try {
            // Create new empty shop (this format has no UUID, so generate one)
            Shop shop = new Shop(UUID.randomUUID());

            // Retrieve Shop Name (from filename)
            shop.setName(file.getName().split("\\.")[0]);

            // Open file & iterate over lines
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                if(Config.getSrvDebug()) {
                    log.info(String.format("[%s] %s", plugin.pdfFile.getName(), line));
                }

                // Skip comment lines / metadata
                if (line.startsWith("#")) {
                    line = br.readLine();
                    continue;
                }

                // Data is separated by =
                String[] cols = line.split("=");

                // Check if there are enough columns (needs key and value)
                if (cols.length < 2) {
                    line = br.readLine();
                    continue;
                }

                if (cols[0].equalsIgnoreCase("world")) { // World
                    shop.setWorld(cols[1]);
                } else if (cols[0].equalsIgnoreCase("owner")) { // Owner
                    shop.setOwner(cols[1]);
                } else if (cols[0].equalsIgnoreCase("managers")) { // Managers
                    String[] managers = cols[1].split(",");
                    shop.setManagers(managers);
                } else if (cols[0].equalsIgnoreCase("creator")) { // Creator
                    shop.setCreator(cols[1]);
                } else if (cols[0].equalsIgnoreCase("position1")) { // Position
                    // A
                    String[] xyzStr = cols[1].split(",");
                    try {
                        int x = Integer.parseInt(xyzStr[0].trim());
                        int y = Integer.parseInt(xyzStr[1].trim());
                        int z = Integer.parseInt(xyzStr[2].trim());

                        ShopLocation loc = new ShopLocation(x, y, z);
                        shop.setLocationA(loc);
                    } catch (NumberFormatException e) {
                        if(isolateBrokenShopFile(file)) {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Location Data, Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        } else {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Location Data, Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        }
                        return null;
                    }
                } else if (cols[0].equalsIgnoreCase("position2")) { // Position
                    // B
                    String[] xyzStr = cols[1].split(",");
                    try {
                        int x = Integer.parseInt(xyzStr[0].trim());
                        int y = Integer.parseInt(xyzStr[1].trim());
                        int z = Integer.parseInt(xyzStr[2].trim());

                        ShopLocation loc = new ShopLocation(x, y, z);
                        shop.setLocationB(loc);
                    } catch (NumberFormatException e) {
                        if(isolateBrokenShopFile(file)) {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Location Data, Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        } else {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Location Data, Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        }
                        return null;
                    }
                } else if (cols[0].equalsIgnoreCase("unlimited-money")) { // Unlimited
                    // Money
                    shop.setUnlimitedMoney(Boolean.parseBoolean(cols[1]));
                } else if (cols[0].equalsIgnoreCase("unlimited-stock")) { // Unlimited
                    // Stock
                    shop.setUnlimitedStock(Boolean.parseBoolean(cols[1]));
                } else if (cols[0].matches("\\d+:\\d+")) { // Items
                    String[] itemInfo = cols[0].split(":");
                    if (itemInfo.length < 2) {
                        if(isolateBrokenShopFile(file)) {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        } else {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        }
                        return null;
                    }
                    int itemId = Integer.parseInt(itemInfo[0]);
                    short damageMod = Short.parseShort(itemInfo[1]);

                    String[] dataCols = cols[1].split(",");
                    if (dataCols.length < 3) {
                        if(isolateBrokenShopFile(file)) {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        } else {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        }
                        return null;
                    }

                    String[] buyInfo = dataCols[0].split(":");
                    if (buyInfo.length < 2) {
                        if(isolateBrokenShopFile(file)) {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        } else {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        }
                        return null;
                    }
                    int buyPrice = Integer.parseInt(buyInfo[0]);
                    int buySize = Integer.parseInt(buyInfo[1]);

                    String[] sellInfo = dataCols[1].split(":");
                    if (sellInfo.length < 2) {
                        if(isolateBrokenShopFile(file)) {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        } else {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        }
                        return null;
                    }
                    int sellPrice = Integer.parseInt(sellInfo[0]);
                    int sellSize = Integer.parseInt(sellInfo[1]);

                    String[] stockInfo = dataCols[2].split(":");
                    if (stockInfo.length < 2) {
                        if(isolateBrokenShopFile(file)) {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        } else {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data, Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                        }
                        return null;
                    }
                    int stock = Integer.parseInt(stockInfo[0]);
                    int maxStock = Integer.parseInt(stockInfo[1]);

                    if(!shop.addItem(itemId, damageMod, buyPrice, buySize, sellPrice, sellSize, stock, maxStock)) {
                        if(isolateBrokenShopFile(file)) {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data (%d:%d), Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString(), itemId, damageMod));
                        } else {
                            log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data (%d:%d), Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString(), itemId, damageMod));
                        }
                        return null;
                    }
                } else { // Not defined
                    log.info(String.format("[%s] Shop File \"%s\" has undefined data, ignoring.", plugin.pdfFile.getName(), file.toString()));
                }
                line = br.readLine();
            }

            br.close();

            File dir = new File(Config.getDirShopsConvertedPath());
            dir.mkdir();
            if (file.renameTo(new File(dir, file.getName()))) {
                file.delete();
                return shop;
            } else {
                return null;
            }

        } catch (IOException e) {
            if(isolateBrokenShopFile(file)) {
                log.warning(String.format("[%s] Shop File \"%s\" Exception: %s, Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString(), e.toString()));
            } else {
                log.warning(String.format("[%s] Shop File \"%s\" Exception: %s, Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString(), e.toString()));
            }
            return null;
        }
    }

    public static double[] convertStringArraytoDoubleArray(String[] sarray) {
        if (sarray != null) {
            double longArray[] = new double[sarray.length];
            for (int i = 0; i < sarray.length; i++) {
                longArray[i] = Long.parseLong(sarray[i]);
            }
            return longArray;
        }
        return null;
    }

    public static int[] convertStringArraytoIntArray(String[] sarray) {
        if (sarray != null) {
            int intArray[] = new int[sarray.length];
            for (int i = 0; i < sarray.length; i++) {
                intArray[i] = Integer.parseInt(sarray[i]);
            }
            return intArray;
        }
        return null;
    }

    public Shop loadShop(File file) throws Exception {
        HashMap<String, ShopSign> signMap = new HashMap<String, ShopSign>(4);

        Shop shop = null;
        int[] locationA = null;
        int[] locationB = null;

        SortedProperties props = new SortedProperties();
        try {
            props.load(new FileInputStream(file));
        } catch(IOException e) {
            log.warning(String.format("[%s] %s", plugin.pdfFile.getName(), "IOException: " + e.getMessage()));
            return null;
        }

        // Shop attributes
        UUID uuid = UUID.fromString(props.getProperty("uuid", "00000000-0000-0000-0000-000000000000"));
        String name = props.getProperty("name", "Nameless Shop");
        boolean unlimitedMoney = Boolean.parseBoolean(props.getProperty("unlimited-money", "false"));
        boolean unlimitedStock = Boolean.parseBoolean(props.getProperty("unlimited-stock", "false"));
        double minBalance = Double.parseDouble((props.getProperty("min-balance", "0.0")));
        boolean notification = Boolean.parseBoolean(props.getProperty("notification", "true"));
        boolean global = Boolean.parseBoolean(props.getProperty("global", "false"));
        
        if (!global) {
            // Location - locationB=-88, 50, -127
            try {
                locationA = convertStringArraytoIntArray(props.getProperty("locationA").split(", "));
                locationB = convertStringArraytoIntArray(props.getProperty("locationB").split(", "));

            } catch (Exception e) {
                if(isolateBrokenShopFile(file)) {
                    log.warning(String.format("[%s] Shop File \"%s\" has bad Location Data, Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                } else {
                    log.warning(String.format("[%s] Shop File \"%s\" has bad Location Data, Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString()));
                }
                return null;
            }
        }

        // People
        String owner = props.getProperty("owner", "");
        String[] managers = props.getProperty("managers", "").replaceAll("[\\[\\]]", "").split(", ");
        String creator = props.getProperty("creator", "LocalShops");
        String world = props.getProperty("world", "world1");

        shop = new Shop(uuid);
        shop.setName(name);
        shop.setUnlimitedMoney(unlimitedMoney);
        shop.setUnlimitedStock(unlimitedStock);
        shop.setOwner(owner);
        shop.setManagers(managers);
        shop.setCreator(creator);
        shop.setNotification(notification);
        shop.setWorld(world);
        if (!global) {
            shop.setLocationA(new ShopLocation(locationA));
            shop.setLocationB(new ShopLocation(locationB));
        } else {
            shop.setGlobal(true);
        }
        
        // Make sure minimum balance isn't negative
        if (minBalance < 0) {
            shop.setMinBalance(0);
        } else {
            shop.setMinBalance(minBalance);
        }

        // Iterate through all keys, find items & parse
        // props.setProperty(String.format("%d:%d", info.typeId, info.subTypeId), String.format("%d:%d,%d:%d,%d:%d", buyPrice, buySize, sellPrice, sellSize, stock, maxStock));
        Iterator<Object> it = props.keySet().iterator();
        while(it.hasNext()) {
            String key = (String) it.next();
            if(key.matches("\\d+:\\d+")) {
                String[] k = key.split(":");
                int id = Integer.parseInt(k[0]);
                short type = Short.parseShort(k[1]);

                String value = props.getProperty(key);
                String[] v = value.split(",");

                String[] buy = v[0].split(":");
                double buyPrice = Double.parseDouble(buy[0]);
                int buyStackSize = Integer.parseInt(buy[1]);

                String[] sell = v[1].split(":");
                double sellPrice = Double.parseDouble(sell[0]);
                int sellStackSize = Integer.parseInt(sell[1]);

                String[] stock = v[2].split(":");
                int currStock = Integer.parseInt(stock[0]);
                int maxStock = Integer.parseInt(stock[1]);

                if(!shop.addItem(id, type, buyPrice, buyStackSize, sellPrice, sellStackSize, currStock, maxStock)) {                   
                    if(isolateBrokenShopFile(file)) {
                        log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data (%d:%d), Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString(), id, type));
                    } else {
                        log.warning(String.format("[%s] Shop File \"%s\" has bad Item Data (%d:%d), Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.toString(), id, type));
                    }
                    return null;
                }
            } else if (key.matches("\\d+$")) {

                String values = props.getProperty(key);

                String[] v = values.split(":");
                String[] v2 = v[1].split(",");

                String signWorld = v[0];
                int x = Integer.parseInt(v2[0]);
                int y = Integer.parseInt(v2[1]);
                int z = Integer.parseInt(v2[2]);
                String itemName = v2[3];

                if (plugin.getServer().getWorld(signWorld) != null) {
                    ShopSign sign = new ShopSign(plugin.getServer().getWorld(signWorld), x, y, z, itemName);
                    signMap.put(sign.hashString(), sign);
                } else {
                    ShopSign sign = new ShopSign(signWorld, x, y, z, itemName);
                    signMap.put(sign.hashString(), sign);
                }
            }
        }

        //After loading sign data, verify they exist in the world
        Iterator<String> iter = signMap.keySet().iterator();
        while (iter.hasNext() ) {
            String signKey = iter.next();

            //Skip signs that can't be verified yet.
            if (signMap.get(signKey).getWorld() == null)
                continue;

            ShopSign sign = signMap.get(signKey);
            //Load the chunk so we don't try getting blocks that are non-existent
            sign.getWorld().loadChunk(sign.getWorld().getChunkAt(sign.getLoc()));

            //Check if the block is not a sign.
            if ( !sign.isValid() ) {
                iter.remove();
                continue;
            } else {
                ItemInfo item = Search.itemByName(sign.getItemName());
                if (!(shop.containsItem(item))) {
                    iter.remove();
                    continue;
                } else {
                    shop.updateSign(sign);
                }
            }
        }

        //Set the sign mapping for the shop
        shop.getSignMap().putAll(signMap);

        // Sanity Checks
        // Check that filename == UUID from file
        if(!file.getName().equalsIgnoreCase(String.format("%s.shop", shop.getUuid().toString()))) {
            shop = null;

            if(isolateBrokenShopFile(file)) {
                log.warning(String.format("[%s] Shop file %s has bad data!  Moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.getName()));
            } else {
                log.warning(String.format("[%s] Shop file %s has bad data!  Error moving to \"plugins/LocalShops/broken-shops/\"", plugin.pdfFile.getName(), file.getName()));
            }
        }

        return shop;
    }

    public boolean isolateBrokenShopFile(File file) {
        File dir = new File(Config.getDirShopsBrokenPath());
        dir.mkdir();
        if (file.renameTo(new File(dir, file.getName()))) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    public boolean saveAllShops() {
        log.info(String.format("[%s] %s", plugin.pdfFile.getName(), "Saving All Shops"));
        Iterator<Shop> it = shops.values().iterator();
        while(it.hasNext()) {
            Shop shop = it.next();
            saveShop(shop);
        }
        return true;
    }

    public boolean saveShop(Shop shop) {
        SortedProperties props = new SortedProperties();

        // Config attributes
        props.setProperty("config-version", "2.0");

        // Shop attributes
        props.setProperty("uuid", shop.getUuid().toString());
        props.setProperty("name", shop.getName());
        props.setProperty("unlimited-money", String.valueOf(shop.isUnlimitedMoney()));
        props.setProperty("unlimited-stock", String.valueOf(shop.isUnlimitedStock()));
        props.setProperty("min-balance", String.valueOf(shop.getMinBalance()));
        props.setProperty("notification", String.valueOf(shop.getNotification()));

        // Location
        if (!shop.isGlobal()) {
            props.setProperty("locationA", shop.getLocationA().toString());
            props.setProperty("locationB", shop.getLocationB().toString());
        } else {
            props.setProperty("global", "true");
        }

        props.setProperty("world", shop.getWorld());

        // People
        props.setProperty("owner", shop.getOwner());
        props.setProperty("managers", Search.join(shop.getManagers(), ", "));
        props.setProperty("creator", shop.getCreator());

        // Inventory
        for (InventoryItem item : shop.getItems()) {
            ItemInfo info = item.getInfo();
            double buyPrice = item.getBuyPrice();
            int buySize = item.getBuySize();
            double sellPrice = item.getSellPrice();
            int sellSize = item.getSellSize();
            int stock = item.getStock();
            int maxStock = item.getMaxStock();

            props.setProperty(String.format("%d:%d", info.typeId, info.subTypeId), String.format("%f:%d,%f:%d,%d:%d", buyPrice, buySize, sellPrice, sellSize, stock, maxStock));
        }

        //Sign Data
        for (String signId : shop.getSignMap().keySet()) {
            ShopSign sign = shop.getSignMap().get(signId);
            props.setProperty(signId, String.format("%s:%d,%d,%d,%s", sign.getWorldName(), sign.getX(), sign.getY(), sign.getZ(), sign.getItemName()));
        }


        String fileName = Config.getDirShopsActivePath() + shop.getUuid().toString() + ".shop";
        try {
            props.store(new FileOutputStream(fileName), "LocalShops Config Version 2.0");
        } catch (IOException e) {
            log.warning("IOException: " + e.getMessage());
        }

        return true;
    }

    public boolean deleteShop(Shop shop) {
        String shortUuid = shop.getShortUuidString();

        if (shop.isGlobal()) {
            if (Config.globalShopsContainsKey(shop.getWorld())) {
                /**
                Config.GLOBAL_SHOPS.remove(shop.getWorld());
                LocalShops.getProperties().removeKey(shop.getWorld() + "-shop-UUID");
                LocalShops.getProperties().save();
                 */
                return true;
            } else {
                return false;
            }
        }

        // remove string from uuid short list
        Config.removeUuidList(shortUuid);

        // delete the file from the directory
        String filePath = Config.getDirShopsActivePath() + shop.getUuid() + ".shop";
        File shopFile = new File(filePath);
        shopFile.delete();

        // remove shop from data structure
        shops.remove(shop.getUuid());

        return true;
    }

    public boolean logItems(String playerName, String shopName, String action, String itemName, int numberOfItems, int startNumberOfItems, int endNumberOfItems) {

        return logTransaciton(playerName, shopName, action, itemName, numberOfItems, startNumberOfItems, endNumberOfItems, 0, 0, 0);

    }

    public boolean logPayment(String playerName, String action, double moneyTransfered, double startingbalance, double endingbalance) {

        return logTransaciton(playerName, null, action, null, 0, 0, 0, moneyTransfered, startingbalance, endingbalance);
    }

    public boolean logTransaciton(String playerName, String shopName, String action, String itemName, int numberOfItems, int startNumberOfItems, int endNumberOfItems, double moneyTransfered, double startingbalance, double endingbalance) {
        if (!Config.getSrvLogTransactions())
            return false;

        String filePath = Config.getFileTransactionLog();

        File logFile = new File(filePath);
        try {

            logFile.createNewFile();

            String fileOutput = "";

            DateFormat dateFormat = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss z");
            Date date = new Date();
            fileOutput += dateFormat.format(date) + ": ";
            fileOutput += "Action: ";
            if (action != null)
                fileOutput += action;
            fileOutput += ": ";
            fileOutput += "Player: ";
            if (playerName != null)
                fileOutput += playerName;
            fileOutput += ": ";
            fileOutput += "Shop: ";
            if (shopName != null)
                fileOutput += shopName;
            fileOutput += ": ";
            fileOutput += "Item Type: ";
            if (itemName != null)
                fileOutput += itemName;
            fileOutput += ": ";
            fileOutput += "Number Transfered: ";
            fileOutput += numberOfItems;
            fileOutput += ": ";
            fileOutput += "Stating Stock: ";
            fileOutput += startNumberOfItems;
            fileOutput += ": ";
            fileOutput += "Ending Stock: ";
            fileOutput += endNumberOfItems;
            fileOutput += ": ";
            fileOutput += "Money Transfered: ";
            fileOutput += moneyTransfered;
            fileOutput += ": ";
            fileOutput += "Starting balance: ";
            fileOutput += startingbalance;
            fileOutput += ": ";
            fileOutput += "Ending balance: ";
            fileOutput += endingbalance;
            fileOutput += ": ";
            fileOutput += "\n";

            FileOutputStream logFileOut = new FileOutputStream(logFile, true);
            logFileOut.write(fileOutput.getBytes());
            logFileOut.close();

        } catch (IOException e1) {
            System.out.println(plugin.pdfFile.getName() + ": Error - Could not write to file " + logFile.getName());
            return false;
        }

        return true;
    }

    /**
     * @return null
     */
    public Callable<Object> updateSigns() {
        for (UUID key : shops.keySet()) {
            Shop shop = shops.get(key);
            if (shop.isDynamicPrices())
                for (String signKey : shop.getSignMap().keySet())
                    shop.updateSign(shop.getSignMap().get(signKey));
        }
        return null;
    }


}
