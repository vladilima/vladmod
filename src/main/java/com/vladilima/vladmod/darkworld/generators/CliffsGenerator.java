package com.vladilima.vladmod.darkworld.generators;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.blocks.great_door.GreatDoorCoreBlockEntity;
import com.vladilima.vladmod.darkworld.GenerationUtils;
import com.vladilima.vladmod.fountain.DarkFountain;
import com.vladilima.vladmod.registries.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

import static com.vladilima.vladmod.darkworld.DarkWorld.DARK_WORLD_SIZE;

public class CliffsGenerator extends Generator {
    private final static BlockState SURFACE_BLOCK = Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState();
    private final static BlockState CLIFF_BLOCK = Blocks.BLUE_TERRACOTTA.defaultBlockState();

    private final int SURFACE_LEVEL = 8;
    private final int CLIFF_MIN_Y = SURFACE_LEVEL + 5;
    private final int CLIFF_MAX_Y = 63;

    @Override
    public void empty(Level level, List<BlockPos> generationArea) {
        VladMod.LOGGER.debug("Began generation of empty DW Location.");

        generationArea.forEach(blockPos -> {
            LevelChunk levelChunk = level.getChunkAt(blockPos);
            for (int y = 1; y < level.getMaxBuildHeight(); y++) {
                GenerationUtils.setBlockAtChunkSection(
                        levelChunk,
                        blockPos.atY(y),
                        y <= SURFACE_LEVEL ? Blocks.BLACK_CONCRETE.defaultBlockState() : Blocks.AIR.defaultBlockState()
                );
            }
        });

        VladMod.LOGGER.debug("Finished DW hollowing-out.");
    }

    @Override
    public void surface(Level level, List<BlockPos> generationArea) {
        VladMod.LOGGER.debug("Began generation of surface of Dark World.");

        List<PathSegment> paths = new ArrayList<>();

        // Create Path Segments
        int pathAmount = getPathAmount(generationArea);
        for (int i = 0; i < pathAmount; i++) {
            int pathSegmentAmount = GenerationUtils.randInt(7, 15);
            List<PathSegment> currentPath = new ArrayList<>();
            for (int j = 0; j < pathSegmentAmount; j++) {
                PathSegment newSegment = getNewSegment(level, generationArea, paths, currentPath);
                if (newSegment == null) {
                    currentPath.clear();
                    i -= 1;
                    break;
                } else {
                    currentPath.add(newSegment);
                }
            }

            paths.addAll(currentPath);
        }

        // Place down paths
        for (PathSegment path : paths) {
            BlockPos.betweenClosedStream(path.boundingBox).forEach(blockPos -> {
                LevelChunk levelChunk = level.getChunkAt(blockPos);
                GenerationUtils.setBlockAtChunkSection(
                        levelChunk,
                        blockPos,
                        CLIFF_BLOCK
                );
            });
        }

        // Place "surface" blocks
        for (BlockPos pos : generationArea) {
            for (int y = CLIFF_MIN_Y; y <= CLIFF_MAX_Y + 1; y++) {
                if (level.isEmptyBlock(pos.atY(y))) {
                    break;
                } else if (level.getBlockState(pos.atY(y)) == CLIFF_BLOCK) {
                    if (level.getBlockState(pos.atY(y + 1)) == Blocks.AIR.defaultBlockState()) {
                        LevelChunk levelChunk = level.getChunkAt(pos);
                        GenerationUtils.setBlockAtChunkSection(
                                levelChunk,
                                pos.atY(y),
                                SURFACE_BLOCK
                        );
                        break;
                    }
                }
            }
        }

        VladMod.LOGGER.debug("Finished DW surface Generation.");
    }

    // Decides amount of path structures the dark world will have based on size
    private static int getPathAmount(List<BlockPos> generationArea) {
        int pathAmount = (int) (Math.sqrt(generationArea.size()) / 15);
        VladMod.LOGGER.debug("Amount of Paths: " + pathAmount);
        return pathAmount;
    }

    private PathSegment getNewSegment(Level level, List<BlockPos> generationArea, List<PathSegment> paths, List<PathSegment> currentPath) {
        PathSegment prevSegment = !currentPath.isEmpty() ? currentPath.getLast() : null;
        PathSegment newSegment = new PathSegment();

        newSegment.startPos = prevSegment != null ? prevSegment.finishPos :
                Util.getRandom(generationArea, level.random).atY(GenerationUtils.randInt(CLIFF_MIN_Y, CLIFF_MAX_Y));

        int attempts = 0;
        List<PathSegment> everyPath = new ArrayList<>(List.copyOf(paths));
        everyPath.addAll(currentPath);
        while (!isSegmentValid(generationArea, everyPath, newSegment, prevSegment)) {
            int segmentSize = GenerationUtils.randInt(8, 24);
            newSegment.startPos = prevSegment != null ?
                    prevSegment.finishPos :
                    Util.getRandom(generationArea, level.random).atY(GenerationUtils.randInt(CLIFF_MIN_Y, CLIFF_MAX_Y));
            newSegment.direction = Direction.Plane.HORIZONTAL.getRandomDirection(level.random);
            newSegment.finishPos = newSegment.startPos.relative(newSegment.direction, segmentSize);
            newSegment.boundingBox = newPathBox(newSegment);

            // Delete Path if not possible to create a new segment
            attempts += 1;
            if (attempts > 5) {
                return null;
            }
        }

        return newSegment;
    }

