package com.vladilima.vladmod.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vladilima.vladmod.fountain.DarkFountain;
import com.vladilima.vladmod.fountain.RoomScanner.ScanResult;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.List;
import java.util.Optional;

public class DarkFountainsAttachment {
    public static final Codec<DarkFountain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ScanResult.CODEC.fieldOf("scan").forGetter(DarkFountain::roomInfo),
            BlockPos.CODEC.fieldOf("currentBlock").forGetter(DarkFountain::currentBlock),
            Codec.INT.fieldOf("ticksAlive").forGetter(DarkFountain::ticksAlive),
            Codec.BOOL.fieldOf("isFilled").forGetter(DarkFountain::isFilled),
            DarkWorldsAttachment.CODEC.optionalFieldOf("darkWorld").forGetter((fountain) -> Optional.ofNullable(fountain.darkWorld))
    ).apply(instance, DarkFountain::new));


    public static final String NAME = "dark_fountains";
    public static final AttachmentType<List<DarkFountain>> TYPE = AttachmentType
            .builder(DarkFountainsAttachment::buildNewlist)
            .serialize(CODEC.listOf())
            .build();

    private static List<DarkFountain> buildNewlist() {
        return List.of();
    }
}
