package com.milkbukkit.localshops.resources;

import java.util.ListResourceBundle;

import com.milkbukkit.localshops.ResourceManager;

public class StringLabel_pirate extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }
    
    static final Object[][] contents = {
            { ResourceManager.LOAD, "[%PLUGIN_NAME%] Loaded with %NUM_SHOPS% shop(s)" },
            { ResourceManager.ENABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% be enabled: %UUID%" },
            { ResourceManager.DISABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% be disabled." },
            { ResourceManager.ECONOMY_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No economic plugins found, please refer t' t' documentation." },
            { ResourceManager.PERMISSION_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No permission plugins found, please refer t' t' documentation." },
    };
}
