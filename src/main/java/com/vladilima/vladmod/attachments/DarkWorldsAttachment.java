package com.vladilima.vladmod.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vladilima.vladmod.darkworld.DarkWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.List;

public class DarkWorldsAttachment {
    public static final Codec<DarkWorld> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BoundingBox.CODEC.fieldOf("boundingBox").forGetter(DarkWorld::boundingBox),
            BlockPos.CODEC.listOf().fieldOf("greatDoors").forGetter(DarkWorld::greatDoors)
    ).apply(instance, DarkWorld::new));


    public static final String NAME = "dark_worlds";
    public static final AttachmentType<List<DarkWorld>> TYPE = AttachmentType
            .builder(DarkWorldsAttachment::buildNewlist)
            .serialize(CODEC.listOf())
            .build();

    private static List<DarkWorld> buildNewlist() {
        return List.of();
    }
}
