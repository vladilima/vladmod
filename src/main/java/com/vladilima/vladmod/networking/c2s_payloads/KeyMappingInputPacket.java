package com.vladilima.vladmod.networking.c2s_payloads;

import com.vladilima.vladmod.networking.C2SPackets;
import com.vladilima.vladmod.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record KeyMappingInputPacket(String key) implements CustomPacketPayload {

    public static final Type<KeyMappingInputPacket> ID = C2SPackets.makeId("summon_stand_keybind");
    public static final StreamCodec<FriendlyByteBuf, KeyMappingInputPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, KeyMappingInputPacket::key,
                    KeyMappingInputPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static IPayloadHandler<KeyMappingInputPacket> getPayloadHandler() {
        return (payload, context) -> {
            StandUser player = (StandUser) context.player();

            switch (payload.key()) {
                case "key.vladmod.stand_toggle":
                    player.vladmod$toggleStand(context.player().level());
                    break;
                case "key.vladmod.ability_1":
                    player.vladmod$ability1();
                    break;
            }
        };
    }
}
