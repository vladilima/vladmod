package com.vladilima.vladmod.gui.textbox;

import com.vladilima.vladmod.registries.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class TextboxMenu extends AbstractContainerMenu {
    public TextboxMenu(int containerId, Inventory playerInv) {
        super(ModMenuTypes.TEXTBOX.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
