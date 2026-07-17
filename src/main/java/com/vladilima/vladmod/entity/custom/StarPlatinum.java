package com.vladilima.vladmod.entity.custom;

import com.vladilima.vladmod.powers.GenericPunchStandPower;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class StarPlatinum extends StandEntity {
    public StarPlatinum(EntityType<? extends StandEntity> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20d);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState punchAnimationState_1 = new AnimationState();
    public final AnimationState punchAnimationState_2 = new AnimationState();
    public final AnimationState punchAnimationState_3 = new AnimationState();

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);

        if (data == ANIMATION) {
            switch (this.getAnimation()) {
                case IDLE:
                    this.idleAnimationState.start(this.tickCount);
                    break;
                case FIRST_PUNCH:
                    this.punchAnimationState_3.stop();
                    this.punchAnimationState_1.start(this.tickCount);
                    break;
                case SECOND_PUNCH:
                    this.punchAnimationState_1.stop();
                    this.punchAnimationState_2.start(this.tickCount);
                    break;
                case THIRD_PUNCH:
                    this.punchAnimationState_2.stop();
                    this.punchAnimationState_3.start(this.tickCount);
                    break;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();


        if (!this.level().isClientSide())  {
            genericPower.tick();
        }
    }

    GenericPunchStandPower genericPower = new GenericPunchStandPower(this);
    @Override
    public void ability1() {
        genericPower.basicPunch();
    }

    @Override
    public float transferDamage(float amount) {
        return amount / 4;
    }
}
