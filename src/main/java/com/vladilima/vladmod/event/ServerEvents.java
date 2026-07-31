package com.vladilima.vladmod.event;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.fountain.DarkFountain;
import com.vladilima.vladmod.fountain.FountainManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.Objects;

@EventBusSubscriber(modid = VladMod.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void levelTick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();

        if (FountainManager.darkFountains != null && !FountainManager.darkFountains.isEmpty()) {
            FountainManager.darkFountains.remove(null);
            for (DarkFountain fountain : FountainManager.darkFountains) {
                if (fountain != null && level.dimension() == fountain.fountainDimension) {
                    fountain.tick(level);
                }
            }
        }
    }

    @SubscribeEvent
    public static void dataAttachmentSave(LevelEvent.Save event) {
        LevelAccessor level = event.getLevel();
        if (level.getServer() != null && isLevelCorrect(level)) {
            FountainManager.save(level.getServer());
        }
    }

    @SubscribeEvent
    public static void dataAttachmentLoad(LevelEvent.Load event) {
        LevelAccessor level = event.getLevel();
        if (level.getServer() != null && isLevelCorrect(level)) {
            FountainManager.load(level.getServer());
        }
    }

    private static boolean isLevelCorrect(LevelAccessor level) {
        return Objects.requireNonNull(Objects.requireNonNull(level.getServer()).getLevel(Level.OVERWORLD)).dimensionType() == level.dimensionType();
    }
}