    private boolean isSegmentValid(List<BlockPos> generationArea, List<PathSegment> everyPath, PathSegment newSegment, PathSegment prevSegment) {
        if (newSegment.finishPos == null || newSegment.direction == null || newSegment.boundingBox == null) {
            return false;
        } else if (prevSegment != null && newSegment.direction == prevSegment.direction.getOpposite()) {
            return false;
        } else if (!generationArea.contains(newSegment.finishPos.atY(1))) {
            return false;
        } else {
            // Check if any of segment corners are outside Generation Area
            AABB bBox = newSegment.boundingBox;
            if (!generationArea.contains(BlockPos.containing(bBox.minX, 1, bBox.minZ))) {
                return false;
            } else if (!generationArea.contains(BlockPos.containing(bBox.maxX, 1, bBox.minZ))) {
                return false;
            } else if (!generationArea.contains(BlockPos.containing(bBox.minX, 1, bBox.maxZ))) {
                return false;
            } else if (!generationArea.contains(BlockPos.containing(bBox.maxX, 1, bBox.maxZ))) {
                return false;
            } else {
                for (PathSegment path : everyPath) {
                    if (path != prevSegment && path.boundingBox.intersects(newSegment.boundingBox)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private AABB newPathBox(PathSegment newSegment) {
        AABB pathBox = AABB.encapsulatingFullBlocks(newSegment.startPos, newSegment.finishPos);
        pathBox = pathBox.setMinY(SURFACE_LEVEL + 1);

        return pathBox.inflate(1.5, 0, 1.5).move(.5,0,.5);
    }

    private class PathSegment {
        AABB boundingBox;
        BlockPos startPos;
        BlockPos finishPos;
        Direction direction;
    }

    @Override
    public List<BlockPos> features(Level level, List<BlockPos> generationArea, DarkFountain fountainInfo) {
        ServerLevel fountainLevel = level.getServer().getLevel(fountainInfo.fountainDimension);

        // Gets unique door positions from LW room.
        List<BlockPos> lightDoors = new ArrayList<>();
        for (BlockPos doorPos : fountainInfo.roomInfo().doorBlocks) {
            if (fountainLevel.getBlockState(doorPos).getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
                boolean isDoubleDoor = false;
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    if (lightDoors.contains(doorPos.relative(dir))) {
                        isDoubleDoor = true;
                    }
                }

                if (!isDoubleDoor) {
                    lightDoors.add(doorPos);
                };
            }
        }

        List<BlockPos> greatDoors = new ArrayList<>();

        for (BlockPos door : lightDoors) {
            BlockPos relativeToFountain = door.subtract(fountainInfo.fountainPos);

            BlockPos darkWorldPos = BoundingBox.encapsulatingPositions(generationArea).get().getCenter();
            BlockPos largePos = darkWorldPos.offset(relativeToFountain.multiply(DARK_WORLD_SIZE / 2));

            AABB doorArea = AABB.of(new BoundingBox(largePos.atY(1))).inflate(DARK_WORLD_SIZE * 2, 1, DARK_WORLD_SIZE * 2);
            for (BlockPos pos : generationArea) {
                if (doorArea.contains(pos.getCenter())) {
                    if (isValidForDoor(level, pos, fountainLevel.getBlockState(door).getValue(HorizontalDirectionalBlock.FACING))) {
                        VladMod.LOGGER.info("Placed a door");
                        BlockPos validPos = pos;
                        while (!level.isEmptyBlock(validPos.above())) {
                            validPos = validPos.above();
                        }
                        validPos = validPos.above();
                        BlockPlaceContext context = new BlockPlaceContext(
                                level,
                                null,
                                InteractionHand.MAIN_HAND,
                                ItemStack.EMPTY,
                                new BlockHitResult(
                                        validPos.getCenter(),
                                        fountainLevel.getBlockState(door).getValue(HorizontalDirectionalBlock.FACING),
                                        validPos,
                                        false
                                )
                        );
                        level.setBlockAndUpdate(validPos, ModBlocks.GREAT_DOOR.getDefaultState().setValue(BlockStateProperties.HORIZONTAL_FACING, fountainLevel.getBlockState(door).getValue(HorizontalDirectionalBlock.FACING)));
                        GreatDoorCoreBlockEntity greatDoor = (GreatDoorCoreBlockEntity) level.getBlockEntity(validPos);
                        greatDoor.lightDoorPos = door;
                        greatDoor.lightDoorDim = fountainInfo.fountainDimension;
                        greatDoor.structure.place(context);

                        greatDoors.add(validPos);
                        break;
                    }
                }
            }
        }

        VladMod.LOGGER.debug("Finished populating DW with Features.");
        return greatDoors;
    }

    private boolean isValidForDoor(Level level, BlockPos doorCandidatePos, Direction doorDirection) {
        BlockPos currentPos = doorCandidatePos;
        while (!level.isEmptyBlock(currentPos.above())) {
            currentPos = currentPos.above();
        }
        if (level.getBlockState(currentPos) == SURFACE_BLOCK) {
//            if (GreatDoorStructure.STRUCTURE.get().canPlace(new BlockPlaceContext(level, null, null, null, new BlockHitResult(currentPos.getCenter(), doorDirection, currentPos, false)))) {
//                return true;
//            }
//            VladMod.LOGGER.error("Can't place Great Door here");
            return true;
        }

        VladMod.LOGGER.error("Not a surface block");
        return false;
    }

    @Override
    public void entities(Level level, List<BlockPos> generationArea) {
        VladMod.LOGGER.debug("Began populating Dark World with Entities.");
    }
}
