package com.vladilima.vladmod.datagen;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.registries.ModParticles;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class ModParticleDescriptionProvider extends ParticleDescriptionProvider {
    protected ModParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        // Adds a single sprite particle definition with the file at
        // assets/examplemod/textures/particle/my_single_particle.png.
        sprite(ModParticles.DARKNESS_PARTICLES.get(), ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "darkness"));

        // Adds a multi sprite particle definition, with a vararg parameter. Alternatively accepts a list.
//        spriteSet(ModParticles.DARKNESS_PARTICLE.get(),
//                // The base name.
//                ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "darkness_particle"),
//                // The amount of textures.
//                3,
//                // Whether to reverse the list, i.e. start at the last element instead of the first.
//                false
//        );
    }
}
