package com.commodorethrawn.strawgolem;

import com.commodorethrawn.strawgolem.config.StrawgolemConfig;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

public class Strawgolem implements ModInitializer {
    public static final String MODID = "strawgolem";
    public static StrawgolemConfig config;

    @Override
    public void onInitialize() {
        config = ConfigManager.loadConfig(ConfigHelperclass);
    }
}
