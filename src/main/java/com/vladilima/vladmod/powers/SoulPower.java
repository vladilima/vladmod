package com.vladilima.vladmod.powers;

import com.vladilima.vladmod.darkworld.DarkWorld;
import com.vladilima.vladmod.darkworld.DarkWorldManager;
import com.vladilima.vladmod.darkworld.DimensionManager;
import com.vladilima.vladmod.fountain.DarkFountain;
import com.vladilima.vladmod.fountain.FountainManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SoulPower {
    public static void attemptSealFountain(Player player) {
        if (!isInDarkWorld(player)) {
            player.displayClientMessage(Component.literal("Player is in Light World."), true);
            return;
        }

        Vec3 currentPos = player.getEyePosition();
        Vec3 lookDirection = player.getLookAngle();

        DarkWorld darkWorld = null;

        int INTERACT_REACH = 6;
        for (int i = 0; i < INTERACT_REACH; i++) {
            currentPos = currentPos.add(lookDirection);
            darkWorld = getFountainInPos(currentPos);
            if (darkWorld != null) {
                break;
            }
        }

        if (darkWorld != null) {
            sealFountain(darkWorld, player.level());
        } else {
            player.displayClientMessage(Component.literal("No Fountain found."), true);
        }
    }

    private static void sealFountain(DarkWorld darkWorld, Level darkWorldLevel) {
        for (DarkFountain fountain : FountainManager.darkFountains) {
            if (fountain.darkWorld != null && fountain.darkWorld.equals(darkWorld)) {
                ServerLevel fountainLevel = darkWorldLevel.getServer().getLevel(fountain.fountainDimension);
                for (BlockPos blockPos : fountain.roomInfo().roomBlocks) {
                    fountainLevel.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                }


                DimensionTransition dimTransition = new DimensionTransition(fountainLevel,
                        fountain.fountainPos.getBottomCenter(), Vec3.ZERO, 0f, 0f,
                        DimensionTransition.DO_NOTHING
                );
                AABB darkWorldAABB = AABB.of(darkWorld.boundingBox);
                darkWorldAABB = darkWorldAABB.setMinY(0);
                darkWorldAABB = darkWorldAABB.setMaxY(128);
                for (Entity entity : darkWorldLevel.getEntities(null, darkWorldAABB)){
                    entity.changeDimension(dimTransition);
                }

                FountainManager.nullFountain(fountain);
            }
        }
    }

    private static DarkWorld getFountainInPos(Vec3 currentPos) {
        Vec3 currentPos2D = currentPos.multiply(1, 0, 1); // Fountain Pos 2D
        for (DarkWorld dw : DarkWorldManager.darkWorlds) {
            if (dw.fountainPos.atY(0).relative(Direction.WEST, 3).distToCenterSqr(currentPos2D) <= (4*4)) {
                return dw;
            }
        }
        return null;
    }

    private static boolean isInDarkWorld(Player player) {
        return player.level().dimension() == DimensionManager.DARK_WORLD;
    }
}
