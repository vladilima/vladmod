package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.darkworld.generation.DarkWorldGenerators;
import com.vladilima.vladmod.darkworld.generation.DarkWorldTheme;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class ModCustomRegistries {
    @SubscribeEvent // on the mod event bus
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        VladMod.LOGGER.info("Registering Datapack Registries...");
        DarkWorldGenerators.registerGenerators();
        event.dataPackRegistry(
                // The registry key.
                DarkWorldTheme.REGISTRY_KEY,
                // The codec of the registry contents.
                DarkWorldTheme.CODEC,
                // The network codec of the registry contents. Often identical to the normal codec.
                // May be a reduced variant of the normal codec that omits data that is not needed on the client.
                // May be null. If null, registry entries will not be synced to the client at all.
                // May be omitted, which is functionally identical to passing null (a method overload
                // with two parameters is called that passes null to the normal three parameter method).
                null
        );
    }
}
