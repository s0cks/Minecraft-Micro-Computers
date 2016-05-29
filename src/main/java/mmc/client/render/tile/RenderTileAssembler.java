package mmc.client.render.tile;

import dorkbox.tweenengine.Timeline;
import dorkbox.tweenengine.Tween;
import dorkbox.tweenengine.TweenAccessor;
import dorkbox.tweenengine.TweenEquations;
import dorkbox.tweenengine.TweenManager;
import mmc.client.ClientEventHandler;
import mmc.client.ShaderHelper;
import mmc.client.render.entity.RenderEntityRobit;
import mmc.common.MMC;
import mmc.common.entity.EntityRobit;
import mmc.common.tile.TileEntityAssembler;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public final class RenderTileAssembler
extends TileEntitySpecialRenderer<TileEntityAssembler>
implements TweenAccessor<RenderTileAssembler> {
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

  private static final byte COLOR_R = 0x0;
  private static final byte COLOR_G = 0x1;
  private static final byte COLOR_B = 0x2;
  private static final byte LEVITATION = 0x3;
  private static final byte DIVERGENCE = 0x4;

  private int light = -1;

  private long lastSysTime = 0L;

  private float red = 1.0F;
  private float green = 0.0F;
  private float blue = 0.21568627451F;
  private float levitation = 0.3F;
  private float divergence = 0.0F;
  private volatile TileEntityAssembler.State state = TileEntityAssembler.State.CONVERGED;

  private final TweenManager manager = new TweenManager();
  private final Timeline pulseTimeline = Timeline.createParallel()
                                                 .push(Tween.to(this, COLOR_R, this, 3)
                                                            .ease(TweenEquations.Linear)
                                                            .target(1.0F))
                                                 .push(Tween.to(this, COLOR_G, this, 3)
                                                            .ease(TweenEquations.Linear)
                                                            .target(0.17647058823F))
                                                 .push(Tween.to(this, COLOR_B, this, 3)
                                                            .ease(TweenEquations.Linear)
                                                            .target(0.6F))
                                                 .repeatAutoReverse(-1, 0.0F);
  private final Timeline levitationTimeline = Timeline.createSequence()
                                                      .push(Tween.to(this, LEVITATION, this, 3)
                                                                 .ease(TweenEquations.Quad_InOut)
                                                                 .target(0.5F))
                                                      .push(Tween.to(this, LEVITATION, this, 3)
                                                                 .ease(TweenEquations.Quad_InOut)
                                                                 .target(0.0F))
                                                      .repeatAutoReverse(-1, 0.0F);
  private final EntityRobit robit = new EntityRobit(null);

  public RenderTileAssembler() {
    this.pulseTimeline.start(this.manager)
                      .pause();
    this.levitationTimeline.start(this.manager)
                           .pause();

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent e) {
    if (e.phase == TickEvent.Phase.START) {
      long now = Minecraft.getSystemTime();
      long delta = now - this.lastSysTime;
      this.lastSysTime = now;

      this.manager.update(TimeUnit.MILLISECONDS.toNanos(delta));
    }
  }

  private Timeline createDivergenceTimeline(){
    return Timeline.createSequence()
            .push(Tween.to(this, DIVERGENCE, this, 1)
                       .ease(TweenEquations.Linear)
                       .target(0.2F))
            .setEndCallback(updatedObject -> {
              state = TileEntityAssembler.State.DIVERGED;
            });
  }

  private Timeline createConvergenceTimeline(){
    return Timeline.createSequence()
            .push(Tween.to(this, DIVERGENCE, this, 1)
                       .ease(TweenEquations.Linear)
                       .target(0.0F))
            .setEndCallback(updatedObject -> {
              state = TileEntityAssembler.State.CONVERGED;
            });
  }

  @Override
  public void renderTileEntityAt(TileEntityAssembler te, double x, double y, double z, float partialTicks, int destroyStage) {
    if (this.levitationTimeline.isPaused()) {
      this.levitationTimeline.resume();
      this.pulseTimeline.resume();
    }

    if (te.state() == TileEntityAssembler.State.CONVERGED && this.state != TileEntityAssembler.State.CONVERGED) {
      this.createConvergenceTimeline().start(this.manager);
    } else if (te.state() == TileEntityAssembler.State.DIVERGED && this.state != TileEntityAssembler.State.DIVERGED) {
      this.createDivergenceTimeline().start(this.manager);
    }

    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);
    GlStateManager.disableLighting();

    if (this.light != -1) {
      ARBShaderObjects.glUseProgramObjectARB(this.light);
    } else {
      this.light = ShaderHelper.compile(new ResourceLocation("mmc", "shaders/assembler/light.fsh"), new ResourceLocation("mmc", "shaders/assembler/light.vsh"));
      if (this.light != -1) {
        ARBShaderObjects.glUseProgramObjectARB(this.light);
      }
    }

    GL20.glUniform3f(GL20.glGetUniformLocation(this.light, "color"), this.red, this.green, this.blue);

    this.render(top, topTexture);
    this.render(bottom, bottomTexture);

    if (this.light != -1) {
      ARBShaderObjects.glUseProgramObjectARB(0);
    }

    GlStateManager.enableLighting();

    GlStateManager.pushMatrix();
    GlStateManager.translate(0.5F, -0.35F, 0.5F);
    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
    RenderEntityRobit render = ((RenderEntityRobit) MMC.proxy.client()
                                                             .getRenderManager()
                                                             .getEntityRenderObject(this.robit));
    GlStateManager.translate(0.0F, this.levitation, 0.0F);
    render.doRenderWithDivergence(this.robit, 0.0F, 0.0F, 0.0F, this.divergence);
    GlStateManager.popMatrix();

    GlStateManager.popMatrix();
  }

  private void render(IBakedModel model, ResourceLocation texture) {
    MMC.proxy.client().renderEngine.bindTexture(texture);
    MMC.proxy.client()
             .getBlockRendererDispatcher()
             .getBlockModelRenderer()
             .renderModelBrightnessColor(model, 1.0F, 1.0F, 1.0F, 1.0F);
  }

  @Override
  public int getValues(RenderTileAssembler target, int tweenType, float[] returnValues) {
    switch (tweenType) {
      case COLOR_R:
        returnValues[0] = this.red;
        break;
      case COLOR_G:
        returnValues[0] = this.green;
        break;
      case COLOR_B:
        returnValues[0] = this.blue;
        break;
      case LEVITATION:
        returnValues[0] = this.levitation;
        break;
      case DIVERGENCE:
        returnValues[0] = this.divergence;
        break;
    }
    return 1;
  }

  @Override
  public void setValues(RenderTileAssembler target, int tweenType, float[] newValues) {
    switch (tweenType) {
      case COLOR_R:
        this.red = newValues[0];
        break;
      case COLOR_G:
        this.green = newValues[0];
        break;
      case COLOR_B:
        this.blue = newValues[0];
        break;
      case LEVITATION:
        this.levitation = newValues[0];
        break;
      case DIVERGENCE:
        this.divergence = newValues[0];
        break;
    }
  }
}