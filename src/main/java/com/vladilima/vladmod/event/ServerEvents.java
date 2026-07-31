package com.vladilima.vladmod.event;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.fountain.DarkFountain;
import com.vladilima.vladmod.fountain.FountainManager;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.List;

@EventBusSubscriber(modid = VladMod.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void levelTick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();

        List<DarkFountain> darkFountains = FountainManager.getFountains(level);
        if (darkFountains != null && !darkFountains.isEmpty()) {
            FountainManager.removeNull(level);
            for (DarkFountain fountain : darkFountains) {
                if (fountain != null && level.dimension() == fountain.fountainDimension) {
                    fountain.tick(level);
                }
            }
        }
    }
}
