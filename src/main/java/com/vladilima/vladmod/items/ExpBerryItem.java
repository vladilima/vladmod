package com.vladilima.vladmod.items;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ExpBerryItem extends ItemNameBlockItem {
    public ExpBerryItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        ItemStack itemstack = super.finishUsingItem(stack, level, entityLiving);

        if (!level.isClientSide && entityLiving.getType() == EntityType.PLAYER) {
            Player player = (Player) entityLiving;
            player.giveExperiencePoints(5);
        };

        return itemstack;
    }
}
