package com.milkbukkit.localshops;

import java.util.ArrayList;
import java.util.List;

public class Search { 
    
    private static ArrayList<ItemInfo> items = new ArrayList<ItemInfo>();
    static {
     // Stack modes (the three values at the end of each item) are in the following order:
     // Vanilla, Enhanced, Unlimited
     // Vanilla = Conforms to Notch's default stack sizes.
     // Enhanced = Modified stack sizes in favour of the player.
     // Unlimited = Unlimited (64) stack sizes for all items that wouldn't glitch as a result of it.
        items.add(new ItemInfo("Stone", new String[][] { { "stone" } }, 1, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Grass", new String[][] { { "gras" } }, 2, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Dirt", new String[][] { { "dirt" } }, 3, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cobblestone", new String[][] { { "cobb", "sto" }, { "cobb" } }, 4, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Wooden Plank", new String[][] { { "wood" }, { "wood", "plank" } }, 5, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Sapling", new String[][] { { "sapling" } }, 6, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Redwood Sapling", new String[][] { { "sapling", "red" } }, 6, (short) 1, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Birch Sapling", new String[][] { { "sapling", "birch" } }, 6, (short) 2, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Bedrock", new String[][] { { "rock" } }, 7, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Water", new String[][] { { "water" } }, 8, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Lava", new String[][] { { "lava" } }, 10, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Sand", new String[][] { { "sand" } }, 12, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Gravel", new String[][] { { "gravel" } }, 13, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Gold Ore", new String[][] { { "ore", "gold" } }, 14, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Iron Ore", new String[][] { { "ore", "iron" } }, 15, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Coal Ore", new String[][] { { "ore", "coal" } }, 16, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Log", new String[][] { { "log" } }, 17, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Redwood Log", new String[][] { { "log", "red" }, { "red", "wood" } }, 17, (short) 1, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Birch Log", new String[][] { { "birch" }, { "log", "birch" } }, 17, (short) 2, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Leaves", new String[][] { { "leaf" }, { "leaves" } }, 18, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Redwood Leaves", new String[][] { { "lea", "red" } }, 18, (short) 1, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Birch Leaves", new String[][] { { "lea", "birch" } }, 18, (short) 2, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Sponge", new String[][] { { "sponge" } }, 19, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Glass", new String[][] { { "glas" }, { "sili" } }, 20, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Lapis Lazuli Ore", new String[][] { { "laz", "ore" } }, 21, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Lapis Lazuli Block", new String[][] { { "laz", "bl" } }, 22, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Dispenser", new String[][] { { "dispen" } }, 23, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Sandstone", new String[][] { { "sand", "st" } }, 24, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Note Block", new String[][] { { "note" } }, 25, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Powered Rail", new String[][] { { "rail", "pow" }, { "trac", "pow" }, { "boost" } }, 27, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Detector Rail", new String[][] { { "rail", "det" }, { "trac", "det" }, { "detec" } }, 28, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Web", new String[][] { { "web" } }, 30, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("White Wool", new String[][] { { "wool", "whit" }, { "wool" } }, 35, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Orange Wool", new String[][] { { "wool", "ora" } }, 35, (short) 1, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Magenta Wool", new String[][] { { "wool", "mag" } }, 35, (short) 2, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Light Blue Wool", new String[][] { { "wool", "lig", "blue" } }, 35, (short) 3, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Yellow Wool", new String[][] { { "wool", "yell" } }, 35, (short) 4, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Light Green Wool", new String[][] { { "wool", "lig", "gree" }, { "wool", "gree" } }, 35, (short) 5, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Pink Wool", new String[][] { { "wool", "pink" } }, 35, (short) 6, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Gray Wool", new String[][] { { "wool", "gray" }, { "wool", "grey" } }, 35, (short) 7, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Light Gray Wool", new String[][] { { "lig", "wool", "gra" }, { "lig", "wool", "gre" } }, 35, (short) 8, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cyan Wool", new String[][] { { "wool", "cya" } }, 35, (short) 9, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Purple Wool", new String[][] { { "wool", "pur" } }, 35, (short) 10, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Blue Wool", new String[][] { { "wool", "blue" } }, 35, (short) 11, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Brown Wool", new String[][] { { "wool", "brow" } }, 35, (short) 12, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Dark Green Wool", new String[][] { { "wool", "dar", "gree" }, { "wool", "gree" } }, 35, (short) 13, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Red Wool", new String[][] { { "wool", "red" } }, 35, (short) 14, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Black Wool", new String[][] { { "wool", "bla" } }, 35, (short) 15, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Dandelion", new String[][] { { "flow", "yell" }, { "dande" } }, 37, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Red Rose", new String[][] { { "flow", "red" }, { "rose" } }, 38, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Brown Mushroom", new String[][] { { "mush", "bro" } }, 39, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Red Mushroom", new String[][] { { "mush", "red" } }, 40, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Gold Block", new String[][] { { "gold", "bl" } }, 41, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Iron Block", new String[][] { { "iron", "bl" } }, 42, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Stone Slab", new String[][] { { "slab" }, { "slab", "sto" }, { "step", "sto" } }, 44, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Sandstone Slab", new String[][] { { "slab", "sand", "sto" }, { "step", "sand", "sto" } }, 44, (short) 1, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Wooden Slab", new String[][] { { "slab", "woo" }, { "step", "woo" } }, 44, (short) 2, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cobblestone Slab", new String[][] { { "slab", "cob", "sto" }, { "slab", "cob" } }, 44, (short) 3, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Brick", new String[][] { { "bric" } }, 45, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("TNT", new String[][] { { "tnt" }, { "boom" } }, 46, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Bookshelf", new String[][] { { "bookshe" }, { "book", "she" } }, 47, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Moss Stone", new String[][] { { "moss", "sto" }, { "moss" } }, 48, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Obsidian", new String[][] { { "obsi" } }, 49, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Torch", new String[][] { { "torc" } }, 50, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Fire", new String[][] { { "fire" } }, 51, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Monster Spawner", new String[][] { { "spawn" } }, 52, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Wooden Stairs", new String[][] { { "stair", "wood" } }, 53, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Chest", new String[][] { { "chest" } }, 54, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Diamond Ore", new String[][] { { "ore", "diam" } }, 56, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Diamond Block", new String[][] { { "diam", "bl" } }, 57, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Crafting Table", new String[][] { { "benc" }, { "squa" }, { "craft" } }, 58, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Farmland", new String[][] { { "soil" }, { "farm" } }, 60, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Furnace", new String[][] { { "furna" }, { "cooke" } }, 61, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Ladder", new String[][] { { "ladd" } }, 65, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Rails", new String[][] { { "rail" }, { "trac" } }, 66, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cobblestone Stairs", new String[][] { { "stair", "cob", "sto" }, { "stair", "cob" } }, 67, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Lever", new String[][] { { "lever" }, { "switc" } }, 69, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Stone Pressure Plate", new String[][] { { "pres", "plat", "ston" } }, 70, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Wooden Pressure Plate", new String[][] { { "pres", "plat", "wood" } }, 72, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Redstone Ore", new String[][] { { "ore", "red" }, { "ore", "rs" } }, 73, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Redstone Torch", new String[][] { { "torc", "red" }, { "torc", "rs" } }, 76, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Stone Button", new String[][] { { "stone", "button" }, { "button" } }, 77, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Ice", new String[][] { { "ice" } }, 79, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Snow Block", new String[][] { { "snow" } }, 80, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cactus", new String[][] { { "cact" } }, 81, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Clay Block", new String[][] { { "clay", "blo" } }, 82, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Jukebox", new String[][] { { "jukeb" } }, 84, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Fence", new String[][] { { "fence" } }, 85, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Pumpkin", new String[][] { { "pump" } }, 86, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Netherrack", new String[][] { { "netherr" }, { "netherst" }, { "hellst" } }, 87, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Soul Sand", new String[][] { { "soul", "sand" }, { "soul" }, { "slowsa" }, { "nether", "mud" }, { "slow", "sand" }, { "quick", "sand" }, { "mud" } }, 88, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Glowstone", new String[][] { { "glow", "stone" }, { "light", "stone" } }, 89, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Jack-O-Lantern", new String[][] { { "jack" }, { "lante" } }, 91, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Iron Shovel", new String[][] { { "shov", "ir" }, { "spad", "ir" } }, 256, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Iron Pickaxe", new String[][] { { "pick", "ir" } }, 257, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Iron Axe", new String[][] { { "axe", "ir" } }, 258, (short) 0, new int[]{ 0, 0, 0 } ));
        items.add(new ItemInfo("Flint and Steel", new String[][] { { "steel" }, { "lighter" }, { "flin", "ste" } }, 259, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Apple", new String[][] { { "appl" } }, 260, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Bow", new String[][] { { "bow" } }, 261, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Arrow", new String[][] { { "arro" } }, 262, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Coal", new String[][] { { "coal" } }, 263, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Charcoal", new String[][] { { "char", "coal" }, { "char" } }, 263, (short) 1, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Diamond", new String[][] { { "diamo" } }, 264, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Iron Ingot", new String[][] { { "ingo", "ir" }, { "iron" } }, 265, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Gold Ingot", new String[][] { { "ingo", "go" }, { "gold" } }, 266, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Iron Sword", new String[][] { { "swor", "ir" } }, 267, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Wooden Sword", new String[][] { { "swor", "woo" } }, 268, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Wooden Shovel", new String[][] { { "shov", "wo" }, { "spad", "wo" } }, 269, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Wooden Pickaxe", new String[][] { { "pick", "woo" } }, 270, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Wooden Axe", new String[][] { { "axe", "woo" } }, 271, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Stone Sword", new String[][] { { "swor", "sto" } }, 272, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Stone Shovel", new String[][] { { "shov", "sto" }, { "spad", "sto" } }, 273, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Stone Pickaxe", new String[][] { { "pick", "sto" } }, 274, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Stone Axe", new String[][] { { "axe", "sto" } }, 275, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Diamond Sword", new String[][] { { "swor", "dia" } }, 276, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Diamond Shovel", new String[][] { { "shov", "dia" }, { "spad", "dia" } }, 277, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Diamond Pickaxe", new String[][] { { "pick", "dia" } }, 278, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Diamond Axe", new String[][] { { "axe", "dia" } }, 279, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Stick", new String[][] { { "stic" } }, 280, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Bowl", new String[][] { { "bo", "wl" } }, 281, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Mushroom Soup", new String[][] { { "soup" } }, 282, (short) 0, new int[]{ 1, 1, 1 } ));
        items.add(new ItemInfo("Gold Sword", new String[][] { { "swor", "gol" } }, 283, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Gold Shovel", new String[][] { { "shov", "gol" }, { "spad", "gol" } }, 284, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Gold Pickaxe", new String[][] { { "pick", "gol" } }, 285, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Gold Axe", new String[][] { { "axe", "gol" } }, 286, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("String", new String[][] { { "stri" } }, 287, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Feather", new String[][] { { "feat" } }, 288, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Gunpowder", new String[][] { { "gun" }, { "sulph" } }, 289, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Wooden Hoe", new String[][] { { "hoe", "wo" } }, 290, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Stone Hoe", new String[][] { { "hoe", "sto" } }, 291, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Iron Hoe", new String[][] { { "hoe", "iro" } }, 292, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Diamond Hoe", new String[][] { { "hoe", "dia" } }, 293, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Gold Hoe", new String[][] { { "hoe", "go" } }, 294, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Seeds", new String[][] { { "seed" } }, 295, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Wheat", new String[][] { { "whea" } }, 296, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Bread", new String[][] { { "brea" } }, 297, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Leather Cap", new String[][] { { "cap", "lea" }, { "hat", "lea" }, { "helm", "lea" } }, 298, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Leather Tunic", new String[][] { { "tun", "lea" }, { "ches", "lea" } }, 299, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Leather Pants", new String[][] { { "pan", "lea" }, { "trou", "lea" }, { "leg", "lea" } }, 300, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Leather Boots", new String[][] { { "boo", "lea" } }, 301, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Chainmail Helmet", new String[][] { { "cap", "cha" }, { "hat", "cha" }, { "helm", "cha" } }, 302, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Chainmail Chestplate", new String[][] { { "tun", "cha" }, { "ches", "cha" } }, 303, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Chainmail Leggings", new String[][] { { "pan", "cha" }, { "trou", "cha" }, { "leg", "cha" } }, 304, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Chainmail Boots", new String[][] { { "boo", "cha" } }, 305, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Iron Helmet", new String[][] { { "cap", "ir" }, { "hat", "ir" }, { "helm", "ir" } }, 306, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Iron Chestplate", new String[][] { { "tun", "ir" }, { "ches", "ir" } }, 307, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Iron Leggings", new String[][] { { "pan", "ir" }, { "trou", "ir" }, { "leg", "ir" } }, 308, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Iron Boots", new String[][] { { "boo", "ir" } }, 309, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Diamond Helmet", new String[][] { { "cap", "dia" }, { "hat", "dia" }, { "helm", "dia" } }, 310, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Diamond Chestplate", new String[][] { { "tun", "dia" }, { "ches", "dia" } }, 311, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Diamond Leggings", new String[][] { { "pan", "dia" }, { "trou", "dia" }, { "leg", "dia" } }, 312, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Diamond Boots", new String[][] { { "boo", "dia" } }, 313, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Gold Helmet", new String[][] { { "cap", "go" }, { "hat", "go" }, { "helm", "go" } }, 314, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Gold Chestplate", new String[][] { { "tun", "go" }, { "ches", "go" } }, 315, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Gold Leggings", new String[][] { { "pan", "go" }, { "trou", "go" }, { "leg", "go" } }, 316, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Gold Boots", new String[][] { { "boo", "go" } }, 317, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Flint", new String[][] { { "flin" } }, 318, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Raw Porkchop", new String[][] { { "pork" }, { "ham" } }, 319, (short) 0, new int[]{ 1, 8, 64 } ));
        items.add(new ItemInfo("Cooked Porkchop", new String[][] { { "pork", "cook" }, { "baco" } }, 320, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Paintings", new String[][] { { "paint" } }, 321, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Golden Apple", new String[][] { { "appl", "go" } }, 322, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Sign", new String[][] { { "sign" } }, 323, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Wooden Door", new String[][] {  { "door", "wood" }, { "door" } }, 324, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Bucket", new String[][] { { "buck" }, { "bukk" } }, 325, (short) 0, new int[]{ 1, 1, 1 } ));
        items.add(new ItemInfo("Water Bucket", new String[][] { { "water", "buck" } }, 326, (short) 0, new int[]{ 1, 1, 1 } ));
        items.add(new ItemInfo("Lava Bucket", new String[][] { { "lava", "buck" } }, 327, (short) 0, new int[]{ 1, 1, 1 } ));
        items.add(new ItemInfo("Minecart", new String[][] { { "cart" } }, 328, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Saddle", new String[][] { { "sad" }, { "pig" } }, 329, (short) 0, new int[]{ 1, 1, 1 } ));
        items.add(new ItemInfo("Iron Door", new String[][] { { "door", "iron" } }, 330, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Redstone Dust", new String[][] { { "red", "ston", "dust" }, {"red", "ston" }, { "dust", "rs" }, { "dust", "red" }, { "reds" } }, 331, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Snowball", new String[][] { { "snow", "ball" } }, 332, (short) 0, new int[]{ 16, 64, 64 } ));
        items.add(new ItemInfo("Boat", new String[][] { { "boat" } }, 333, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Leather", new String[][] { { "lea" }, { "hide" } }, 334, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Milk Bucket", new String[][] { { "milk" } }, 335, (short) 0, new int[]{ 1, 1, 1 } ));
        items.add(new ItemInfo("Clay Brick", new String[][] { { "bric", "cl" }, { "sin", "bric" } }, 336, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Clay", new String[][] { { "cla" } }, 337, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Sugar Cane", new String[][] { { "reed" }, { "cane" } }, 338, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Paper", new String[][] { { "pape" } }, 339, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Book", new String[][] { { "book" } }, 340, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Slimeball", new String[][] { { "slime" } }, 341, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Storage Minecart", new String[][] { { "cart", "sto" }, { "cart", "che" }, { "cargo" } }, 342, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Powered Minecart", new String[][] { { "cart", "pow" }, { "engine" } }, 343, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Egg", new String[][] { { "egg" } }, 344, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Compass", new String[][] { { "comp" } }, 345, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Fishing Rod", new String[][] { { "rod" }, { "pole" } }, 346, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Clock", new String[][] { { "cloc" }, { "watc" } }, 347, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Glowstone Dust", new String[][] { { "glow", "sto", "dus" }, { "glow", "dus" }, { "ligh", "dust" } }, 348, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Raw Fish", new String[][] { { "fish" } }, 349, (short) 0, new int[]{ 1, 8, 64 } ));
        items.add(new ItemInfo("Cooked Fish", new String[][] { { "fish", "coo" }, { "kipper" } }, 350, (short) 0, new int[]{ 1, 4, 64 } ));
        items.add(new ItemInfo("Ink Sac", new String[][] { { "ink" }, { "dye", "bla" } }, 351, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Red Dye", new String[][] { { "dye", "red" }, { "pain", "red" }, { "pet", "ros" }, { "pet", "red" } }, 351, (short) 1, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cactus Green", new String[][] { { "cact", "gree" }, { "dye", "gree" }, { "pain", "gree" } }, 351, (short) 2, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cocoa Beans", new String[][] { { "bean" }, { "choco" }, { "cocoa" }, { "dye", "bro" }, { "pain", "bro" } }, 351, (short) 3, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Lapis Lazuli", new String[][] { { "lapi" }, { "dye", "blu" }, { "pain", "blu" } }, 351, (short) 4, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Purple Dye", new String[][] { { "dye", "pur" }, { "pain", "pur" } }, 351, (short) 5, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cyan Dye", new String[][] { { "dye", "cya" }, { "pain", "cya" } }, 351, (short) 6, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Light Gray Dye", new String[][] { { "dye", "lig", "gra" }, { "dye", "lig", "grey" }, { "pain", "lig", "grey" }, { "pain", "lig", "grey" } }, 351, (short) 7, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Gray Dye", new String[][] { { "dye", "gra" }, { "dye", "grey" }, { "pain", "grey" }, { "pain", "grey" } }, 351, (short) 8, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Pink Dye", new String[][] { { "dye", "pin" }, { "pain", "pin" } }, 351, (short) 9, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Lime Dye", new String[][] { { "dye", "lim" }, { "pain", "lim" }, { "dye", "lig", "gree" }, { "pain", "lig", "gree" } }, 351, (short) 10, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Dandelion Yellow", new String[][] { { "dye", "yel" }, { "yel", "dan" }, { "pet", "dan" }, { "pet", "yel" } }, 351, (short) 11, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Light Blue Dye", new String[][] { { "dye", "lig", "blu" }, { "pain", "lig", "blu" } }, 351, (short) 12, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Magenta Dye", new String[][] { { "dye", "mag" }, { "pain", "mag" } }, 351, (short) 13, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Orange Dye", new String[][] { { "dye", "ora" }, { "pain", "ora" } }, 351, (short) 14, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Bone Meal", new String[][] { { "bonem" }, { "bone", "me" }, { "dye", "whi" }, { "pain", "whi" } }, 351, (short) 15, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Bone", new String[][] { { "bone" }, { "femur" } }, 352, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Sugar", new String[][] { { "suga" } }, 353, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cake", new String[][] { { "cake" } }, 354, (short) 0, new int[]{ 1, 2, 64 } ));
        items.add(new ItemInfo("Bed", new String[][] { { "bed" } }, 355, (short) 0, new int[]{ 1, 16, 64 } ));
        items.add(new ItemInfo("Redstone Repeater", new String[][] { { "diod" }, { "red", "rep" }, { "rs", "rep" } }, 356, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Cookie", new String[][] { { "cooki" } }, 357, (short) 0, new int[]{ 8, 16, 64 } ));
        items.add(new ItemInfo("Gold Music Disc", new String[][] { { "dis", "gol" }, { "rec", "gol" } }, 2256, (short) 0, new int[]{ 64, 64, 64 } ));
        items.add(new ItemInfo("Green Music Disc", new String[][] { { "dis", "gre" }, { "rec", "gre" } }, 2257, (short) 0, new int[]{ 64, 64, 64 } ));
    }
    
    public static String join(String[] array, String glue) {
        String joined = null;
        for (String element : array) {
            if (joined == null) {
                joined = element;
            } else {
                joined += glue + element;
            }
        }
        
        if(joined == null) {
            return "";
        } else {
            return joined;
        }
    }    

    public static String join(List<String> list, String glue) {
        String joined = null;
        for (String element : list) {
            if (joined == null) {
                joined = element;
            } else {
                joined += glue + element;
            }
        }
        
        if(joined == null) {
            return "";
        } else {
            return joined;
        }
    }
    
    public static ItemInfo itemById(int type) {
        return itemById(type, (short) 0);
    }
    
    public static ItemInfo itemById(int type, short subType) {
        for(ItemInfo item : items) {
            if(item.typeId == type && item.subTypeId == subType) {
                return item;
            }
        }
        return null;
    }
    
    public static ItemInfo itemByName(ArrayList<String> search) {
        String searchString = join(search, " ");
        return itemByName(searchString);
    }

    public static ItemInfo itemByName(String searchString) {
        ItemInfo matchedItem = null;
        int matchedItemStrength = 0;

        if (searchString.matches("\\d+:\\d+")) {
            // Match on integer:short to get typeId and subTypeId
            
            // Retrieve/parse data
            String[] params = searchString.split(":");
            int typeId = Integer.parseInt(params[0]);
            short subTypeId = Short.parseShort(params[1]);
            
            // Iterate through Items
            for (ItemInfo item : items) {
                // Test for match
                if (item.typeId == typeId && item.subTypeId == subTypeId) {
                    matchedItem = item;
                    break;
                }
            }
        } else if (searchString.matches("\\d+")) {
            // Match an integer only, assume subTypeId = 0
            
            // Retrieve/parse data
            int typeId = Integer.parseInt(searchString);
            short subTypeId = 0;
            
            // Iterate through Items
            for (ItemInfo item : items) {
                // Test for match
                if (item.typeId == typeId && item.subTypeId == subTypeId) {
                    matchedItem = item;
                    break;
                }
            }
        } else {
            // Else this must be a string that we need to identify
            
            // Iterate through Items
            for (ItemInfo item : items) {
                // Look through each possible match criteria
                for (String[] attributes : item.search) {
                    boolean match = false;
                    // Loop through entire criteria strings
                    for (String attribute : attributes) {
                        if (searchString.toLowerCase().contains(attribute)) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    }
                    
                    // THIS was a match
                    if (match) {
                        if (matchedItem == null || attributes.length > matchedItemStrength) {
                            matchedItem = item;
                            matchedItemStrength = attributes.length;
                        }
                        
                        // This criteria was a match, lets break out of this item...no point testing alternate criteria's
                        break;
                    }                    
                }
            }
        }

        return matchedItem;
    }
}