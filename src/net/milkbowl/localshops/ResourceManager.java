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

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.util.GenericFunctions;

import org.bukkit.plugin.PluginDescriptionFile;

public class ResourceManager {

    // Objects
    private PluginDescriptionFile pdf;
    private ResourceBundle bundle;
    private final Logger log = Logger.getLogger("Minecraft");

    public ResourceManager(PluginDescriptionFile p, Locale l) {
        pdf = p;
        if (l == null) {
            bundle = ResourceBundle.getBundle("props/Messages");
        } else {
            try {
                bundle = ResourceBundle.getBundle("props/Messages", l);
            } catch (Exception e) {
                bundle = ResourceBundle.getBundle("props/Messages");
                log.warning(getString(MsgType.MAIN_ERROR_LOAD_LOCALE, new String[]{"%LOCALE%"}, new String[]{l.getLanguage()}));
            }
        }
    }

    // Get locale
    public Locale getLocale() {
        return bundle.getLocale();
    }

    // Get String
    public String getString(MsgType key) {
        return parsePluginData(GenericFunctions.parseColors(parseBase(bundle.getString(key.getMsg()))));
    }

    // Get String w/ Data Values (replace key array and replace value array MUST match lengths!
    public String getString(MsgType key, String[] replaceKeys, Object[] replaceValues) {
        if (replaceKeys.length != replaceValues.length) {
            return null;
        }

        String s = getString(key);
        for (int i = 0; i < replaceKeys.length; i++) {
            s = s.replaceAll(replaceKeys[i], replaceValues[i].toString());
        }

        //Parse any other values leftover - ORDER IS IMPORTANT!
        s = parseBase(s);
        s = GenericFunctions.parseColors(s);
        s = parsePluginData(s);

        return s;
    }

    // Parse Plugin Data
    private String parsePluginData(String s) {
        s = s.replaceAll("%PLUGIN_NAME%", pdf.getName());
        s = s.replaceAll("%PLUGIN_VERSION%", pdf.getVersion());
        s = s.replaceAll("%PLUGIN_AUTHORS%", GenericFunctions.join(pdf.getAuthors(), ", "));

        return s;
    }

    // Parse Base
    private String parseBase(String s) {
        s = s.replaceAll("%CHAT_PREFIX%", bundle.getString(MsgType.BASE_CHAT_PREFIX.getMsg()));
        s = s.replaceAll("%BASESHOP%", bundle.getString(MsgType.BASE_SHOP.getMsg()));
        s = s.replaceAll("%TRUE%", bundle.getString(MsgType.BASE_TRUE.getMsg()));
        s = s.replaceAll("%FALSE%", bundle.getString(MsgType.BASE_FALSE.getMsg()));

        return s;
    }

    // Get Chat Prefix
    public String getChatPrefix() {
        return getString(MsgType.BASE_CHAT_PREFIX);
    }
}
