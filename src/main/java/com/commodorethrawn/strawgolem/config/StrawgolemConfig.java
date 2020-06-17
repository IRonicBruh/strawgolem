package com.commodorethrawn.strawgolem.config;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name = "strawgolem")
public class StrawgolemConfig {
    @Comment(value = "Allow the straw golems to replant a crop when they harvest it.")
    public boolean replantEnabled = true;
    @Comment(value = "Allow the straw golem to deliver a crop (requires replantEnabled = true)")
    public boolean deliveryEnabled = true;
    @Comment(value = "Horizontal search range for crops and chests. Accepted values: 8-32")
    public int searchRangeHorizontal = 12;
    @Comment(value = "Vertical search range for crops and chests. Accepted values: 2-8")
    public int searchRangeVertical = 3;
    @Comment(value = "Sets the method for applying harvest filters.  Note that only the most specific match will be taken into consideration.\n" +
            "If a crop's mod appears in the whitelist, but the crop itself is in the blacklist, the crop will be banned.\n" +
            "Likewise if a crop's mod appears in the blacklist, but the crop itself is in the whitelist, the crop will be allowed.\n" +
            "\"none\": allow all crops to be harvested (default).\n" +
            "\"whitelist\": will deny crops from being harvested unless the most specific match is in the whitelist.\n" +
            "\"blacklist\": will allows crops to be harvested unless the most specific match is in the blacklist.\n")
    public String filterMode = "none";
    @Comment(value = "Whitelist Filter")
    public String[] whiteList = {};
    @Comment(value = "Blacklist Filter")
    public String[] blackList = {};
    @Comment(value = "Set the lifespan, in ticks, of newly created straw golems. Set to -1 for infinite")
    public int lifespan = 168000;

}
