package com.commodorethrawn.strawgolem.config;

import com.commodorethrawn.strawgolem.Strawgolem;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

public class ConfigHelper {
    private static final String FILTER_MODE_WHITELIST = "whitelist";
    private static final String FILTER_MODE_BLACKLIST = "blacklist";

    public static boolean isReplantEnabled() {
        return Strawgolem.config.replantEnabled;
    }

    public static boolean isDeliveryEnabled() {
        return Strawgolem.config.deliveryEnabled;
    }

    public static int getSearchRangeHorizontal() {
        return Strawgolem.config.searchRangeHorizontal;
    }

    public static int getLifespan() {
        return Strawgolem.config.lifespan;
    }

    public static int getSearchRangeVertical() {
        return Strawgolem.config.searchRangeVertical;
    }

    public static boolean blockHarvestAllowed(Block block) {
        switch (Strawgolem.config.filterMode) {
            case FILTER_MODE_WHITELIST:
                // prioritise whitelist
                FilterMatch whitelistMatch = blockMatchesFilter(block, Strawgolem.config.whiteList);
                // if we got a whitelist match by mod, check if we're blacklisted by item
                if (whitelistMatch == FilterMatch.Mod)
                    return blockMatchesFilter(block, Strawgolem.config.blackList) != FilterMatch.Exact;
                return whitelistMatch != FilterMatch.None;

            case FILTER_MODE_BLACKLIST:
                // prioritise blacklist
                FilterMatch blacklistMatch = blockMatchesFilter(block, Strawgolem.config.whiteList);
                // if we got a blacklist match by mod, check if we're whitelisted by item
                if (blacklistMatch == FilterMatch.Mod)
                    return blockMatchesFilter(block, Strawgolem.config.blackList) == FilterMatch.Exact;
                return blacklistMatch == FilterMatch.None;

            default:
                return true;
        }
    }

    public static FilterMatch blockMatchesFilter(Block block, String[] filter) {
        FilterMatch bestMatch = FilterMatch.None;

        for (String s : filter) {
            String[] elements = s.split(":");

            if (elements.length == 1 && Registry.BLOCK.getId(block).getNamespace().equals(elements[0])) {
                bestMatch = FilterMatch.Mod;
                continue;
            }

            if (elements.length >= 2 && Registry.BLOCK.getId(block).getNamespace().equals(elements[0]) && Registry.BLOCK.getId(block).getPath().equals(elements[1])) {
                bestMatch = FilterMatch.Exact;
                break;
            }
        }

        return bestMatch;
    }

    public enum FilterMatch {
        None,
        Mod,
        Exact,
    }
}
