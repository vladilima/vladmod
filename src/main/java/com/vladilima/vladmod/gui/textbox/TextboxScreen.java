package com.vladilima.vladmod.gui.textbox;

import com.vladilima.vladmod.VladMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;
import java.util.regex.Pattern;

public class TextboxScreen extends AbstractContainerScreen<TextboxMenu> {
    // Points to 'assets/vladmod/textures/gui/sprites/dark_world_menu.png'
    private static final ResourceLocation SPRITE = ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "dark_world_menu");
    private static final ResourceLocation FONT = ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "determination_mono");

    private final String text;
    private final int textProgressSpeed;
    private final String SLOW_CHARS;

    public TextboxScreen(TextboxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.text = title.getString();
        this.textProgressSpeed = 0;
        this.SLOW_CHARS = COMMA_ONLY;
    }

    private int textProgress = 0;
    int nextCharProgress = 0;
    @Override
    protected void containerTick() {
        if (textProgress < text.length()) {
            nextCharProgress += 1;
            if (nextCharProgress >= textProgressSpeed) {
                String currentChar = String.valueOf(text.charAt(textProgress));
                if (Pattern.matches(SLOW_CHARS, currentChar)) {
                    nextCharProgress = -5;
                } else {
                    nextCharProgress = 0;
                }

                textProgress += 1;
            }
        }
    }

    int textboxX;
    int textboxY;
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        guiGraphics.pose().pushPose();

        int i = 0;
        for (String drawString : text.substring(0, textProgress).split("\n")) {
            if (i == 0) {
                drawString = "* " + drawString;
            } else {
                drawString = "  " + drawString;
            }

            FormattedCharSequence displayText = FormattedCharSequence.forward(drawString, Style.EMPTY.withFont(FONT));
            guiGraphics.drawString(
                    font,
                    displayText,
                    (textboxX + 18) + .5f,
                    (textboxY + 18) + (18 * i) + .5f,
                    2039649,
                    false
            );
            guiGraphics.drawString(
                    font,
                    displayText,
                    (textboxX + 18),
                    (textboxY + 18) + (18 * i),
                    Color.WHITE.hashCode(),
                    false
            );

            i += 1;
        }

        guiGraphics.pose().popPose();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int textboxWidth = 297;
        int textboxHeight = 84;

        textboxX = (width / 2) - (textboxWidth / 2);
        textboxY = (height - textboxHeight) - 44; // 44px is hotbar height * 2

        guiGraphics.blitSprite(SPRITE, textboxX, textboxY, textboxWidth, textboxHeight);
    }

    private static final String EVERYTHING = ",!\\?\\.";
    private static final String COMMA_ONLY = ",";
}
