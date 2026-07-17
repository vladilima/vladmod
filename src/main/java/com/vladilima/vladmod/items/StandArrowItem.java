package com.vladilima.vladmod.items;

import com.vladilima.vladmod.powers.StandUser;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class StandArrowItem extends Item {
    public StandArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 10;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide()) {
            stack.consume(1, livingEntity);

            StandUser player = (StandUser) livingEntity;

            player.vladmod$setStand(StandUser.Stand.STAR_PLATINUM);
            System.out.println("Set stand to " + StandUser.Stand.STAR_PLATINUM.getName());
        }

        return stack;
    }
}
