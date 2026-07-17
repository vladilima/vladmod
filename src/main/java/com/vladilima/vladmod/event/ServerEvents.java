package com.vladilima.vladmod.event;

import com.vladilima.vladmod.attachments.DarkFountainsAttachment;
import com.vladilima.vladmod.fountain.DarkFountain;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;

public class ServerEvents {

    @SubscribeEvent
    public void levelTick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();

        ArrayList<DarkFountain> darkFountains = level.getExistingData(DarkFountainsAttachment.TYPE).orElse(null);
        if (darkFountains != null) {
            darkFountains.remove(null);
            for (DarkFountain fountain : darkFountains) {
                if (fountain != null) {
                    fountain.tick(level);
                }
            }
        }
    }
}
