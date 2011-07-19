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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.milkbowl.localshops.objects.Item;
import net.milkbowl.localshops.objects.ItemInfo;
import net.milkbowl.localshops.util.GenericFunctions;


public class Search { 

	private static final List<ItemInfo> items = Collections.synchronizedList(new ArrayList<ItemInfo>());
	static {
		// Stack modes (the three values at the end of each item) are in the following order:
		// Vanilla, Enhanced, Unlimited
		// Vanilla = Conforms to Notch's default stack sizes.
		// Enhanced = Modified stack sizes in favour of the player.
		// Unlimited = Unlimited (64) stack sizes for all items that wouldn't glitch as a result of it.
		items.add(new ItemInfo("Stone", new String[][] { { "stone" } }, Material.STONE));
		items.add(new ItemInfo("Grass", new String[][] { { "gras" } }, Material.GRASS));
		items.add(new ItemInfo("Dirt", new String[][] { { "dirt" } }, Material.DIRT));
		items.add(new ItemInfo("Cobblestone", new String[][] { { "cobb", "sto" }, { "cobb" } }, Material.COBBLESTONE));
		items.add(new ItemInfo("Wooden Plank", new String[][] { { "wood" }, { "wood", "plank" } }, Material.WOOD));
		items.add(new ItemInfo("Sapling", new String[][] { { "sapling" } }, Material.SAPLING));
		items.add(new ItemInfo("Redwood Sapling", new String[][] { { "sapling", "red" } }, Material.SAPLING, (short) 1));
		items.add(new ItemInfo("Birch Sapling", new String[][] { { "sapling", "birch" } }, Material.SAPLING, (short) 2));
		items.add(new ItemInfo("Bedrock", new String[][] { { "rock" } }, Material.BEDROCK));
		items.add(new ItemInfo("Water", new String[][] { { "water" } }, Material.WATER));
		items.add(new ItemInfo("Lava", new String[][] { { "lava" } },Material.LAVA));
		items.add(new ItemInfo("Sand", new String[][] { { "sand" } }, Material.SAND));
		items.add(new ItemInfo("Gravel", new String[][] { { "gravel" } }, Material.GRAVEL));
		items.add(new ItemInfo("Gold Ore", new String[][] { { "ore", "gold" } }, Material.GOLD_ORE));
		items.add(new ItemInfo("Iron Ore", new String[][] { { "ore", "iron" } }, Material.IRON_ORE));
		items.add(new ItemInfo("Coal Ore", new String[][] { { "ore", "coal" } }, Material.COAL_ORE));
		items.add(new ItemInfo("Log", new String[][] { { "log" } }, Material.LOG));
		items.add(new ItemInfo("Redwood Log", new String[][] { { "log", "red" }, { "red", "wood" } }, Material.LOG, (short) 1));
		items.add(new ItemInfo("Birch Log", new String[][] { { "birch" }, { "log", "birch" } }, Material.LOG, (short) 2));
		items.add(new ItemInfo("Leaves", new String[][] { { "leaf" }, { "leaves" } }, Material.LEAVES));
		items.add(new ItemInfo("Redwood Leaves", new String[][] { { "lea", "red" } }, Material.LEAVES, (short) 1));
		items.add(new ItemInfo("Birch Leaves", new String[][] { { "lea", "birch" } }, Material.LEAVES, (short) 2));
		items.add(new ItemInfo("Sponge", new String[][] { { "sponge" } }, Material.SPONGE));
		items.add(new ItemInfo("Glass", new String[][] { { "glas" }, { "sili" } }, Material.GLASS));
		items.add(new ItemInfo("Lapis Lazuli Ore", new String[][] { { "laz", "ore" }, { "ore", "lapi" } }, Material.LAPIS_ORE));
		items.add(new ItemInfo("Lapis Lazuli Block", new String[][] { { "laz", "bl" }, { "blo", "lapi"} }, Material.LAPIS_BLOCK));
		items.add(new ItemInfo("Dispenser", new String[][] { { "dispen" } }, Material.DISPENSER));
		items.add(new ItemInfo("Sandstone", new String[][] { { "sand", "st" } }, Material.SANDSTONE));
		items.add(new ItemInfo("Note Block", new String[][] { { "note" } }, Material.NOTE_BLOCK));
		items.add(new ItemInfo("Bed Block", new String[][] { { "block", "bed" } }, Material.BED_BLOCK));
		items.add(new ItemInfo("Powered Rail", new String[][] { { "rail", "pow" }, { "trac", "pow" }, { "boost" } }, Material.POWERED_RAIL));
		items.add(new ItemInfo("Detector Rail", new String[][] { { "rail", "det" }, { "trac", "det" }, { "detec" } }, Material.DETECTOR_RAIL));
		items.add(new ItemInfo("Sticky Piston", new String[][] { {"stic", "pis"} }, Material.PISTON_STICKY_BASE));
		items.add(new ItemInfo("Web", new String[][] { { "web" } }, Material.WEB));
		items.add(new ItemInfo("Piston", new String[][] { {"pist"} }, Material.PISTON_BASE));
		items.add(new ItemInfo("White Wool", new String[][] { { "wool", "whit" }, { "wool" } }, Material.WOOL));
		items.add(new ItemInfo("Orange Wool", new String[][] { { "wool", "ora" } }, Material.WOOL, (short) 1));
		items.add(new ItemInfo("Magenta Wool", new String[][] { { "wool", "mag" } }, Material.WOOL, (short) 2));
		items.add(new ItemInfo("Light Blue Wool", new String[][] { { "wool", "lig", "blue" } }, Material.WOOL, (short) 3));
		items.add(new ItemInfo("Yellow Wool", new String[][] { { "wool", "yell" } }, Material.WOOL, (short) 4));
		items.add(new ItemInfo("Light Green Wool", new String[][] { { "wool", "lig", "gree" }, { "wool", "gree" } }, Material.WOOL, (short) 5));
		items.add(new ItemInfo("Pink Wool", new String[][] { { "wool", "pink" } }, Material.WOOL, (short) 6));
		items.add(new ItemInfo("Gray Wool", new String[][] { { "wool", "gray" }, { "wool", "grey" } }, Material.WOOL, (short) 7));
		items.add(new ItemInfo("Light Gray Wool", new String[][] { { "lig", "wool", "gra" }, { "lig", "wool", "gre" } }, Material.WOOL, (short) 8));
		items.add(new ItemInfo("Cyan Wool", new String[][] { { "wool", "cya" } }, Material.WOOL, (short) 9));
		items.add(new ItemInfo("Purple Wool", new String[][] { { "wool", "pur" } }, Material.WOOL, (short) 10));
		items.add(new ItemInfo("Blue Wool", new String[][] { { "wool", "blue" } }, Material.WOOL, (short) 11));
		items.add(new ItemInfo("Brown Wool", new String[][] { { "wool", "brow" } }, Material.WOOL, (short) 12));
		items.add(new ItemInfo("Dark Green Wool", new String[][] { { "wool", "dar", "gree" }, { "wool", "gree" } }, Material.WOOL, (short) 13));
		items.add(new ItemInfo("Red Wool", new String[][] { { "wool", "red" } }, Material.WOOL, (short) 14));
		items.add(new ItemInfo("Black Wool", new String[][] { { "wool", "bla" } }, Material.WOOL, (short) 15));
		items.add(new ItemInfo("Dandelion", new String[][] { { "flow", "yell" }, { "dande" } }, Material.YELLOW_FLOWER));
		items.add(new ItemInfo("Red Rose", new String[][] { { "flow", "red" }, { "rose" } }, Material.RED_ROSE));
		items.add(new ItemInfo("Brown Mushroom", new String[][] { { "mush", "bro" } }, Material.BROWN_MUSHROOM));
		items.add(new ItemInfo("Red Mushroom", new String[][] { { "mush", "red" } }, Material.RED_MUSHROOM));
		items.add(new ItemInfo("Gold Block", new String[][] { { "gold", "bl" } }, Material.GOLD_BLOCK));
		items.add(new ItemInfo("Iron Block", new String[][] { { "iron", "bl" } }, Material.IRON_BLOCK));
		items.add(new ItemInfo("Double Stone Slab", new String[][] { { "doub", "slab" }, { "doub", "slab", "sto" }, { "doub", "step", "sto" } }, Material.DOUBLE_STEP));
		items.add(new ItemInfo("Double Sandstone Slab", new String[][] { { "doub", "slab", "sand", "sto" }, { "doub", "step", "sand", "sto" } }, Material.DOUBLE_STEP, (short) 1));
		items.add(new ItemInfo("Double Wooden Slab", new String[][] { { "doub", "slab", "woo" }, { "doub", "step", "woo" } }, Material.DOUBLE_STEP, (short) 2));
		items.add(new ItemInfo("Double Cobblestone Slab", new String[][] { { "doub", "slab", "cob", "sto" }, { "doub", "slab", "cob" }, { "doub", "step", "cob" } }, Material.DOUBLE_STEP, (short) 3));
		items.add(new ItemInfo("Stone Slab", new String[][] { { "slab", "sto"}, { "slab" }, { "step", "ston" } }, Material.STEP));
		items.add(new ItemInfo("Sandstone Slab", new String[][] { { "slab", "sand", "sto" }, { "step", "sand", "sto" } }, Material.STEP, (short) 1));
		items.add(new ItemInfo("Wooden Slab", new String[][] { { "slab", "woo" }, { "step", "woo" } }, Material.STEP, (short) 2));
		items.add(new ItemInfo("Cobblestone Slab", new String[][] { { "slab", "cob", "sto" }, { "slab", "cob" } }, Material.STEP, (short) 3));
		items.add(new ItemInfo("Brick", new String[][] { { "bric" } }, Material.BRICK));
		items.add(new ItemInfo("TNT", new String[][] { { "tnt" }, { "boom" } }, Material.TNT));
		items.add(new ItemInfo("Bookshelf", new String[][] { { "bookshe" }, { "book", "she" } }, Material.BOOKSHELF));
		items.add(new ItemInfo("Moss Stone", new String[][] { { "moss", "sto" }, { "moss" } }, Material.MOSSY_COBBLESTONE));
		items.add(new ItemInfo("Obsidian", new String[][] { { "obsi" } }, Material.OBSIDIAN));
		items.add(new ItemInfo("Torch", new String[][] { { "torc" } }, Material.TORCH));
		items.add(new ItemInfo("Fire", new String[][] { { "fire" } }, Material.FIRE));
		items.add(new ItemInfo("Monster Spawner", new String[][] { { "spawn" } }, Material.MOB_SPAWNER));
		items.add(new ItemInfo("Wooden Stairs", new String[][] { { "stair", "wood" } }, Material.WOOD_STAIRS));
		items.add(new ItemInfo("Chest", new String[][] { { "chest" } }, Material.CHEST));
		items.add(new ItemInfo("Diamond Ore", new String[][] { { "ore", "diam" } }, Material.DIAMOND_ORE));
		items.add(new ItemInfo("Diamond Block", new String[][] { { "diam", "bl" } }, Material.DIAMOND_BLOCK));
		items.add(new ItemInfo("Crafting Table", new String[][] { { "benc" }, { "squa" }, { "craft" } }, Material.WORKBENCH));
		items.add(new ItemInfo("Farmland", new String[][] { { "soil" }, { "farm" } }, Material.SOIL));
		items.add(new ItemInfo("Furnace", new String[][] { { "furna" }, { "cooke" } }, Material.FURNACE));
		items.add(new ItemInfo("Ladder", new String[][] { { "ladd" } }, Material.LADDER));
		items.add(new ItemInfo("Rails", new String[][] { { "rail" }, { "trac" } }, Material.RAILS));
		items.add(new ItemInfo("Cobblestone Stairs", new String[][] { { "stair", "cob", "sto" }, { "stair", "cob" } }, Material.COBBLESTONE_STAIRS));
		items.add(new ItemInfo("Lever", new String[][] { { "lever" }, { "switc" } }, Material.LEVER));
		items.add(new ItemInfo("Stone Pressure Plate", new String[][] { { "pres", "plat", "ston" } }, Material.STONE_PLATE));
		items.add(new ItemInfo("Wooden Pressure Plate", new String[][] { { "pres", "plat", "wood" } }, Material.WOOD_PLATE));
		items.add(new ItemInfo("Redstone Ore", new String[][] { { "ore", "red" }, { "ore", "rs" } }, Material.REDSTONE_ORE));
		items.add(new ItemInfo("Redstone Torch", new String[][] { { "torc", "red" }, { "torc", "rs" } }, Material.REDSTONE_TORCH_ON));
		items.add(new ItemInfo("Stone Button", new String[][] { { "stone", "button" }, { "button" } }, Material.STONE_BUTTON));
		items.add(new ItemInfo("Snow Tile", new String[][] { { "tile", "snow" }, { "snow", "slab" } }, Material.SNOW));
		items.add(new ItemInfo("Ice", new String[][] { { "ice" } }, Material.ICE));
		items.add(new ItemInfo("Snow Block", new String[][] { { "snow" } }, Material.SNOW_BLOCK));
		items.add(new ItemInfo("Cactus", new String[][] { { "cact" } }, Material.CACTUS));
		items.add(new ItemInfo("Clay Block", new String[][] { { "clay", "blo" } }, Material.CLAY));
		items.add(new ItemInfo("Jukebox", new String[][] { { "jukeb" } }, Material.JUKEBOX));
		items.add(new ItemInfo("Fence", new String[][] { { "fence" } }, Material.FENCE));
		items.add(new ItemInfo("Pumpkin", new String[][] { { "pump" } }, Material.PUMPKIN));
		items.add(new ItemInfo("Netherrack", new String[][] { { "netherr" }, { "netherst" }, { "hellst" } }, Material.NETHERRACK));
		items.add(new ItemInfo("Soul Sand", new String[][] { { "soul", "sand" }, { "soul" }, { "slowsa" }, { "nether", "mud" }, { "slow", "sand" }, { "quick", "sand" }, { "mud" } }, Material.SOUL_SAND));
		items.add(new ItemInfo("Glowstone", new String[][] { { "glow", "stone" }, { "light", "stone" } }, Material.GLOWSTONE));
		items.add(new ItemInfo("Portal", new String[][] { {"port"} }, Material.PORTAL));
		items.add(new ItemInfo("Jack-O-Lantern", new String[][] { { "jack" }, { "lante" } }, Material.JACK_O_LANTERN));
		items.add(new ItemInfo("Trapdoor", new String[][] { { "trap", "doo" }, { "hatc" } }, Material.TRAP_DOOR));
		items.add(new ItemInfo("Iron Shovel", new String[][] { { "shov", "ir" }, { "spad", "ir" } }, Material.IRON_SPADE));
		items.add(new ItemInfo("Iron Pickaxe", new String[][] { { "pick", "ir" } }, Material.IRON_PICKAXE));
		items.add(new ItemInfo("Iron Axe", new String[][] { { "axe", "ir" } }, Material.IRON_AXE));
		items.add(new ItemInfo("Flint and Steel", new String[][] { { "steel" }, { "lighter" }, { "flin", "ste" } }, Material.FLINT_AND_STEEL));
		items.add(new ItemInfo("Apple", new String[][] { { "appl" } }, Material.APPLE));
		items.add(new ItemInfo("Bow", new String[][] { { "bow" } }, Material.BOW));
		items.add(new ItemInfo("Arrow", new String[][] { { "arro" } }, Material.ARROW));
		items.add(new ItemInfo("Coal", new String[][] { { "coal" } }, Material.COAL));
		items.add(new ItemInfo("Charcoal", new String[][] { { "char", "coal" }, { "char" } }, Material.COAL, (short) 1));
		items.add(new ItemInfo("Diamond", new String[][] { { "diamo" } }, Material.DIAMOND));
		items.add(new ItemInfo("Iron Ingot", new String[][] { { "ingo", "ir" }, { "iron" } }, Material.IRON_INGOT));
		items.add(new ItemInfo("Gold Ingot", new String[][] { { "ingo", "go" }, { "gold" } }, Material.GOLD_INGOT));
		items.add(new ItemInfo("Iron Sword", new String[][] { { "swor", "ir" } }, Material.IRON_SWORD));
		items.add(new ItemInfo("Wooden Sword", new String[][] { { "swor", "woo" } }, Material.WOOD_SWORD));
		items.add(new ItemInfo("Wooden Shovel", new String[][] { { "shov", "wo" }, { "spad", "wo" } }, Material.WOOD_SPADE));
		items.add(new ItemInfo("Wooden Pickaxe", new String[][] { { "pick", "woo" } }, Material.WOOD_PICKAXE));
		items.add(new ItemInfo("Wooden Axe", new String[][] { { "axe", "woo" } }, Material.WOOD_AXE));
		items.add(new ItemInfo("Stone Sword", new String[][] { { "swor", "sto" } }, Material.STONE_SWORD));
		items.add(new ItemInfo("Stone Shovel", new String[][] { { "shov", "sto" }, { "spad", "sto" } }, Material.STONE_SPADE));
		items.add(new ItemInfo("Stone Pickaxe", new String[][] { { "pick", "sto" } }, Material.STONE_PICKAXE));
		items.add(new ItemInfo("Stone Axe", new String[][] { { "axe", "sto" } }, Material.STONE_AXE));
		items.add(new ItemInfo("Diamond Sword", new String[][] { { "swor", "dia" } }, Material.DIAMOND_SWORD));
		items.add(new ItemInfo("Diamond Shovel", new String[][] { { "shov", "dia" }, { "spad", "dia" } }, Material.DIAMOND_SPADE));
		items.add(new ItemInfo("Diamond Pickaxe", new String[][] { { "pick", "dia" } }, Material.DIAMOND_PICKAXE));
		items.add(new ItemInfo("Diamond Axe", new String[][] { { "axe", "dia" } }, Material.DIAMOND_AXE));
		items.add(new ItemInfo("Stick", new String[][] { { "stic" } }, Material.STICK));
		items.add(new ItemInfo("Bowl", new String[][] { { "bo", "wl" } }, Material.BOWL));
		items.add(new ItemInfo("Mushroom Soup", new String[][] { { "soup" } }, Material.MUSHROOM_SOUP));
		items.add(new ItemInfo("Gold Sword", new String[][] { { "swor", "gol" } }, Material.GOLD_SWORD));
		items.add(new ItemInfo("Gold Shovel", new String[][] { { "shov", "gol" }, { "spad", "gol" } }, Material.GOLD_SPADE));
		items.add(new ItemInfo("Gold Pickaxe", new String[][] { { "pick", "gol" } }, Material.GOLD_PICKAXE));
		items.add(new ItemInfo("Gold Axe", new String[][] { { "axe", "gol" } }, Material.GOLD_AXE));
		items.add(new ItemInfo("String", new String[][] { { "stri" } }, Material.STRING));
		items.add(new ItemInfo("Feather", new String[][] { { "feat" } }, Material.FEATHER));
		items.add(new ItemInfo("Gunpowder", new String[][] { { "gun" }, { "sulph" } }, Material.SULPHUR));
		items.add(new ItemInfo("Wooden Hoe", new String[][] { { "hoe", "wo" } }, Material.WOOD_HOE));
		items.add(new ItemInfo("Stone Hoe", new String[][] { { "hoe", "sto" } }, Material.STONE_HOE));
		items.add(new ItemInfo("Iron Hoe", new String[][] { { "hoe", "iro" } }, Material.IRON_HOE));
		items.add(new ItemInfo("Diamond Hoe", new String[][] { { "hoe", "dia" } }, Material.DIAMOND_HOE));
		items.add(new ItemInfo("Gold Hoe", new String[][] { { "hoe", "go" } }, Material.GOLD_HOE));
		items.add(new ItemInfo("Seeds", new String[][] { { "seed" } }, Material.SEEDS));
		items.add(new ItemInfo("Wheat", new String[][] { { "whea" } }, Material.WHEAT));
		items.add(new ItemInfo("Bread", new String[][] { { "brea" } }, Material.BREAD));
		items.add(new ItemInfo("Leather Cap", new String[][] { { "cap", "lea" }, { "hat", "lea" }, { "helm", "lea" } }, Material.LEATHER_HELMET));
		items.add(new ItemInfo("Leather Tunic", new String[][] { { "tun", "lea" }, { "ches", "lea" } }, Material.LEATHER_CHESTPLATE));
		items.add(new ItemInfo("Leather Pants", new String[][] { { "pan", "lea" }, { "trou", "lea" }, { "leg", "lea" } }, Material.LEATHER_LEGGINGS));
		items.add(new ItemInfo("Leather Boots", new String[][] { { "boo", "lea" } }, Material.LEATHER_BOOTS));
		items.add(new ItemInfo("Chainmail Helmet", new String[][] { { "cap", "cha" }, { "hat", "cha" }, { "helm", "cha" } }, Material.CHAINMAIL_HELMET));
		items.add(new ItemInfo("Chainmail Chestplate", new String[][] { { "tun", "cha" }, { "ches", "cha" } }, Material.CHAINMAIL_CHESTPLATE));
		items.add(new ItemInfo("Chainmail Leggings", new String[][] { { "pan", "cha" }, { "trou", "cha" }, { "leg", "cha" } }, Material.CHAINMAIL_LEGGINGS));
		items.add(new ItemInfo("Chainmail Boots", new String[][] { { "boo", "cha" } }, Material.CHAINMAIL_BOOTS));
		items.add(new ItemInfo("Iron Helmet", new String[][] { { "cap", "ir" }, { "hat", "ir" }, { "helm", "ir" } }, Material.IRON_HELMET));
		items.add(new ItemInfo("Iron Chestplate", new String[][] { { "tun", "ir" }, { "ches", "ir" } }, Material.IRON_CHESTPLATE));
		items.add(new ItemInfo("Iron Leggings", new String[][] { { "pan", "ir" }, { "trou", "ir" }, { "leg", "ir" } }, Material.IRON_LEGGINGS));
		items.add(new ItemInfo("Iron Boots", new String[][] { { "boo", "ir" } }, Material.IRON_BOOTS));
		items.add(new ItemInfo("Diamond Helmet", new String[][] { { "cap", "dia" }, { "hat", "dia" }, { "helm", "dia" } }, Material.DIAMOND_HELMET));
		items.add(new ItemInfo("Diamond Chestplate", new String[][] { { "tun", "dia" }, { "ches", "dia" } }, Material.DIAMOND_CHESTPLATE));
		items.add(new ItemInfo("Diamond Leggings", new String[][] { { "pan", "dia" }, { "trou", "dia" }, { "leg", "dia" } }, Material.DIAMOND_LEGGINGS));
		items.add(new ItemInfo("Diamond Boots", new String[][] { { "boo", "dia" } }, Material.DIAMOND_BOOTS));
		items.add(new ItemInfo("Gold Helmet", new String[][] { { "cap", "go" }, { "hat", "go" }, { "helm", "go" } }, Material.GOLD_HELMET));
		items.add(new ItemInfo("Gold Chestplate", new String[][] { { "tun", "go" }, { "ches", "go" } }, Material.GOLD_CHESTPLATE));
		items.add(new ItemInfo("Gold Leggings", new String[][] { { "pan", "go" }, { "trou", "go" }, { "leg", "go" } }, Material.GOLD_LEGGINGS));
		items.add(new ItemInfo("Gold Boots", new String[][] { { "boo", "go" } }, Material.GOLD_BOOTS));
		items.add(new ItemInfo("Flint", new String[][] { { "flin" } }, Material.FLINT));
		items.add(new ItemInfo("Raw Porkchop", new String[][] { { "pork" }, { "ham" } }, Material.PORK));
		items.add(new ItemInfo("Cooked Porkchop", new String[][] { { "pork", "cook" }, { "baco" } }, Material.GRILLED_PORK));
		items.add(new ItemInfo("Paintings", new String[][] { { "paint" } }, Material.PAINTING));
		items.add(new ItemInfo("Golden Apple", new String[][] { { "appl", "go" } }, Material.GOLDEN_APPLE));
		items.add(new ItemInfo("Sign", new String[][] { { "sign" } }, Material.SIGN));
		items.add(new ItemInfo("Wooden Door", new String[][] {  { "door", "wood" }, { "door" } }, Material.WOODEN_DOOR));
		items.add(new ItemInfo("Bucket", new String[][] { { "buck" }, { "bukk" } }, Material.BUCKET));
		items.add(new ItemInfo("Water Bucket", new String[][] { { "water", "buck" } }, Material.WATER_BUCKET));
		items.add(new ItemInfo("Lava Bucket", new String[][] { { "lava", "buck" } }, Material.LAVA_BUCKET));
		items.add(new ItemInfo("Minecart", new String[][] { { "cart" } }, Material.MINECART));
		items.add(new ItemInfo("Saddle", new String[][] { { "sad" }, { "pig" } }, Material.SADDLE));
		items.add(new ItemInfo("Iron Door", new String[][] { { "door", "iron" } }, Material.IRON_DOOR));
		items.add(new ItemInfo("Redstone Dust", new String[][] { { "red", "ston", "dust" }, {"red", "ston" }, { "dust", "rs" }, { "dust", "red" }, { "reds" } }, Material.REDSTONE));
		items.add(new ItemInfo("Snowball", new String[][] { { "snow", "ball" } }, Material.SNOW_BALL));
		items.add(new ItemInfo("Boat", new String[][] { { "boat" } }, Material.BOAT));
		items.add(new ItemInfo("Leather", new String[][] { { "lea" }, { "hide" } }, Material.LEATHER));
		items.add(new ItemInfo("Milk Bucket", new String[][] { { "milk" } }, Material.MILK_BUCKET));
		items.add(new ItemInfo("Clay Brick", new String[][] { { "bric", "cl" }, { "sin", "bric" } }, Material.CLAY_BRICK));
		items.add(new ItemInfo("Clay", new String[][] { { "cla" } }, Material.CLAY_BALL));
		items.add(new ItemInfo("Sugar Cane", new String[][] { { "reed" }, { "cane" } }, Material.SUGAR_CANE));
		items.add(new ItemInfo("Paper", new String[][] { { "pape" } }, Material.PAPER));
		items.add(new ItemInfo("Book", new String[][] { { "book" } }, Material.BOOK));
		items.add(new ItemInfo("Slimeball", new String[][] { { "slime" } }, Material.SLIME_BALL));
		items.add(new ItemInfo("Storage Minecart", new String[][] { { "cart", "sto" }, { "cart", "che" }, { "cargo" } }, Material.STORAGE_MINECART));
		items.add(new ItemInfo("Powered Minecart", new String[][] { { "cart", "pow" }, { "engine" } }, Material.POWERED_MINECART));
		items.add(new ItemInfo("Egg", new String[][] { { "egg" } }, Material.EGG));
		items.add(new ItemInfo("Compass", new String[][] { { "comp" } }, Material.COMPASS));
		items.add(new ItemInfo("Fishing Rod", new String[][] { { "rod" }, { "pole" } }, Material.FISHING_ROD));
		items.add(new ItemInfo("Clock", new String[][] { { "cloc" }, { "watc" } }, Material.WATCH));
		items.add(new ItemInfo("Glowstone Dust", new String[][] { { "glow", "sto", "dus" }, { "glow", "dus" }, { "ligh", "dust" } }, Material.GLOWSTONE_DUST));
		items.add(new ItemInfo("Raw Fish", new String[][] { { "fish" } }, Material.RAW_FISH));
		items.add(new ItemInfo("Cooked Fish", new String[][] { { "fish", "coo" }, { "kipper" } }, Material.COOKED_FISH));
		items.add(new ItemInfo("Ink Sac", new String[][] { { "ink" }, { "dye", "bla" } }, Material.INK_SACK));
		items.add(new ItemInfo("Red Dye", new String[][] { { "dye", "red" }, { "pain", "red" }, { "pet", "ros" }, { "pet", "red" } }, Material.INK_SACK, (short) 1));
		items.add(new ItemInfo("Cactus Green", new String[][] { { "cact", "gree" }, { "dye", "gree" }, { "pain", "gree" } }, Material.INK_SACK, (short) 2));
		items.add(new ItemInfo("Cocoa Beans", new String[][] { { "bean" }, { "choco" }, { "cocoa" }, { "dye", "bro" }, { "pain", "bro" } }, Material.INK_SACK, (short) 3));
		items.add(new ItemInfo("Lapis Lazuli", new String[][] { { "dye", "lapi" }, { "dye", "blu" }, { "pain", "blu" } }, Material.INK_SACK, (short) 4));
		items.add(new ItemInfo("Purple Dye", new String[][] { { "dye", "pur" }, { "pain", "pur" } }, Material.INK_SACK, (short) 5));
		items.add(new ItemInfo("Cyan Dye", new String[][] { { "dye", "cya" }, { "pain", "cya" } }, Material.INK_SACK, (short) 6));
		items.add(new ItemInfo("Light Gray Dye", new String[][] { { "dye", "lig", "gra" }, { "dye", "lig", "grey" }, { "pain", "lig", "grey" }, { "pain", "lig", "grey" } }, Material.INK_SACK, (short) 7));
		items.add(new ItemInfo("Gray Dye", new String[][] { { "dye", "gra" }, { "dye", "grey" }, { "pain", "grey" }, { "pain", "grey" } }, Material.INK_SACK, (short) 8));
		items.add(new ItemInfo("Pink Dye", new String[][] { { "dye", "pin" }, { "pain", "pin" } }, Material.INK_SACK, (short) 9));
		items.add(new ItemInfo("Lime Dye", new String[][] { { "dye", "lim" }, { "pain", "lim" }, { "dye", "lig", "gree" }, { "pain", "lig", "gree" } }, Material.INK_SACK, (short) 10));
		items.add(new ItemInfo("Dandelion Yellow", new String[][] { { "dye", "yel" }, { "yel", "dan" }, { "pet", "dan" }, { "pet", "yel" } }, Material.INK_SACK, (short) 11));
		items.add(new ItemInfo("Light Blue Dye", new String[][] { { "dye", "lig", "blu" }, { "pain", "lig", "blu" } }, Material.INK_SACK, (short) 12));
		items.add(new ItemInfo("Magenta Dye", new String[][] { { "dye", "mag" }, { "pain", "mag" } }, Material.INK_SACK, (short) 13));
		items.add(new ItemInfo("Orange Dye", new String[][] { { "dye", "ora" }, { "pain", "ora" } }, Material.INK_SACK, (short) 14));
		items.add(new ItemInfo("Bone Meal", new String[][] { { "bonem" }, { "bone", "me" }, { "dye", "whi" }, { "pain", "whi" } }, Material.INK_SACK, (short) 15));
		items.add(new ItemInfo("Bone", new String[][] { { "bone" }, { "femur" } }, Material.BONE));
		items.add(new ItemInfo("Sugar", new String[][] { { "suga" } }, Material.SUGAR));
		items.add(new ItemInfo("Cake", new String[][] { { "cake" } }, Material.CAKE));
		items.add(new ItemInfo("Bed", new String[][] { { "bed" } }, Material.BED));
		items.add(new ItemInfo("Redstone Repeater", new String[][] { {"repe", "reds"}, { "diod" }, { "repeat" } }, Material.DIODE));
		items.add(new ItemInfo("Cookie", new String[][] { { "cooki" } }, Material.COOKIE));
		items.add(new ItemInfo("Map", new String[][] { { "map" } }, Material.MAP));
		items.add(new ItemInfo("Shears", new String[][] { {"shea"} }, Material.SHEARS));
		items.add(new ItemInfo("Gold Music Disc", new String[][] { { "dis", "gol" }, { "rec", "gol" } }, Material.GOLD_RECORD));
		items.add(new ItemInfo("Green Music Disc", new String[][] { { "dis", "gre" }, { "rec", "gre" } }, Material.GREEN_RECORD));
	}

