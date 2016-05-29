package mmc.client.render.entity.model.rosegold;

import mmc.api.robit.IRobitPart;
import mmc.client.render.entity.model.ModularRobitModel;
import mmc.common.MMC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public final class RoseGoldLeftArm
extends ModelBase
implements IRobitPart{
  private final ModelRenderer leftAShoulder;
  private final ModelRenderer leftArm;
  private final ModelRenderer leftADecoration1;
  private final ModelRenderer leftADecoration2;

  public RoseGoldLeftArm(){
    this.textureWidth = 64;
    this.textureHeight = 32;
    this.leftArm = new ModelRenderer(this, 8, 14);
    this.leftArm.setRotationPoint(-2.8F, 13.0F, 0.0F);
    this.leftArm.addBox(-1.0F, 0.0F, -1.0F, 1, 5, 2, 0.0F);
    this.leftAShoulder = new ModelRenderer(this, 0, 14);
    this.leftAShoulder.setRotationPoint(-2.0F, 12.0F, 0.0F);
    this.leftAShoulder.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, 0.1F);
    this.leftADecoration2 = new ModelRenderer(this, 20, 14);
    this.leftADecoration2.setRotationPoint(-2.8F, 13.2F, 0.0F);
    this.leftADecoration2.addBox(-1.0F, 0.0F, -1.0F, 1, 1, 2, 0.1F);
    this.leftADecoration1 = new ModelRenderer(this, 14, 14);
    this.leftADecoration1.setRotationPoint(-2.8F, 15.0F, 0.0F);
    this.leftADecoration1.addBox(-1.0F, 0.0F, -1.0F, 1, 2, 2, 0.1F);
  }

  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    MMC.proxy.client().renderEngine.bindTexture(ModularRobitModel.ROSE_GOLD_TEX);
    this.leftArm.render(f5);
    this.leftAShoulder.render(f5);
    this.leftADecoration2.render(f5);
    this.leftADecoration1.render(f5);
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
    return "rosegold_larm";
  }

  @Override
  public Type type() {
    return Type.L_ARM;
  }
}