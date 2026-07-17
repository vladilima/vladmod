package com.vladilima.vladmod.powers;

import com.vladilima.vladmod.entity.custom.StandEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface StandUser {
    void vladmod$setStand(Stand stand);
    @Nullable Stand vladmod$getStand();
    void vladmod$toggleStand(Level level);


    void vladmod$setStandEntity(StandEntity standEntity);
    @Nullable StandEntity vladmod$getStandEntity();

    void vladmod$ability1();

    enum Stand {
        STAR_PLATINUM("Star Platinum", "star_platinum");

        private final String name;
        private final String id;

        Stand(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
}
