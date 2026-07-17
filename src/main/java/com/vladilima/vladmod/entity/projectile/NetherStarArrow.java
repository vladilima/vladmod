package com.vladilima.vladmod.entity.projectile;

import com.vladilima.vladmod.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class NetherStarArrow extends AbstractArrow {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator() {
        @Override
        public boolean shouldBlockExplode(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, float f) {
            return false;
        }
    };

    public NetherStarArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }


    public NetherStarArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityType.ARROW, x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public NetherStarArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityType.ARROW, owner, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return ModItems.NETHER_STAR_ARROW.toStack();
    }

    @Override
    protected void onHit(HitResult result) {
        System.out.println("Arrow Test Entity");
        this.level().explode(
                this,
                Explosion.getDefaultDamageSource(this.level(), this.getOwner()),
                EXPLOSION_DAMAGE_CALCULATOR,
                this.getX(),
                this.getY(0.0625),
                this.getZ(),
                4.0F,
                false,
                Level.ExplosionInteraction.TRIGGER
        );
        this.discard();
    }
}
