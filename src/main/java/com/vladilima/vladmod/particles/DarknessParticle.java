package com.vladilima.vladmod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DarknessParticle extends TextureSheetParticle {
    protected DarknessParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet,
                               double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 3.0F;

        if (ySpeed > 0) {
            this.gravity = -.5f;
            this.setPower(3);
        } else {
            this.setPower(5);
            this.gravity = 0;
        }

        this.friction = .6f;
        this.lifetime = 70;

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        if (this.age == 10) {
            this.gravity = -.5f;
        }

        if (this.age > 20) {
            setAlpha(Float.min(1F,1F - ((float) (this.age - 50) / 50)));
        }

        this.quadSize += .002F;

        super.tick();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new DarknessParticle(level, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}
