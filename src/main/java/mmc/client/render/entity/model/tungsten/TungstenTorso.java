package mmc.client.render.entity.model.tungsten;

import mmc.api.robit.IRobitPart;
import mmc.client.render.entity.model.ModularRobitModel;
import mmc.common.MMC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public final class TungstenTorso
extends ModelBase
implements IRobitPart{
  private final ModelRenderer torso1;
  private final ModelRenderer torsoLower;
  private final ModelRenderer torsoUpper;
  private final ModelRenderer torsoSpine;
  private final ModelRenderer torsoSpine2;
  private final ModelRenderer torsoNeck;
  private final ModelRenderer head;
  private final ModelRenderer headJaw;

  public TungstenTorso(){
    this.textureWidth = 64;
    this.textureHeight = 32;
    this.torsoSpine = new ModelRenderer(this, 34, 9);
    this.torsoSpine.setRotationPoint(0.0F, 11.0F, 0.4F);
    this.torsoSpine.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2, 0.0F);
    this.setRotateAngle(torsoSpine, 0.12217304763960307F, 0.0F, 0.0F);
    this.torsoNeck = new ModelRenderer(this, 46, 9);
    this.torsoNeck.setRotationPoint(0.0F, 11.1F, -0.7F);
    this.torsoNeck.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
    this.setRotateAngle(torsoNeck, -0.08726646259971647F, 0.0F, 0.0F);
    this.headJaw = new ModelRenderer(this, 8, 14);
    this.headJaw.setRotationPoint(0.0F, 12.1F, -1.0F);
    this.headJaw.addBox(0.0F, 0.0F, -2.0F, 2, 1, 2, 0.2F);
    this.setRotateAngle(headJaw, 0.0F, 0.8377580409572781F, 0.0F);
    this.head = new ModelRenderer(this, 0, 14);
    this.head.setRotationPoint(0.0F, 12.1F, -1.0F);
    this.head.addBox(0.0F, -1.0F, -2.0F, 2, 2, 2, 0.0F);
    this.setRotateAngle(head, 0.0F, 0.8377580409572781F, 0.0F);
    this.torso1 = new ModelRenderer(this, 0, 9);
    this.torso1.setRotationPoint(0.0F, 17.5F, 0.0F);
    this.torso1.addBox(-0.5F, -1.0F, -1.0F, 1, 2, 2, 0.0F);
    this.torsoUpper = new ModelRenderer(this, 18, 9);
    this.torsoUpper.setRotationPoint(0.0F, 11.5F, -2.0F);
    this.torsoUpper.addBox(-2.5F, 0.0F, 0.0F, 5, 4, 3, 0.0F);
    this.setRotateAngle(torsoUpper, 0.12217304763960307F, 0.0F, 0.0F);
    this.torsoSpine2 = new ModelRenderer(this, 40, 9);
    this.torsoSpine2.setRotationPoint(0.0F, 14.6F, 0.9F);
    this.torsoSpine2.addBox(-0.5F, 0.0F, -1.0F, 1, 3, 2, 0.0F);
    this.setRotateAngle(torsoSpine2, -0.2617993877991494F, 0.0F, 0.0F);
    this.torsoLower = new ModelRenderer(this, 6, 9);
    this.torsoLower.setRotationPoint(0.0F, 14.0F, -0.2F);
    this.torsoLower.addBox(-2.0F, 0.0F, -1.0F, 4, 3, 2, 0.0F);
    this.setRotateAngle(torsoLower, 0.054105206811824215F, 0.0F, 0.0F);
  }

  @Override
  public String id() {
    return "tungsten_torso";
  }

  @Override
  public Type type() {
    return Type.TORSO;
  }

  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    MMC.proxy.client().renderEngine.bindTexture(ModularRobitModel.TUNGSTEN_TEX);
    this.torsoSpine.render(f5);
    this.torsoNeck.render(f5);
    this.headJaw.render(f5);
    this.head.render(f5);
    this.torso1.render(f5);
    this.torsoUpper.render(f5);
    this.torsoSpine2.render(f5);
    this.torsoLower.render(f5);
  }

  /**
   * This is a helper function from Tabula to set the rotation of model parts
   */
  public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }
}