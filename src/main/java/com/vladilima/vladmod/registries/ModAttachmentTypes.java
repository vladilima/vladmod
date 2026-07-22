package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.attachments.DarkFountainsAttachment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachmentTypes {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, VladMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Attachments...");
        ATTACHMENT_TYPES.register(DarkFountainsAttachment.NAME, () -> DarkFountainsAttachment.TYPE);

        ATTACHMENT_TYPES.register(eventBus);
    }
}
