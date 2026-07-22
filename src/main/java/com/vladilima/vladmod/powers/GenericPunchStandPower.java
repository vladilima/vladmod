package com.vladilima.vladmod.powers;

import com.vladilima.vladmod.entity.custom.StandEntity;
import com.vladilima.vladmod.registries.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GenericPunchStandPower {
    StandEntity stand;

    public GenericPunchStandPower(StandEntity standEntity) {
        this.stand = standEntity;
    }

    private int attackPlacementTicksLeft = 0;
    private boolean queuedAttack = false;

    private int punchCombo = 0;
    private int punchTicksLeft = 0;
    private static int PUNCH_DURATION = 5;
    public void basicPunch() {
        if (punchTicksLeft <= 0) {
            attackPlacementTicksLeft = 25;
            stand.setPlacement(StandEntity.PLACEMENT_ATTACK);
            punchTicksLeft = PUNCH_DURATION;
            queuedAttack = true;

            punchCombo += 1;

            stand.setAnimation((byte) punchCombo);

            System.out.println(punchCombo);

            if (punchCombo >= 3) {
                punchCombo = 1;
            }

        }
    }

    public void tick() {
        punchTicksLeft -= 1;
        if (this.attackPlacementTicksLeft-- <= 0) {
            stand.setAnimation(StandEntity.IDLE);
            stand.setPlacement(StandEntity.PLACEMENT_IDLE);
            punchCombo = 0;
        }

        if (this.queuedAttack) {
            attackOnce(10);
            this.queuedAttack = false;
        }

    }

    public void attackOnce(float damage) {
        Level level = stand.level();
        if (!level.isClientSide()) {
            Vec3 ONE = new Vec3(1,1.5,1);
            Vec3 lookAngle = stand.getLookAngle();
            Vec3 point = stand.getEyePosition().add(lookAngle);

            AABB hitbox = new AABB(point.subtract(ONE), point.add(ONE));
            List<LivingEntity> hitEntities = level.getEntitiesOfClass(LivingEntity.class, hitbox);

            hitEntities.remove(stand);
            hitEntities.remove(stand.user);

            if (hitEntities.isEmpty()) {
                level.playSound(null, stand.blockPosition(), ModSounds.STAND_PUNCH_MISS.get(), SoundSource.HOSTILE);
            } else {
                level.playSound(null, stand.blockPosition(), ModSounds.STAND_PUNCH.get(), SoundSource.HOSTILE);

                DamageSource dmgSource = stand.damageSources().mobAttack(stand.user);
                hitEntities.forEach(entity -> {
                    entity.knockback(1, -lookAngle.x, -lookAngle.z);
                    entity.hurt(dmgSource, damage);
                });
            }
        }
    }
}
