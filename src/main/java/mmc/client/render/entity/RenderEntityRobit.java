package mmc.client.render.entity;

import mmc.api.robit.IRobitPart;
import mmc.client.ShaderHelper;
import mmc.client.render.entity.model.ModularRobitModel;
import mmc.common.entity.EntityRobit;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public final class RenderEntityRobit
extends Render<EntityRobit>{
  private static final ModularRobitModel model = ModularRobitModel.TUNGSTEN;

  // Rosey Pink: 1.0F, 0.0F, 0.329411765F
  // Royal Purple: 0.4F, 0.2F, 0.6F
  // Gold: 0.831372549F, 0.6862745098F, 0.21568627451F
  // Dark Green: 0.0F, 0.39215686274F, 0.0F
  // Orange Red: 1.0F, 0.27058823529F, 0.0F
  // Silver: 0.75294117647F, 0.75294117647F, 0.75294117647F
  private static final float[] color = {
  0.831372549F, 0.6862745098F, 0.21568627451F
  };

  private int accentralizer = -1;

  public RenderEntityRobit(RenderManager renderManager) {
    super(renderManager);
  }

  public void doRenderWithDivergence(EntityRobit entity, double x, double y, double z, float divergence){
    GlStateManager.pushMatrix();
    GlStateManager.disableLighting();
    GlStateManager.shadeModel(GL11.GL_SMOOTH);

    if (accentralizer != -1) {
      ARBShaderObjects.glUseProgramObjectARB(accentralizer);
    } else {
      this.accentralizer = ShaderHelper.compile(new ResourceLocation("mmc", "shaders/robit/accents.fsh"), new ResourceLocation("mmc", "shaders/robit/accents.vsh"));
      if (accentralizer != -1) {
        ARBShaderObjects.glUseProgramObjectARB(accentralizer);
      }
    }

    GL20.glUniform3f(GL20.glGetUniformLocation(accentralizer, "accent"), color[0], color[1], color[2]);

    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y + 2.5F, z);
    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
    for(IRobitPart part : model){
      switch(part.type()){
        case L_ARM:{
          GlStateManager.translate(-divergence, 0.0F, 0.0F);
          part.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / 16.0F);
          GlStateManager.translate(divergence, 0.0F, 0.0F);
          break;
        }
        case R_ARM:{
          GlStateManager.translate(divergence, 0.0F, 0.0F);
          part.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / 16.0F);
          GlStateManager.translate(-divergence, 0.0F, 0.0F);
          break;
        }
        case L_LEG:{
          GlStateManager.translate(0.0F, divergence, 0.0F);
          part.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / 16.0F);
          GlStateManager.translate(0.0F, -divergence, 0.0F);
          break;
        }
        case R_LEG:{
          GlStateManager.translate(0.0F, divergence, 0.0F);
          part.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / 16.0F);
          GlStateManager.translate(0.0F, -divergence, 0.0F);
          break;
        }
        default: {
          part.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / 16.0F);
        }
      }
    }
    GlStateManager.popMatrix();

    if (accentralizer != -1) {
      ARBShaderObjects.glUseProgramObjectARB(0);
    }

    GlStateManager.enableLighting();
    GlStateManager.popMatrix();
  }

  @Override
  public void doRender(EntityRobit entity, double x, double y, double z, float entityYaw, float partialTicks) {
    this.doRenderWithDivergence(entity, x, y, z, 0.0F);
  }

  @Override
  protected ResourceLocation getEntityTexture(EntityRobit entity) {
    return null;
  }
}