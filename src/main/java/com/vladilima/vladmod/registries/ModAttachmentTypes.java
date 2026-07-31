package com.vladilima.vladmod.registries;

import com.mojang.serialization.Codec;
import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.attachments.DarkFountainsAttachment;
import com.vladilima.vladmod.darkworld.DarkWorldTheme;
import com.vladilima.vladmod.fountain.DarkFountain;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModAttachmentTypes {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, VladMod.MOD_ID);

    public static final Supplier<AttachmentType<List<DarkFountain>>> DARK_FOUNTAINS = ATTACHMENT_TYPES.register(
            DarkFountainsAttachment.NAME, () -> DarkFountainsAttachment.TYPE
    );

    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Attachments...");

        ATTACHMENT_TYPES.register(eventBus);
    }
}
