package com.vladilima.vladmod.networking;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.networking.c2s_payloads.KeyMappingInputPacket;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.*;
import net.neoforged.neoforge.network.registration.*;

public class C2SPackets {

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> makeId(String id) {
        return new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, id));
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        VladMod.LOGGER.info("Registering C2S Packets...");
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                KeyMappingInputPacket.ID,
                KeyMappingInputPacket.CODEC,
                KeyMappingInputPacket.getPayloadHandler());
    }
}
