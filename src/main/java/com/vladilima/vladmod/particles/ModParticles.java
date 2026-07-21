package com.vladilima.vladmod.particles;

import com.vladilima.vladmod.VladMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, VladMod.MOD_ID);

    public static final Supplier<SimpleParticleType> DARKNESS_PARTICLES = PARTICLE_TYPES.register(
            "darkness",
            () -> new SimpleParticleType(false)
    );

    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Particles...");
        PARTICLE_TYPES.register(eventBus);
    }
}
