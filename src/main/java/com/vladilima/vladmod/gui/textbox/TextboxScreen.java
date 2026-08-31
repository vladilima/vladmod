package com.vladilima.vladmod.gui.textbox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.vladilima.vladmod.VladMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TextboxScreen extends AbstractContainerScreen<TextboxMenu> {
    // Points to 'assets/vladmod/textures/gui/sprites/dark_world_menu.png'
    private static final ResourceLocation SPRITE = ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "dark_world_menu");

    public TextboxScreen(TextboxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        super.render();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int textboxWidth = 250;
        int textboxHeight = 84;

        int x = (width / 2) - (textboxWidth / 2);
        int y = (height - textboxHeight) - 44; // 44px is hotbar height * 2

        guiGraphics.blitSprite(SPRITE, x, y, textboxWidth, textboxHeight);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) { }
}
