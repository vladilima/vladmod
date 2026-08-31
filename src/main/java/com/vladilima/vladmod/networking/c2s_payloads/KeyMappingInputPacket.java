package com.vladilima.vladmod.networking.c2s_payloads;

import com.vladilima.vladmod.gui.textbox.TextboxMenu;
import com.vladilima.vladmod.powers.SoulPower;
import com.vladilima.vladmod.registries.ModPackets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
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
                    player.openMenu(new SimpleMenuProvider(
                            (containerId, playerInventory, player1) -> new TextboxMenu(containerId, playerInventory),
                            Component.literal("Textbox")
                    ));
//                    SoulPower.attemptSealFountain(player);
                    break;
            }
        };
    }
}