	public static ItemInfo itemById(int typeId) {
		return itemByType(Material.getMaterial(typeId), (short) 0);
	}
	
	public static ItemInfo itemById(int typeId, short subType) {
		return itemByType(Material.getMaterial(typeId), subType);
	}
	
	public static ItemInfo itemByStack(ItemStack itemStack) {
		if (itemStack == null) {
			return null;
		}
		
		for (ItemInfo item : items) {
			if (itemStack.getType().equals(item.getType()) && item.isDurable())
				return item;
			else if (itemStack.getType().equals(item.getType()) && item.getSubTypeId() == itemStack.getDurability())
				return item;
		}
		
		return null;
	}
	
	public static ItemInfo itemByItem(Item item) {
		for(ItemInfo i : items) {
			if (item.equals(i))
				return i;
		}
		return null;
	}

	public static ItemInfo itemByType(Material type, short subType) {
		for(ItemInfo item : items) {
			if(item.getType() == type && item.getSubTypeId() == subType) {
				return item;
			}
		}
		return null;
	}

	public static ItemInfo itemByString(String string) {

		// int
		Pattern pattern = Pattern.compile("(?i)^(\\d+)$");
		Matcher matcher = pattern.matcher(string);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			return itemById(id);
		}

		// int:int
		matcher.reset();
		pattern = Pattern.compile("(?i)^(\\d+):(\\d+)$");
		matcher = pattern.matcher(string);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			short type = Short.parseShort(matcher.group(2));
			return itemById(id, type);
		}

		// name
		matcher.reset();
		pattern = Pattern.compile("(?i)^(.*)$");
		matcher = pattern.matcher(string);
		if (matcher.find()) {
			String name = matcher.group(1);
			return itemByName(name);
		}

		return null;
	}

	public static ItemInfo itemByName(ArrayList<String> search) {
		String searchString = GenericFunctions.join(search, " ");
		return itemByName(searchString);
	}

	public static ItemInfo[] itemByNames(ArrayList<String> search, boolean multi) {
		String searchString = GenericFunctions.join(search, " ");
		return itemsByName(searchString, multi);
	}

	/**
	 * Multi-Item return search for dumping all items with the search string to the player
	 * 
	 * 
	 * @param searchString
	 * @param multi
	 * @return Array of items found
	 */
	public static ItemInfo[] itemsByName(String searchString, boolean multi) {
		if (multi == false)
			return new ItemInfo[] {itemByName(searchString)};

		ItemInfo[] itemList = new ItemInfo[] {};

		if (searchString.matches("\\d+:\\d+")) {
			// Match on integer:short to get typeId and subTypeId

			// Retrieve/parse data
			String[] params = searchString.split(":");
			int typeId = Integer.parseInt(params[0]);
			short subTypeId = Short.parseShort(params[1]);

			// Iterate through Items
			for (ItemInfo item : items) {
				// Test for match
				if (item.getId() == typeId && item.getSubTypeId() == subTypeId) {
					itemList[0] = item;
					break;
				}
			}
		} else if (searchString.matches("\\d+")) {

			// Retrieve/parse data
			int typeId = Integer.parseInt(searchString);

			// Iterate through Items
			int i = 0;
			for (ItemInfo item : items) {
				// Test for match
				if (item.getId() == typeId) {
					itemList[i] = item;
					i++;
				}                 
			}
		} else {
			// Else this must be a string that we need to identify

			// Iterate through Items
			int i = 0;
			for (ItemInfo item : items) {
				// Look through each possible match criteria
				for (String[] attributes : item.search) {
					boolean match = false;
					// Loop through entire criteria strings
					for (String attribute : attributes) {
						if (searchString.toLowerCase().contains(attribute)) {
							match = true;
							break;
						}
					}
					// THIS was a match
					if (match) {
						itemList[i] = item;
						i++;
					}
				}
			}
		}

		return itemList;
	}

	/**
	 * Single item search function, for when we only ever want to return 1 result
	 * 
	 * @param searchString
	 * @return
	 */
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
				if (item.getId() == typeId && item.getSubTypeId() == subTypeId) {
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
				if (item.getId() == typeId && item.getSubTypeId() == subTypeId) {
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