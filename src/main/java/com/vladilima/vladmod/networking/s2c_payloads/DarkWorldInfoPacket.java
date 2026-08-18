package com.vladilima.vladmod.networking.s2c_payloads;

import com.vladilima.vladmod.darkworld.DarkWorldClientData;
import com.vladilima.vladmod.registries.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record DarkWorldInfoPacket(BlockPos fountainPos) implements CustomPacketPayload {

    public static final Type<DarkWorldInfoPacket> ID = ModPackets.makeId("dark_world_info");
    public static final StreamCodec<FriendlyByteBuf, DarkWorldInfoPacket> CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, DarkWorldInfoPacket::fountainPos,
                    DarkWorldInfoPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static IPayloadHandler<DarkWorldInfoPacket> getPayloadHandler() {
        return (payload, context) -> {
            DarkWorldClientData.fountainPos = payload.fountainPos;
        };
    }
}
