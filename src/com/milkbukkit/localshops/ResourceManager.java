package com.milkbukkit.localshops;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;

import com.milkbukkit.localshops.util.GenericFunctions;

public class ResourceManager {
    // Constants
    public final static String LOAD = "LOAD";
    public final static String ENABLE = "ENABLE";
    public final static String DISABLE = "DISABLE";
    public final static String ECONOMY_NOT_FOUND = "ECONOMY_NOT_FOUND";
    public final static String PERMISSION_NOT_FOUND = "PERMISSION_NOT_FOUND";
    
    // Objects
    private PluginDescriptionFile pdf;
    private ResourceBundle RES_BUNDLE = ResourceBundle.getBundle("com.milkbukkit.localshops.resources.StringLabel", new Locale("pirate"));
    
    // Color Map
    private static final Map<String, ChatColor> COLOR_MAP = new HashMap<String, ChatColor>();
    static {
        COLOR_MAP.put("%AQUA%", ChatColor.AQUA);
        COLOR_MAP.put("%BLACK%", ChatColor.BLACK);
        COLOR_MAP.put("%BLUE%", ChatColor.BLUE);
        COLOR_MAP.put("%DARK_AQUA%", ChatColor.DARK_AQUA);
        COLOR_MAP.put("%DARK_BLUE%", ChatColor.DARK_BLUE);
        COLOR_MAP.put("%DARK_GRAY%", ChatColor.DARK_GRAY);
        COLOR_MAP.put("%DARK_GREEN%", ChatColor.DARK_GREEN);
        COLOR_MAP.put("%DARK_PURPLE%", ChatColor.DARK_PURPLE);
        COLOR_MAP.put("%DARK_RED%", ChatColor.DARK_RED);
        COLOR_MAP.put("%GOLD%", ChatColor.GOLD);
        COLOR_MAP.put("%GRAY%", ChatColor.GRAY);
        COLOR_MAP.put("%GREEN%", ChatColor.GREEN);
        COLOR_MAP.put("%LIGHT_PURPLE%", ChatColor.LIGHT_PURPLE);
        COLOR_MAP.put("%RED%", ChatColor.RED);
        COLOR_MAP.put("%WHITE%", ChatColor.WHITE);
        COLOR_MAP.put("%YELLOW%", ChatColor.YELLOW);
    }
    
    public ResourceManager(PluginDescriptionFile p) {
        pdf = p;
    }
    
    // Get String
    public String getString(String key) {
        return parsePluginData(parseColors(RES_BUNDLE.getString(key)));
    }
    
    // Get String w/ Data Values (replace key array and replace value array MUST match lengths!
    public String getString(String key, String[] replaceKeys, Object[] replaceValues) {
        if(replaceKeys.length != replaceValues.length) {
            return null;
        }
        
        String s = getString(key);
        for(int i = 0; i < replaceKeys.length; i++) {
            s = s.replaceAll(replaceKeys[i], replaceValues[i].toString());
        }
        return s;
    }
    
    // Parse color data
    private String parseColors(String s) {
        for(String key : COLOR_MAP.keySet()) {
            s = s.replaceAll(key, COLOR_MAP.get(key).toString());
        }
        return s;
    }
    
    // Parse Plugin Data
    private String parsePluginData(String s) {
        s = s.replaceAll("%PLUGIN_NAME%", pdf.getName());
        s = s.replaceAll("%PLUGIN_VERSION%", pdf.getVersion());
        s = s.replaceAll("%PLUGIN_AUTHORS%", GenericFunctions.join(pdf.getAuthors(), ", "));
        return s;
    }
}
