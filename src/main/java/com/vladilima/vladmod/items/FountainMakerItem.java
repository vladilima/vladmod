package com.vladilima.vladmod.items;

import com.vladilima.vladmod.fountain.FountainManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FountainMakerItem extends Item {
    public FountainMakerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        if (!level.isClientSide()) {
            FountainManager.makeFountain(level, player.blockPosition());
        }
        return InteractionResultHolder.success(itemstack);
    }
}
