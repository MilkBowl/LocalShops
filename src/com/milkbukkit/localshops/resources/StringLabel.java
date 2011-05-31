package com.milkbukkit.localshops.resources;

import java.util.ListResourceBundle;

import com.milkbukkit.localshops.ResourceManager;

public class StringLabel extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            { ResourceManager.LOAD, "[%PLUGIN_NAME%] Loaded with %NUM_SHOPS% shop(s)" },
            { ResourceManager.ENABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% is enabled: %UUID%" },
            { ResourceManager.DISABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% is disabled." },
            { ResourceManager.ECONOMY_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No economic plugins found, please refer to the documentation." },
            { ResourceManager.PERMISSION_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No permission plugins found, please refer to the documentation." },
    };

}