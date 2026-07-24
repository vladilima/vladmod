package com.vladilima.vladmod.blocks.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vladilima.vladmod.blocks.entity.DarknessBlockEntity;
import com.vladilima.vladmod.registries.ModRenderTypes;
import com.vladilima.vladmod.registries.ModShaders;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class DarknessBlockEntityRenderer implements BlockEntityRenderer<DarknessBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public DarknessBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(DarknessBlockEntity blockEntity, float partialTick, PoseStack poseStack,
               MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Matrix4f pose = poseStack.last().pose();

        for (Direction direction : Direction.values()) {
            if (!isTouchingDarknessBlock(blockEntity, direction)) {
                renderFaceOutline(blockEntity, pose, bufferSource, direction);
            }
        }
    }

    private void renderFaceOutline(DarknessBlockEntity blockEntity, Matrix4f pose, MultiBufferSource bufferSource, Direction direction) {
        BlockPos adjacent = blockEntity.getBlockPos().relative(direction);
        if (!blockEntity.getLevel().getBlockState(adjacent).is(BlockTags.DOORS)) {
            Vector3f[] vertices = FACE_VERTICES.get(direction);
            Direction[] vertexDirs = VERTEX_DIRECTIONS.get(direction);

            // Render outer face (border)
            for (int i = vertices.length - 1; i >= 0; i--) {
                Vector3f vertex = new Vector3f(vertices[i]);
                vertex.mul(0.999f);
                vertex.add(0.0005f, 0.0005f, 0.0005f);

                VertexConsumer consumer = bufferSource.getBuffer(
                        ModRenderTypes.darknessBlock(ModShaders::getDarknessBlockShader)
                );

                addVertex(pose, consumer, vertex, true, i);
            }

            // Render inner face
            for (int i = 0; i < vertices.length; i++) {
                Direction dir1 = vertexDirs[i];
                Direction dir2 = vertexDirs[(i + 1) % 4];

                VertexConsumer consumer = bufferSource.getBuffer(
                        RenderType.debugQuads()
                );

                int[] innerPos = positionInnerVertex(blockEntity, direction, dir1, dir2);

                Vector3f vertexCoords = new Vector3f(vertices[i]);
                vertexCoords.sub(direction.getNormal().getX() * 1f / 16f,
                        direction.getNormal().getY() * 1f / 16f,
                        direction.getNormal().getZ() * 1f / 16f);
                vertexCoords.add(dir1.getNormal().getX() * innerPos[0] / 16f,
                        dir1.getNormal().getY() * innerPos[0] / 16f,
                        dir1.getNormal().getZ() * innerPos[0] / 16f);
                vertexCoords.add(dir2.getNormal().getX() * innerPos[1] / 16f,
                        dir2.getNormal().getY() * innerPos[1] / 16f,
                        dir2.getNormal().getZ() * innerPos[1] / 16f);

                addVertex(pose, consumer, vertexCoords, false, 0);
            }
        }

    }

    private int[] positionInnerVertex(DarknessBlockEntity blockEntity, Direction faceDir, Direction dir1, Direction dir2) {
        boolean touching1 = isTouchingDarknessBlock(blockEntity, dir1);
        boolean touching2 = isTouchingDarknessBlock(blockEntity, dir2);

        if (!touching1 && !touching2) return new int[]{-1, -1};
        if (!touching1 && touching2) return new int[]{-1, 1};
        if (touching1 && !touching2) return new int[]{1, -1};
        if (touching1 && touching2) return positionCornerVertex(blockEntity, faceDir, dir1, dir2);

        return new int[]{-1, -1};
    }

    private int[] positionCornerVertex(DarknessBlockEntity blockEntity, Direction faceDir, Direction dir1, Direction dir2) {
        boolean cornerTouching1 = isTouchingDarknessBlock(blockEntity, dir1) &&
                checkDiagonalBlock(blockEntity, dir1, faceDir);
        boolean cornerTouching2 = isTouchingDarknessBlock(blockEntity, dir2) &&
                checkDiagonalBlock(blockEntity, dir2, faceDir);

        if (!cornerTouching1 && !cornerTouching2) return new int[]{0, 0};
        if (!cornerTouching1 && cornerTouching2) return new int[]{0, 1};
        if (cornerTouching1 && !cornerTouching2) return new int[]{1, 0};
        return new int[]{1, 1};
    }

    private boolean checkDiagonalBlock(DarknessBlockEntity blockEntity, Direction blockDir, Direction faceDir) {
        if (blockEntity.getLevel() == null) return false;

        BlockEntity adjacent = blockEntity.getLevel()
                .getBlockEntity(blockEntity.getBlockPos().relative(blockDir));

        if (isDarknessBlock(adjacent)) {
            return isTouchingDarknessBlock((DarknessBlockEntity) adjacent, faceDir);
        }
        return false;
    }

    private void addVertex(Matrix4f pose, VertexConsumer consumer, Vector3f position, boolean isBorder, int uvIndex) {
        int borderChannel = isBorder ? 255 : 0;
        int middleChannel = isBorder ? 0 : 255;

        float u = (uvIndex % 2) * 1f;
        float v = (((float) uvIndex / 2) % 2);

        consumer.addVertex(pose, position.x, position.y, position.z)
                .setColor(borderChannel, 0, 0, middleChannel)
                .setUv(u, v)
                .setLight(0xF000F0)
                .setNormal(0, 0, 0);
    }


    private boolean isTouchingDarknessBlock(DarknessBlockEntity blockEntity, Direction direction) {
        // Check if adjacent block in this direction is an outline block
        if (blockEntity.getLevel() == null) return false;

        BlockEntity adjacent = blockEntity.getLevel()
                .getBlockEntity(blockEntity.getBlockPos().relative(direction));

        return isDarknessBlock(adjacent);
    }

    private boolean isDarknessBlock(BlockEntity blockEntity) {
        return blockEntity instanceof DarknessBlockEntity;
    }

    private static final Map<Direction, Vector3f[]> FACE_VERTICES = new HashMap<>();
    private static final Map<Direction, Direction[]> VERTEX_DIRECTIONS = new HashMap<>();
    static {
        FACE_VERTICES.put(Direction.DOWN, new Vector3f[]{
                new Vector3f(0f, 0f, 0f), new Vector3f(1f, 0f, 0f),
                new Vector3f(1f, 0f, 1f), new Vector3f(0f, 0f, 1f)
        });
        FACE_VERTICES.put(Direction.UP, new Vector3f[]{
                new Vector3f(0f, 1f, 0f), new Vector3f(0f, 1f, 1f),
                new Vector3f(1f, 1f, 1f), new Vector3f(1f, 1f, 0f)
        });
        FACE_VERTICES.put(Direction.NORTH, new Vector3f[]{
                new Vector3f(0f, 0f, 0f), new Vector3f(0f, 1f, 0f),
                new Vector3f(1f, 1f, 0f), new Vector3f(1f, 0f, 0f)
        });
        FACE_VERTICES.put(Direction.SOUTH, new Vector3f[]{
                new Vector3f(0f, 0f, 1f), new Vector3f(1f, 0f, 1f),
                new Vector3f(1f, 1f, 1f), new Vector3f(0f, 1f, 1f)
        });
        FACE_VERTICES.put(Direction.WEST, new Vector3f[]{
                new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 1f),
                new Vector3f(0f, 1f, 1f), new Vector3f(0f, 1f, 0f)
        });
        FACE_VERTICES.put(Direction.EAST, new Vector3f[]{
                new Vector3f(1f, 0f, 0f), new Vector3f(1f, 1f, 0f),
                new Vector3f(1f, 1f, 1f), new Vector3f(1f, 0f, 1f)
        });

        VERTEX_DIRECTIONS.put(Direction.DOWN, new Direction[]{
                Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH
        });
        VERTEX_DIRECTIONS.put(Direction.UP, new Direction[]{
                Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST
        });
        VERTEX_DIRECTIONS.put(Direction.NORTH, new Direction[]{
                Direction.DOWN, Direction.WEST, Direction.UP, Direction.EAST
        });
        VERTEX_DIRECTIONS.put(Direction.SOUTH, new Direction[]{
                Direction.WEST, Direction.DOWN, Direction.EAST, Direction.UP
        });
        VERTEX_DIRECTIONS.put(Direction.WEST, new Direction[]{
                Direction.NORTH, Direction.DOWN, Direction.SOUTH, Direction.UP
        });
        VERTEX_DIRECTIONS.put(Direction.EAST, new Direction[]{
                Direction.DOWN, Direction.NORTH, Direction.UP, Direction.SOUTH
        });
    }
}
