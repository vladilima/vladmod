package com.vladilima.vladmod.networking.c2s_payloads;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.darkworld.DarkWorld;
import com.vladilima.vladmod.darkworld.DarkWorldManager;
import com.vladilima.vladmod.registries.ModPackets;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record KeyMappingInputPacket(String key) implements CustomPacketPayload {

    public static final Type<KeyMappingInputPacket> ID = ModPackets.makeId("client_keybind_input");
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
            Player player = context.player();

            switch (payload.key()) {
                case "key.vladmod.soul_confirm":
                    Vec3 currentPos = player.getEyePosition();
                    Vec3 lookDirection = player.getLookAngle();

                    DarkWorld darkWorld = null;

                    int INTERACT_REACH = 6;
                    for (int i = 0; i < INTERACT_REACH; i++) {
                        currentPos = currentPos.add(lookDirection);
                        darkWorld = getDarkWorld(currentPos);
                        if (darkWorld != null) {
                            break;
                        }
                    }

                    if (darkWorld != null) {
                        VladMod.LOGGER.info("Fountain Pos: " + darkWorld.fountainPos);
                    }

                    break;
            }
        };
    }

    private static DarkWorld getDarkWorld(Vec3 currentPos) {
        currentPos = currentPos.multiply(1, 0, 1); // Fountain Pos 2D
        for (DarkWorld dw : DarkWorldManager.darkWorlds) {
            if (dw.fountainPos.atY(0).relative(Direction.WEST, 3).distToCenterSqr(currentPos) <= (19)) {
                return dw;
            }
        }
        return null;
    }
}
