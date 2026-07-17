package com.vladilima.vladmod.attachments;

import com.vladilima.vladmod.fountain.DarkFountain;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.ArrayList;

public class DarkFountainsAttachment {

    public static final String NAME = "dark_fountains";
    public static final AttachmentType<ArrayList<DarkFountain>> TYPE =
            AttachmentType.builder(() -> new ArrayList<DarkFountain>()).build();
}
