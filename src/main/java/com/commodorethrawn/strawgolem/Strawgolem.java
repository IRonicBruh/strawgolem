package com.commodorethrawn.strawgolem;

import com.commodorethrawn.strawgolem.config.ConfigHolder;
import com.commodorethrawn.strawgolem.entity.EntityRegistry;
import com.commodorethrawn.strawgolem.entity.RenderStrawGolem;
import com.commodorethrawn.strawgolem.entity.capability.ILifespan;
import com.commodorethrawn.strawgolem.entity.capability.Lifespan;
import com.commodorethrawn.strawgolem.entity.capability.LifespanStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Strawgolem.MODID)
public class Strawgolem {
    public static final String MODID = "strawgolem";
    public static Logger logger = LogManager.getLogger(MODID);

    public Strawgolem() {
        logger.info("Initializing strawgolem");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        logger.info("Strawgolem common setup");
        CapabilityManager.INSTANCE.register(ILifespan.class, new LifespanStorage(), Lifespan::new);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        logger.info("Strawgolem client setup");
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.STRAW_GOLEM_TYPE, RenderStrawGolem::new);
    }


}