package net.joefoxe.hexerei.block.connected;


import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// CREDIT: https://github.com/Creators-of-Create/Create/tree/mc1.19/dev by simibubi & team
// Under MIT-License: https://github.com/Creators-of-Create/Create/blob/mc1.19/dev/LICENSE
// edits by JoeFoxe

public class CTModel extends BakedModelWrapperWithData {

    private static final ModelProperty<CTData> CT_PROPERTY = new ModelProperty<>();

    private final ConnectedTextureBehaviour behaviour;

    public CTModel(BakedModel originalModel, ConnectedTextureBehaviour behaviour) {
        super(originalModel);
        this.behaviour = behaviour;
    }

    @Override
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                                ModelData blockEntityData) {
        return builder.with(CT_PROPERTY, createCTData(world, pos, state));
    }

    protected CTData createCTData(BlockAndTintGetter world, BlockPos pos, BlockState state) {
        CTData data = new CTData();
        MutableBlockPos mutablePos = new MutableBlockPos();
        for (Direction face : Direction.values()) {
            BlockState actualState = world.getBlockState(pos);
            if (!behaviour.buildContextForOccludedDirections()
                    && !Block.shouldRenderFace(state, world, pos, face, mutablePos.setWithOffset(pos, face))
//                    && !(actualState.getBlock()instanceof CopycatBlock ufb
//                    && !ufb.canFaceBeOccluded(actualState, face))
            )
                continue;
            CTType dataType = behaviour.getDataType(world, pos, state, face);
            if (dataType == null)
                continue;
            ConnectedTextureBehaviour.CTContext context = behaviour.buildContext(world, pos, state, face, dataType.getContextRequirement());
            data.put(face, dataType.getTextureIndex(context));
        }
        return data;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, extraData, renderType);
        if (!extraData.has(CT_PROPERTY))
            return quads;

        CTData data = extraData.get(CT_PROPERTY);
        quads = new ArrayList<>(quads);

        for (int i = 0; i < quads.size(); i++) {
            BakedQuad quad = quads.get(i);

            int index = data.get(quad.getDirection());
            if (index == -1)
                continue;

            CTSpriteShiftEntry spriteShift = behaviour.getShift(state, quad.getDirection(), quad.getSprite());
            boolean hasTransparent = behaviour instanceof ConnectedTextureTransparentLayer;


            if (spriteShift == null)
                continue;
            if (!(quad.getSprite() == spriteShift.getOriginal() || hasTransparent))
                continue;
            if (behaviour instanceof ConnectedTextureTransparentLayer transparentLayer) {
                CTSpriteShiftEntry transparentShift = transparentLayer.getTransparentShift(state, quad.getDirection(), quad.getSprite());
                if (transparentShift == null)
                    continue;
                if (quad.getSprite() == transparentShift.getOriginal())
                    spriteShift = transparentShift;
                else if (quad.getSprite() != spriteShift.getOriginal())
                    continue;
            }

            BakedQuad newQuad = BakedQuadHelper.clone(quad);
            int[] vertexData = newQuad.getVertices();

            for (int vertex = 0; vertex < 4; vertex++) {
                float u = BakedQuadHelper.getU(vertexData, vertex);
                float v = BakedQuadHelper.getV(vertexData, vertex);
                BakedQuadHelper.setU(vertexData, vertex, spriteShift.getTargetU(u, index));
                BakedQuadHelper.setV(vertexData, vertex, spriteShift.getTargetV(v, index));
            }

            quads.set(i, newQuad);
        }

        return quads;
    }

    private static class CTData {
        private final int[] indices;

        public CTData() {
            indices = new int[6];
            Arrays.fill(indices, -1);
        }

        public void put(Direction face, int texture) {
            indices[face.get3DDataValue()] = texture;
        }

        public int get(Direction face) {
            return indices[face.get3DDataValue()];
        }
    }

}