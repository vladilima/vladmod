package com.vladilima.vladmod.entity.custom;

import com.vladilima.vladmod.sound.ModSounds;
import net.minecraft.core.NonNullList;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class StandEntity extends LivingEntity {
    protected StandEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public boolean summoned = false;
    public LivingEntity user;

    public static final byte
            IDLE = 0,
            FIRST_PUNCH = 1,
            SECOND_PUNCH = 2,
            THIRD_PUNCH = 3,
            BARRAGE = 4,
            BLOCK = 5;

    public static final int
            PLACEMENT_IDLE = 60,
            PLACEMENT_ATTACK = 0;

    public void onSummon(LivingEntity standUser,
                         double x, double y, double z) {
        if (standUser != null) {
            this.user = standUser;
            setUserId(standUser.getId());
        }
        this.setPos(x, y, z);

        this.summoned = true;

        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.blockPosition(), ModSounds.STAND_SUMMON.get(), SoundSource.HOSTILE);
        }

    }

    protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(StandEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> ANIMATION = SynchedEntityData.defineId(StandEntity.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Integer> PLACEMENT = SynchedEntityData.defineId(StandEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(USER_ID, -1);
        builder.define(ANIMATION, IDLE);
        builder.define(PLACEMENT, PLACEMENT_IDLE);
    }

    public int getUserId() { return entityData.get(USER_ID); }
    public void setUserId(int userId) { entityData.set(USER_ID, userId); }

    public int getAnimation() { return entityData.get(ANIMATION); }
    public void setAnimation(byte animation) { entityData.set(ANIMATION, animation); }

    public int getPlacement() { return entityData.get(PLACEMENT); }
    public void setPlacement(int placement) { entityData.set(PLACEMENT, placement); }



    @Override
    public void tick() {
        super.tick();
        if (!this.isRemoved()) {
            this.aiStep();
        }


        if (!this.level().isClientSide())  {
            if (this.user != null) {
                this.updatePos(this.user);
            } else {
                if (this.getUserId() == -1) {
                    this.discard();
                }
            }
        } else {
            this.updatePos((LivingEntity) this.level().getEntity(this.getUserId()));
        }
    }



    // Stand Positioning/Offset/Rotation Code
    private void updatePos(LivingEntity user) {
        if (user == null) {
            return;
        }

        setIdleOffset(user, Entity::setPos);

        this.setYRot(user.getYHeadRot() % 360);
        this.setYBodyRot(user.getYHeadRot() % 360);
        this.setYHeadRot(user.getYHeadRot() % 360);
    }

    @Override
    protected float getMaxHeadRotationRelativeToBody() {
        return 0F;
    }

    private void setIdleOffset(LivingEntity standUser , Entity.MoveFunction positionUpdater) {
        double r = 1.5;
        double yawfix = standUser.getYRot();
        yawfix += this.getPlacement();
        if (yawfix > 360) {
            yawfix -= 360;
        } else if (yawfix < 0) {
            yawfix += 360;
        }
        double ang = (yawfix - 180) * Math.PI;

        double mcap = 0.3;
        Vec3 xyz = standUser.getDeltaMovement();
        double yy = xyz.y() * 0.3;
        if (yy > mcap) {
            yy = mcap;
        } else if (yy < -mcap) {
            yy = -mcap;
        }
        if (isSwimming() || isVisuallyCrawling() || isFallFlying()) {
            yy += 1;
        }


        Vec3 offset = new Vec3(
                (- (-1 * (r * (Math.sin(ang / 180))))),
                (.5 - yy),
                (-(r * (Math.cos(ang / 180))))
        );

        double x1 = standUser.getX() + offset.x;
        double y1 = standUser.getY() + offset.y;
        double z1 = standUser.getZ() + offset.z;

        positionUpdater.accept(this, x1, y1, z1);
    }



    // Ability Methods: Override these to implement abilities
    public abstract void ability1();
//    public abstract void ability2();




    // Sustained Damage
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.user != null){
            if (this.user != source.getEntity() && this != source.getEntity() ) {
                return this.user.hurt(source, transferDamage(amount));
            }
        }

        return false;
    }
    public float transferDamage(float amount) {
        return amount;
    }




    // Obligatory Method Overrides
    @Override
    protected void doPush(Entity entity) { }

    @Override
    public void push(Vec3 vector) { }


    @Override
    public Iterable<ItemStack> getArmorSlots() { return NonNullList.withSize(1, ItemStack.EMPTY); }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) { }

    @Override
    public HumanoidArm getMainArm() { return HumanoidArm.RIGHT; }
}
