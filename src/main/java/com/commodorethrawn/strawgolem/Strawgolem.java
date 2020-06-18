package com.commodorethrawn.strawgolem;

import com.commodorethrawn.strawgolem.config.StrawgolemConfig;
import com.commodorethrawn.strawgolem.entity.EntityStrawGolem;
import com.commodorethrawn.strawgolem.mixin.PlaceBlockCallback;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResult;

public class Strawgolem implements ModInitializer {
    public static final String MODID = "strawgolem";
    public static StrawgolemConfig config;

    @Override
    public void onInitialize() {
        config = ConfigManager.loadConfig(StrawgolemConfig.class);
        PlaceBlockCallback.EVENT.register((world, pos) -> {
            Block block = world.getBlockState(pos).getBlock();
            Block blockDown = world.getBlockState(pos.down()).getBlock();
            if ((block == Blocks.CARVED_PUMPKIN && blockDown == Blocks.HAY_BLOCK)
                    || (block == Blocks.HAY_BLOCK && blockDown == Blocks.CARVED_PUMPKIN)) {
                world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                EntityStrawGolem golem = new EntityStrawGolem(world);
                golem.setPos(pos.getX() + 0.5D, pos.getY() - 1.0D, pos.getZ() + 0.5D);
                world.spawnEntity(golem);
            }
            return ActionResult.SUCCESS;
        });
    }
}
