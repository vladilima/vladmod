package com.vladilima.vladmod.event;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.darkworld.DarkWorld;
import com.vladilima.vladmod.darkworld.DarkWorldManager;
import com.vladilima.vladmod.darkworld.DimensionManager;
import com.vladilima.vladmod.fountain.DarkFountain;
import com.vladilima.vladmod.fountain.FountainManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.Objects;

@EventBusSubscriber(modid = VladMod.MOD_ID)
public class ServerEvents {

    public static boolean loadedFountains = false;
    public static boolean loadedDarkWorlds = false;

    @SubscribeEvent
    public static void levelTick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();

        if (!level.isClientSide()) {
            if (!loadedFountains) {
                if (FountainManager.darkFountains != null) {
                    for (DarkFountain fountain : FountainManager.darkFountains) {
                        if (fountain != null) {
                            DarkFountain.loadDarkness(level.getServer(), fountain);
                        }
                    }
                }
                loadedFountains = true;
            }

            if (!loadedDarkWorlds) {
                loadedDarkWorlds = true;
            }

            if (FountainManager.darkFountains != null && !FountainManager.darkFountains.isEmpty()) {
                FountainManager.darkFountains.remove(null);
                for (DarkFountain fountain : FountainManager.darkFountains) {
                    if (fountain != null && level.dimension() == fountain.fountainDimension) {
                        fountain.tick(level);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void dataAttachmentSave(LevelEvent.Save event) {
        LevelAccessor level = event.getLevel();
        if (level.getServer() != null && isLevelCorrect(level)) {
            DarkWorldManager.save(level.getServer());
            FountainManager.save(level.getServer());
        }
    }

    @SubscribeEvent
    public static void dataAttachmentLoad(LevelEvent.Load event) {
        loadedDarkWorlds = false;
        loadedFountains = false;
        LevelAccessor level = event.getLevel();
        if (level.getServer() != null && isLevelCorrect(level)) {
            DarkWorldManager.load(level.getServer());
            FountainManager.load(level.getServer());
        }
    }

    private static boolean isLevelCorrect(LevelAccessor level) {
        return Objects.requireNonNull(Objects.requireNonNull(level.getServer()).getLevel(Level.OVERWORLD)).dimensionType() == level.dimensionType();
    }

    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level() instanceof ServerLevel level) {
            if (level.dimension() == DimensionManager.DARK_WORLD) {
                DarkWorld darkWorld = DarkWorld.findDarkWorld(event.getEntity().blockPosition());
                if (darkWorld != null) {
                    darkWorld.sendEnterPacket((ServerPlayer) event.getEntity());
                }
            }
        }
    }

}
