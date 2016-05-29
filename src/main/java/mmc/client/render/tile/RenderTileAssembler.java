package mmc.client.render.tile;

import mmc.client.ClientEventHandler;
import mmc.common.MMC;
import mmc.common.tile.TileEntityAssembler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.io.InputStream;
import java.io.InputStreamReader;

public final class RenderTileAssembler
extends TileEntitySpecialRenderer<TileEntityAssembler> {
  private static final FaceBakery faceBarkery = new FaceBakery();

  private static IBakedModel create(String side, TextureAtlasSprite textureSprite) {
    try (InputStream in = MMC.proxy.client()
                                   .getResourceManager()
                                   .getResource(new ResourceLocation("mmc", "models/block/robit_assembler_" + side + ".json"))
                                   .getInputStream()) {
      ModelBlock model = ModelBlock.deserialize(new InputStreamReader(in));
      SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(model, model.createOverrides()).setTexture(textureSprite);
      for (BlockPart part : model.getElements()) {
        for (EnumFacing facing : part.mapFaces.keySet()) {
          BlockPartFace face = part.mapFaces.get(facing);
          if (face.cullFace == null) {
            builder.addGeneralQuad(faceBarkery.makeBakedQuad(part.positionFrom, part.positionTo, face, textureSprite, facing, ModelRotation.X0_Y0, part.partRotation, false, part.shade));
          } else {
            builder.addFaceQuad(ModelRotation.X0_Y0.rotate(face.cullFace), faceBarkery.makeBakedQuad(part.positionFrom, part.positionTo, face, textureSprite, facing, ModelRotation.X0_Y0, part.partRotation, false, part.shade));
          }
        }
      }
      return builder.makeBakedModel();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static final IBakedModel top = create("top", ClientEventHandler.instance()
                                                                         .assemblerTop());
  private static final IBakedModel bottom = create("bottom", ClientEventHandler.instance()
                                                                               .assemblerBottom());

  private static final ResourceLocation topTexture = new ResourceLocation("mmc", "textures/models/assembler_top.png");
  private static final ResourceLocation bottomTexture = new ResourceLocation("mmc", "textures/models/assembler_bottom.png");

  @Override
  public void renderTileEntityAt(TileEntityAssembler te, double x, double y, double z, float partialTicks, int destroyStage) {
    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);
    this.render(top, topTexture);
    this.render(bottom, bottomTexture);
    GlStateManager.popMatrix();
  }

  private void render(IBakedModel model, ResourceLocation texture) {
    MMC.proxy.client().renderEngine.bindTexture(texture);
    MMC.proxy.client()
             .getBlockRendererDispatcher()
             .getBlockModelRenderer()
             .renderModelBrightnessColor(model, 1.0F, 1.0F, 1.0F, 1.0F);
  }
}