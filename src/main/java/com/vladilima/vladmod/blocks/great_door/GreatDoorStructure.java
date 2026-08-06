package com.vladilima.vladmod.blocks.great_door;

import com.vladilima.vladmod.registries.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import team.lodestar.lodestone.modules.toolkit.multiblock.HorizontalDirectionStructure;
import team.lodestar.lodestone.modules.toolkit.multiblock.MultiBlockStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GreatDoorStructure extends HorizontalDirectionStructure {
    public static final Supplier<GreatDoorStructure> STRUCTURE = () -> (GreatDoorStructure.of(
            new MultiBlockStructure.StructurePiece(-2, 0, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-1, 0, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(1, 0, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(2, 0, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-2, 1, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-1, 1, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(0, 1, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(1, 1, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(2, 1, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-2, 2, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-1, 2, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(0, 2, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(1, 2, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(2, 2, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-2, 3, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-1, 3, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(0, 3, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(1, 3, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(2, 3, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-2, 4, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-1, 4, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(0, 4, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(1, 4, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(2, 4, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-2, 5, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-1, 5, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(0, 5, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(1, 5, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(2, 5, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-2, 6, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-1, 6, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(0, 6, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(1, 6, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(2, 6, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-2, 7, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-1, 7, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(0, 7, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(1, 7, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(2, 7, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-2, 8, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(-1, 8, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(0, 8, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(1, 8, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState()),
            new MultiBlockStructure.StructurePiece(2, 8, 0, ModBlocks.GREAT_DOOR_COMPONENT.get().defaultBlockState())
    ));

    public GreatDoorStructure(ArrayList<StructurePiece> structurePieces) {
        super(structurePieces);
    }

    public static GreatDoorStructure of(StructurePiece... pieces) {
        return new GreatDoorStructure(new ArrayList<>(List.of(pieces)));
    }

    @Override
    public void place(BlockPlaceContext context) {
        if (context.getPlayer() != null) {
            rotatedPieces(context.getHorizontalDirection().getAxis())
                    .forEach(s -> s.place(context.getClickedPos(), context.getLevel(), s.state.setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())));
        } else {
            rotatedPieces(context.getClickedFace().getAxis())
                    .forEach(s -> s.place(context.getClickedPos(), context.getLevel(), s.state.setValue(BlockStateProperties.HORIZONTAL_FACING, context.getClickedFace())));
        }
    }

    @Override
    public boolean canPlace(BlockPlaceContext context) {
        if (context.getPlayer() != null) {
            return rotatedPieces(context.getHorizontalDirection().getAxis())
                    .stream().allMatch(p -> p.canPlace(context));
        } else {
            return rotatedPieces(context.getClickedFace().getAxis())
                    .stream().allMatch(p -> p.canPlace(context));
        }
    }

    public ArrayList<StructurePiece> rotatedPieces(Direction.Axis axis) {
        if (axis == Direction.Axis.Z) {
            return structurePieces;
        } else {
            return new ArrayList<>(structurePieces.stream()
                    .map(piece -> new MultiBlockStructure.StructurePiece(0, piece.offset.getY(), piece.offset.getX(), piece.state))
                    .toList());
        }
    }
}
