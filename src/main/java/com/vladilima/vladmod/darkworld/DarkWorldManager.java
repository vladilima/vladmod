package com.vladilima.vladmod.darkworld;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.registries.ModAttachmentTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class DarkWorldManager {
    public static ArrayList<DarkWorld> darkWorlds;

    public static void save(MinecraftServer server) {
        Level overworldLevel = server.getLevel(Level.OVERWORLD);
        assert overworldLevel != null;

        overworldLevel.setData(ModAttachmentTypes.DARK_WORLDS, darkWorlds);
        VladMod.LOGGER.info("Saved Dark Worlds: " + overworldLevel.getData(ModAttachmentTypes.DARK_WORLDS));
    }

    public static void load(MinecraftServer server) {
        Level overworldLevel = server.getLevel(Level.OVERWORLD);
        assert overworldLevel != null;

        darkWorlds = new ArrayList<>(overworldLevel.getData(ModAttachmentTypes.DARK_WORLDS));
        VladMod.LOGGER.info("Loaded Dark Worlds: " + DarkWorldManager.darkWorlds);
    }
}
