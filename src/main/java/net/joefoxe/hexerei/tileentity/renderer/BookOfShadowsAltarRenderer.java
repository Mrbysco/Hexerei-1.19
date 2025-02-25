package net.joefoxe.hexerei.tileentity.renderer;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Axis;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.data.books.HexereiBookItem;
import net.joefoxe.hexerei.tileentity.BookOfShadowsAltarTile;
import net.joefoxe.hexerei.util.HexereiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.Supplier;

public class BookOfShadowsAltarRenderer implements BlockEntityRenderer<BookOfShadowsAltarTile> {

    private final Minecraft minecraft = Minecraft.getInstance();
    private final ItemRenderer itemRenderer = minecraft.getItemRenderer();

    public static double getDistanceToEntity(Entity entity, BlockPos pos) {
        double deltaX = entity.getX() - pos.getX();
        double deltaY = entity.getY() - pos.getY();
        double deltaZ = entity.getZ() - pos.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }



    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(BookOfShadowsAltarTile tileEntityIn, float partialTicks, PoseStack matrixStackIn,
                       MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {





        if(!tileEntityIn.getLevel().getBlockState(tileEntityIn.getBlockPos()).hasBlockEntity() || !(tileEntityIn.getLevel().getBlockEntity(tileEntityIn.getBlockPos()) instanceof BookOfShadowsAltarTile))
            return;


        ItemStack stack = tileEntityIn.itemHandler.getStackInSlot(0);

        if(stack.getItem() instanceof HexereiBookItem){
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

//            Lighting.setupForFlatItems();
            if (tileEntityIn.degreesOpenedRender != 90) {
                try {
                    tileEntityIn.drawing.drawPages(tileEntityIn, matrixStackIn, buffer, combinedLightIn, combinedOverlayIn, partialTicks);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }

//            Lighting.setupFor3DItems();
            matrixStackIn.pushPose();
            matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
            matrixStackIn.translate((float) Math.sin((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f), 0f / 16f, (float) Math.cos((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f));
            matrixStackIn.translate((float) Math.sin((tileEntityIn.degreesSpunRender + 90f) / 57.1f) / 32f, 0f / 16f, (float) Math.cos((tileEntityIn.degreesSpunRender + 90f) / 57.1f) / 32f);
            matrixStackIn.translate(0, -((tileEntityIn.degreesFloppedRender / 90)) / 16f, 0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(tileEntityIn.degreesSpunRender));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-(tileEntityIn.degreesOpenedRender / 2 + 45)));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(tileEntityIn.degreesOpenedRender));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-tileEntityIn.degreesFloppedRender));
            matrixStackIn.translate(0, 0, -(tileEntityIn.degreesFloppedRender / 10f) / 32);
            matrixStackIn.translate(0, (-0.5f * (tileEntityIn.degreesFloppedRender / 90)) / 16f, (float) Math.sin((tileEntityIn.degreesFloppedRender) / 57.1f) / 32f);
            DyeColor col = HexereiUtil.getDyeColorNamed(stack.getHoverName().getString());
            renderBlock(matrixStackIn, buffer, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_COVER.get().defaultBlockState(), HexereiBookItem.getColor2(stack));
            renderBlock(matrixStackIn, buffer, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_COVER_CORNERS.get().defaultBlockState(), col == null ? HexereiBookItem.getColor1(stack) : HexereiUtil.getColorValue(col));
            matrixStackIn.popPose();

            matrixStackIn.pushPose();
            matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
            matrixStackIn.translate((float) Math.sin((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f), 0f / 16f, (float) Math.cos((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f));
            matrixStackIn.translate(-(float) Math.sin((tileEntityIn.degreesSpunRender + 90f) / 57.1f) / 32f, 0f / 16f, -(float) Math.cos((tileEntityIn.degreesSpunRender + 90f) / 57.1f) / 32f);
            matrixStackIn.translate(0, -((tileEntityIn.degreesFloppedRender / 90)) / 16f, 0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(tileEntityIn.degreesSpunRender));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-(tileEntityIn.degreesOpenedRender / 2 + 45)));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-tileEntityIn.degreesOpenedRender));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(tileEntityIn.degreesFloppedRender));
            matrixStackIn.translate(0, 0, -(tileEntityIn.degreesFloppedRender / 10f) / 32);
            matrixStackIn.translate(0, (-0.5f * (tileEntityIn.degreesFloppedRender / 90)) / 16f, -(float) Math.sin((tileEntityIn.degreesFloppedRender) / 57.1f) / 32f);
            renderBlock(matrixStackIn, buffer, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_BACK.get().defaultBlockState(), HexereiBookItem.getColor2(stack));
            renderBlock(matrixStackIn, buffer, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_BACK_CORNERS.get().defaultBlockState(), col == null ? HexereiBookItem.getColor1(stack) : HexereiUtil.getColorValue(col));
            matrixStackIn.popPose();

            matrixStackIn.pushPose();
            matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
            matrixStackIn.translate((float) Math.sin((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f), 0f / 16f, (float) Math.cos((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f));
            matrixStackIn.translate(0, -((tileEntityIn.degreesFloppedRender / 90)) / 16f, 0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(tileEntityIn.degreesSpunRender));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-(tileEntityIn.degreesOpenedRender / 2 + 45)));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-tileEntityIn.degreesFloppedRender));
            matrixStackIn.translate(0, 0, -(tileEntityIn.degreesFloppedRender / 10f) / 32);
            renderBlock(matrixStackIn, buffer, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_BINDING.get().defaultBlockState(), HexereiBookItem.getColor2(stack));
            matrixStackIn.popPose();

            if(tileEntityIn.degreesFloppedRender != 90){
                matrixStackIn.pushPose();
                matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
                matrixStackIn.translate((float) Math.sin((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f), 0f / 16f, (float) Math.cos((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f));
                matrixStackIn.translate(0, -((tileEntityIn.degreesFloppedRender / 90)) / 16f, 0);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(tileEntityIn.degreesSpunRender));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(-(tileEntityIn.degreesOpenedRender / 2 + 45)));
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-tileEntityIn.degreesFloppedRender));
                matrixStackIn.translate(0, 0, -(tileEntityIn.degreesFloppedRender / 10f) / 32);
                matrixStackIn.translate(0, 1f / 32f, 0);
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees((80f - tileEntityIn.degreesOpenedRender / 1.12f)));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(((80f - tileEntityIn.degreesOpenedRender / 1.12f) / 90f) * (-tileEntityIn.pageOneRotationRender)));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(((80f - tileEntityIn.degreesOpenedRender / 1.12f) / 90f) * (tileEntityIn.pageTwoRotationRender / 16f)));
                renderBlock(matrixStackIn, buffer, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_PAGE.get().defaultBlockState());
                matrixStackIn.popPose();
            }

            if(tileEntityIn.turnPage == 1 || tileEntityIn.turnPage == -1){
                matrixStackIn.pushPose();
                matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
                matrixStackIn.translate((float) Math.sin((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f), 0f / 16f, (float) Math.cos((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f));
                matrixStackIn.translate(0, -((tileEntityIn.degreesFloppedRender / 90)) / 16f, 0);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(tileEntityIn.degreesSpunRender));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(-(tileEntityIn.degreesOpenedRender / 2 + 45)));
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-tileEntityIn.degreesFloppedRender));
                matrixStackIn.translate(0, 0, -(tileEntityIn.degreesFloppedRender / 10f) / 32);
                matrixStackIn.translate(0, 1f / 32f, 0);
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees((80f - tileEntityIn.degreesOpenedRender / 1.12f)));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(((80f - tileEntityIn.degreesOpenedRender / 1.12f) / 90f) * (-tileEntityIn.pageOneRotationRender / 16f + 180/16f)));
                renderBlock(matrixStackIn, buffer, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_PAGE.get().defaultBlockState());
                matrixStackIn.popPose();
            }

            if(tileEntityIn.degreesFloppedRender != 90){
                matrixStackIn.pushPose();
                matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
                matrixStackIn.translate((float) Math.sin((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f), 0f / 16f, (float) Math.cos((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f));
                matrixStackIn.translate(0, -((tileEntityIn.degreesFloppedRender / 90)) / 16f, 0);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(tileEntityIn.degreesSpunRender));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(-(tileEntityIn.degreesOpenedRender / 2 + 45)));
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-tileEntityIn.degreesFloppedRender));
                matrixStackIn.translate(0, 0, -(tileEntityIn.degreesFloppedRender / 10f) / 32);
                matrixStackIn.translate(0, 1f / 32f, 0);
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-(80f - tileEntityIn.degreesOpenedRender / 1.12f)));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees((-(80f - tileEntityIn.degreesOpenedRender / 1.12f) / 90f)*(-tileEntityIn.pageTwoRotationRender)));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees((-(80f - tileEntityIn.degreesOpenedRender / 1.12f) / 90f)*(tileEntityIn.pageOneRotationRender / 16f)));
                renderBlock(matrixStackIn, buffer, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_PAGE.get().defaultBlockState());
                matrixStackIn.popPose();
            }

            if(tileEntityIn.turnPage == 2 || tileEntityIn.turnPage == -1){
                matrixStackIn.pushPose();
                matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
                matrixStackIn.translate((float) Math.sin((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f), 0f / 16f, (float) Math.cos((tileEntityIn.degreesSpunRender) / 57.1f) / 32f * (tileEntityIn.degreesOpenedRender / 5f - 12f));
                matrixStackIn.translate(0, -((tileEntityIn.degreesFloppedRender / 90)) / 16f, 0);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(tileEntityIn.degreesSpunRender));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(-(tileEntityIn.degreesOpenedRender / 2 + 45)));
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-tileEntityIn.degreesFloppedRender));
                matrixStackIn.translate(0, 0, -(tileEntityIn.degreesFloppedRender / 10f) / 32);
                matrixStackIn.translate(0, 1f / 32f, 0);
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-(80f - tileEntityIn.degreesOpenedRender / 1.12f)));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees((-(80f - tileEntityIn.degreesOpenedRender / 1.12f) / 90f) * (-tileEntityIn.pageTwoRotationRender / 16f + 180/16f)));
                renderBlock(matrixStackIn, buffer, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_PAGE.get().defaultBlockState());
                matrixStackIn.popPose();
            }
            CompoundTag tag = stack.getOrCreateTag();

            if(tag.getBoolean("opened") || tileEntityIn.degreesFlopped != 90) {
                if(tileEntityIn.degreesFlopped == 0)
                    tileEntityIn.degreesSpunRender = tileEntityIn.drawing.moveToAngle(tileEntityIn.degreesSpun, tileEntityIn.degreesSpunTo, tileEntityIn.degreesSpunSpeed * partialTicks);
                tileEntityIn.buttonScaleRender = tileEntityIn.drawing.moveToAngle(tileEntityIn.buttonScale, tileEntityIn.buttonScaleTo, tileEntityIn.buttonScaleSpeed * partialTicks);
                tileEntityIn.degreesOpenedRender = tileEntityIn.drawing.moveToAngle(tileEntityIn.degreesOpened, tileEntityIn.degreesOpenedTo, tileEntityIn.degreesOpenedSpeed * partialTicks);
                tileEntityIn.degreesFloppedRender = tileEntityIn.drawing.moveToAngle(tileEntityIn.degreesFlopped, tileEntityIn.degreesFloppedTo, tileEntityIn.degreesFloppedSpeed * partialTicks);
            }
            buffer.endBatch();
            if (tileEntityIn.degreesOpenedRender != 90) {
                try {
                    buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                    tileEntityIn.drawing.drawTooltips(tileEntityIn, matrixStackIn, buffer, combinedLightIn, combinedOverlayIn, partialTicks);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }

        } else if(!stack.isEmpty()){

            FlowerPotBlock block = (FlowerPotBlock)Blocks.FLOWER_POT;
            Map<ResourceLocation, Supplier<? extends Block>> map = block.getFullPotsView();

            ResourceLocation loc = ForgeRegistries.ITEMS.getKey(stack.getItem());
            BlockState blockState = map.getOrDefault(loc,() -> Blocks.AIR).get().defaultBlockState();

            if(!blockState.isAir()) {

                matrixStackIn.pushPose();

                matrixStackIn.translate(3f / 16f, 16.25f / 16f, 3f / 16f);
                matrixStackIn.translate(Math.cos(blockState.getBlock().toString().length() * 14f) / 4f, 0, Math.sin(blockState.getBlock().toString().length() * 14f) / 4f);
                matrixStackIn.scale(0.65f, 0.65f, 0.65f);
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, blockState);

                matrixStackIn.popPose();
            } else {

                BakedModel itemModel = itemRenderer.getModel(stack, tileEntityIn.getLevel(), null, 0);

                boolean is3dModel = itemModel.isGui3d();
                if (stack.getItem() instanceof BlockItem blockItem && is3dModel) {
                    matrixStackIn.translate(8f / 16f, 18.75f / 16f, 8f / 16f);
                    matrixStackIn.scale(0.65f, 0.65f, 0.65f);
                    renderItem(stack, tileEntityIn.getLevel(), matrixStackIn, bufferIn, combinedLightIn);
                } else {
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(8f / 16f, 16.25f / 16f, 8f / 16f);
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(90));
                    matrixStackIn.scale(0.45f, 0.45f, 0.45f);
                    renderItem(stack, tileEntityIn.getLevel(), matrixStackIn, bufferIn, combinedLightIn);
                    matrixStackIn.popPose();
                }
            }
        }


    }

    private void renderItem(ItemStack stack, Level level, PoseStack matrixStackIn, MultiBufferSource bufferIn,
                            int combinedLightIn) {
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, level, 1);
    }

    private void renderBlock(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, BlockState state) {
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);

    }

    private void renderBlock(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, BlockState state, int color) {
        renderSingleBlock(state, matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, color);

    }

    public void renderSingleBlock(BlockState p_110913_, PoseStack p_110914_, MultiBufferSource p_110915_, int p_110916_, int p_110917_, ModelData modelData, int color) {
        RenderShape rendershape = p_110913_.getRenderShape();
        if (rendershape != RenderShape.INVISIBLE) {
            switch (rendershape) {
                case MODEL -> {
                    BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
                    BakedModel bakedmodel = dispatcher.getBlockModel(p_110913_);
                    int i = color;
                    float f = (float) (i >> 16 & 255) / 255.0F;
                    float f1 = (float) (i >> 8 & 255) / 255.0F;
                    float f2 = (float) (i & 255) / 255.0F;
                    dispatcher.getModelRenderer().renderModel(p_110914_.last(), p_110915_.getBuffer(ItemBlockRenderTypes.getRenderType(p_110913_, false)), p_110913_, bakedmodel, f, f1, f2, p_110916_, p_110917_, modelData, null);
                }
                case ENTITYBLOCK_ANIMATED -> {
                    ItemStack stack = new ItemStack(p_110913_.getBlock());
                    IClientItemExtensions.of(stack.getItem()).getCustomRenderer().renderByItem(stack, ItemDisplayContext.NONE, p_110914_, p_110915_, p_110916_, p_110917_);
                }
            }

        }
    }


}
