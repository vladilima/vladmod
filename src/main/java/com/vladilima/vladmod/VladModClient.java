package com.vladilima.vladmod;

import com.mojang.blaze3d.platform.InputConstants;
import com.vladilima.vladmod.registries.ModBlockEntityRenderers;
import com.vladilima.vladmod.registries.ModEntities;
import com.vladilima.vladmod.entity.client.StarPlatinumRenderer;
import com.vladilima.vladmod.networking.c2s_payloads.KeyMappingInputPacket;
import com.vladilima.vladmod.particles.DarknessParticle;
import com.vladilima.vladmod.registries.ModParticles;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = VladMod.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = VladMod.MOD_ID, value = Dist.CLIENT)
public class VladModClient {
    public VladModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        VladMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        VladMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());


        VladMod.LOGGER.info("Registering Entity Renderers...");
        EntityRenderers.register(ModEntities.STAR_PLATINUM.get(), StarPlatinumRenderer::new);
    }

    public static final Lazy<KeyMapping> TOGGLE_STAND_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.vladmod.stand_toggle", // Will be localized using this translation key
            KeyConflictContext.IN_GAME, // Mapping can only be used when a screen isn't open
            KeyModifier.NONE, // Default mapping requires nothing to be held down
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_Z, // Default key is Z
            "key.categories.vladmod.category" // Mapping will be in the modded category
    ));

    public static final Lazy<KeyMapping> ACTIVATE_ABILITY_1 = Lazy.of(() -> new KeyMapping(
            "key.vladmod.ability_1", KeyConflictContext.IN_GAME,
            KeyModifier.NONE, InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,"key.categories.vladmod.category"
    ));

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        VladMod.LOGGER.info("Registering Key Mappings...");
        event.register(TOGGLE_STAND_MAPPING.get());
        event.register(ACTIVATE_ABILITY_1.get());
    }

    @SubscribeEvent // on the game event bus only on the physical client
    public static void onClientTick(ClientTickEvent.Post event) {
        while (TOGGLE_STAND_MAPPING.get().consumeClick()) {
            PacketDistributor.sendToServer(new KeyMappingInputPacket(TOGGLE_STAND_MAPPING.get().getName()));
        }
        while (ACTIVATE_ABILITY_1.get().consumeClick()) {
            PacketDistributor.sendToServer(new KeyMappingInputPacket(ACTIVATE_ABILITY_1.get().getName()));
        }
    }

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        VladMod.LOGGER.info("Registering Particle Providers (Clientside)...");
        event.registerSpriteSet(ModParticles.DARKNESS_PARTICLES.get(), DarknessParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModBlockEntityRenderers.register(event);
    }
}
