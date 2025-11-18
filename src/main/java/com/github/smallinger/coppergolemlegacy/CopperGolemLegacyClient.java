package com.github.smallinger.coppergolemlegacy;

import com.github.smallinger.coppergolemlegacy.client.model.CopperGolemModel;
import com.github.smallinger.coppergolemlegacy.client.renderer.CopperChestRenderer;
import com.github.smallinger.coppergolemlegacy.client.renderer.CopperGolemRenderer;
import com.github.smallinger.coppergolemlegacy.client.renderer.CopperGolemStatueRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = CopperGolemLegacy.MODID, dist = Dist.CLIENT)
public class CopperGolemLegacyClient {
    public CopperGolemLegacyClient(ModContainer container, net.neoforged.bus.api.IEventBus modEventBus) {
        
        // Register config screen
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        
        // Register entity renderers on MOD bus
        modEventBus.addListener(CopperGolemLegacyClient::registerEntityRenderers);
        modEventBus.addListener(CopperGolemLegacyClient::registerLayerDefinitions);
        modEventBus.addListener(CopperGolemLegacyClient::registerBlockEntityRenderers);
        modEventBus.addListener(CopperGolemLegacyClient::onClientSetup);
    }

    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        CopperGolemLegacy.LOGGER.info("HELLO FROM CLIENT SETUP");
        CopperGolemLegacy.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
    
    static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CopperGolemLegacy.COPPER_GOLEM.get(), CopperGolemRenderer::new);
    }
    
    static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CopperGolemModel.LAYER_LOCATION, CopperGolemModel::createBodyLayer);
        
        // Register statue pose layers
        event.registerLayerDefinition(CopperGolemModel.STATUE_STANDING, CopperGolemModel::createStandingStatueBodyLayer);
        event.registerLayerDefinition(CopperGolemModel.STATUE_RUNNING, CopperGolemModel::createRunningPoseBodyLayer);
        event.registerLayerDefinition(CopperGolemModel.STATUE_SITTING, CopperGolemModel::createSittingPoseBodyLayer);
        event.registerLayerDefinition(CopperGolemModel.STATUE_STAR, CopperGolemModel::createStarPoseBodyLayer);
    }
    
    static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CopperGolemLegacy.COPPER_CHEST_BLOCK_ENTITY.get(), CopperChestRenderer::new);
        event.registerBlockEntityRenderer(CopperGolemLegacy.COPPER_GOLEM_STATUE_BLOCK_ENTITY.get(), CopperGolemStatueRenderer::new);
    }
}

