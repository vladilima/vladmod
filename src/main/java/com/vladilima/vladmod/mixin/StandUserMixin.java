package com.vladilima.vladmod.mixin;

import com.vladilima.vladmod.registries.ModEntities;
import com.vladilima.vladmod.entity.custom.StandEntity;
import com.vladilima.vladmod.entity.custom.StarPlatinum;
import com.vladilima.vladmod.powers.StandUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class StandUserMixin implements StandUser {
    @Nullable
    @Unique
    private Stand vladmod$stand;

    @Nullable
    @Unique
    private StandEntity vladmod$standEntity;

    @Unique
    private Boolean vladmod$standActive = false;

    public void vladmod$setStand(Stand stand) {
        this.vladmod$stand = stand;
    }

    public Stand vladmod$getStand() {
        return this.vladmod$stand;
    }

    public void vladmod$setStandEntity(StandEntity standEntity) {
        this.vladmod$standEntity = standEntity;
    }

    public StandEntity vladmod$getStandEntity() {
        return vladmod$standEntity;
    }

    @Override
    public void vladmod$toggleStand(Level level) {
        if (vladmod$stand != null) {
            if (vladmod$standActive) {
                if (vladmod$standEntity != null) {
                    vladmod$standEntity.discard();
                }

                vladmod$standActive = false;
                System.out.println("Stand Toggled Off.");
            } else {
                StandEntity standEntity = getStandEntity(vladmod$stand, level);
                LivingEntity user = ((LivingEntity) (Object) this);
                standEntity.onSummon(user, user.getX(), user.getY(), user.getZ());

                level.addFreshEntity(standEntity);
                vladmod$setStandEntity(standEntity);
                vladmod$standActive = true;
                System.out.println("Stand Toggled On.");
            }
        } else {
            System.out.println("No Stand.");
        }
    }

    private StandEntity getStandEntity(Stand stand, Level level) {
        switch (stand.getId()) {
            case "star_platinum":
                return new StarPlatinum(ModEntities.STAR_PLATINUM.get(), level);
            default:
                return new StarPlatinum(ModEntities.STAR_PLATINUM.get(), level);
        }
    }


    @Override
    public void vladmod$ability1() {
        if (vladmod$standActive) {
            vladmod$getStandEntity().ability1();
        }
    }
}
