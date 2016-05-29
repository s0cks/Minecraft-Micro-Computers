package mmc.client.render.entity.model.tungsten;

import mmc.api.robit.IRobitPart;
import mmc.client.render.entity.model.ModularRobitModel;
import mmc.common.MMC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public final class TungstenRightArm
extends ModelBase
implements IRobitPart{
  private final ModelRenderer rightAShoulder;
  private final ModelRenderer rightArm;
  private final ModelRenderer rightADecoration1;
  private final ModelRenderer rightADecoration2;

  public TungstenRightArm() {
    this.textureWidth = 64;
    this.textureHeight = 32;
    this.rightAShoulder = new ModelRenderer(this, 0, 18);
    this.rightAShoulder.mirror = true;
    this.rightAShoulder.setRotationPoint(1.3F, 12.0F, 0.0F);
    this.rightAShoulder.addBox(0.0F, -1.5F, -1.5F, 4, 3, 3, 0.0F);
    this.rightADecoration1 = new ModelRenderer(this, 22, 18);
    this.rightADecoration1.mirror = true;
    this.rightADecoration1.setRotationPoint(4.0F, 16.0F, 0.0F);
    this.rightADecoration1.addBox(0.0F, 0.0F, -1.0F, 1, 2, 2, 0.2F);
    this.rightArm = new ModelRenderer(this, 14, 18);
    this.rightArm.mirror = true;
    this.rightArm.setRotationPoint(4.0F, 12.5F, 0.0F);
    this.rightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
    this.rightADecoration2 = new ModelRenderer(this, 28, 18);
    this.rightADecoration2.mirror = true;
    this.rightADecoration2.setRotationPoint(4.0F, 14.0F, 0.0F);
    this.rightADecoration2.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.2F);
  }

  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    MMC.proxy.client().renderEngine.bindTexture(ModularRobitModel.TUNGSTEN_TEX);
    this.rightAShoulder.render(f5);
    this.rightADecoration1.render(f5);
    this.rightArm.render(f5);
    this.rightADecoration2.render(f5);
  }

  /**
   * This is a helper function from Tabula to set the rotation of model parts
   */
  public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }

  @Override
  public String id() {
    return "tungsten_rarm";
  }

  @Override
  public Type type() {
    return Type.R_ARM;
  }
}