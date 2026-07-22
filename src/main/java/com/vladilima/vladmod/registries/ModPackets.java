package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.networking.C2SPackets;
import net.neoforged.bus.api.IEventBus;

public class ModPackets {

    public static void register(IEventBus eventBus) {
        eventBus.addListener(C2SPackets::register);

//        eventBus.addListener(S2CPackets::register);
    }
}
