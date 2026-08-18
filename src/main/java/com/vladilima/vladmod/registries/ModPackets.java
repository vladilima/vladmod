package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.networking.c2s_payloads.KeyMappingInputPacket;
import com.vladilima.vladmod.networking.s2c_payloads.DarkWorldInfoPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPackets {
    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> makeId(String id) {
        return new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, id));
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(ModPackets::registerC2S);
        eventBus.addListener(ModPackets::registerS2C);
    }

    public static void registerC2S(RegisterPayloadHandlersEvent event) {
        VladMod.LOGGER.info("Registering C2S Packets...");
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(KeyMappingInputPacket.ID, KeyMappingInputPacket.CODEC, KeyMappingInputPacket.getPayloadHandler());
    }

    public static void registerS2C(RegisterPayloadHandlersEvent event) {
        VladMod.LOGGER.info("Registering S2C Packets...");
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(DarkWorldInfoPacket.ID, DarkWorldInfoPacket.CODEC, DarkWorldInfoPacket.getPayloadHandler());
    }
}
