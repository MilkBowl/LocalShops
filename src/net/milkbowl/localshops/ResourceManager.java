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

package net.milkbowl.localshops;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import net.milkbowl.localshops.objects.Messages;
import net.milkbowl.localshops.util.GenericFunctions;

import org.bukkit.plugin.PluginDescriptionFile;


public class ResourceManager {

	// Objects
	private PluginDescriptionFile pdf;
	private ResourceBundle bundle;

	private final Logger log = Logger.getLogger("Minecraft");

	public ResourceManager(PluginDescriptionFile p, Locale l) {
		pdf = p;
		if(l == null ) {
			bundle = ResourceBundle.getBundle("props/Messages");
		} else {
			try {
				bundle = ResourceBundle.getBundle("props/Messages", l);
			} catch (Exception e) {
				bundle = ResourceBundle.getBundle("props/Messages");
				log.warning(getString(Messages.MAIN_ERROR_LOAD_LOCALE, new String[] {"%LOCALE%"}, new String[] {l.getLanguage()}));
			}
		}
	}

	// Get locale
	public Locale getLocale() {
		return bundle.getLocale();
	}

	// Get String
	public String getString(Messages key) {
		return parsePluginData(GenericFunctions.parseColors(parseBase(bundle.getString(key.getMsg()))));
	}

	// Get String w/ Data Values (replace key array and replace value array MUST match lengths!
	public String getString(Messages key, String[] replaceKeys, Object[] replaceValues) {
		if(replaceKeys.length != replaceValues.length) {
			return null;
		}

		String s = getString(key);
		for(int i = 0; i < replaceKeys.length; i++) {
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
		s = s.replaceAll("%CHAT_PREFIX%", bundle.getString(Messages.BASE_CHAT_PREFIX.getMsg()));
		s = s.replaceAll("%BASESHOP%", bundle.getString(Messages.BASE_SHOP.getMsg()));
		s = s.replaceAll("%TRUE%", bundle.getString(Messages.BASE_TRUE.getMsg()));
		s = s.replaceAll("%FALSE%", bundle.getString(Messages.BASE_FALSE.getMsg()));

		return s;
	}

	// Get Chat Prefix
	public String getChatPrefix() {
		return getString(Messages.BASE_CHAT_PREFIX);
	}
}
