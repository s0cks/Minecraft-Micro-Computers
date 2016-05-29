package mmc.client.render.entity.model.rosegold;

import mmc.api.robit.IRobitPart;
import mmc.client.render.entity.model.ModularRobitModel;
import mmc.common.MMC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public final class RoseGoldTorso
extends ModelBase
implements IRobitPart{
  private final ModelRenderer torsoPelvis;
  private final ModelRenderer torsoLower;
  private final ModelRenderer torsoUpper;
  private final ModelRenderer torsoSpine;
  private final ModelRenderer torsoNeck;
  private final ModelRenderer torsoHead;

  public RoseGoldTorso(){
    this.textureWidth = 64;
    this.textureHeight = 32;
    this.torsoHead = new ModelRenderer(this, 44, 8);
    this.torsoHead.setRotationPoint(0.0F, 11.4F, -0.4F);
    this.torsoHead.addBox(-1.0F, -2.0F, -1.0F, 2, 2, 2, 0.0F);
    this.setRotateAngle(torsoHead, 0.17453292519943295F, 0.0F, 0.0F);
    this.torsoPelvis = new ModelRenderer(this, 0, 8);
    this.torsoPelvis.setRotationPoint(0.0F, 16.8F, 0.0F);
    this.torsoPelvis.addBox(-0.5F, 0.0F, -1.0F, 1, 1, 2, 0.0F);
    this.setRotateAngle(torsoPelvis, 0.10471975511965977F, 0.0F, 0.0F);
    this.torsoUpper = new ModelRenderer(this, 16, 8);
    this.torsoUpper.setRotationPoint(0.0F, 11.0F, 0.0F);
    this.torsoUpper.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
    this.torsoSpine = new ModelRenderer(this, 30, 8);
    this.torsoSpine.setRotationPoint(0.0F, 11.0F, 1.5F);
    this.torsoSpine.addBox(-0.5F, 0.0F, 0.0F, 1, 5, 1, 0.0F);
    this.setRotateAngle(torsoSpine, -0.2792526803190927F, 0.0F, 0.0F);
    this.torsoNeck = new ModelRenderer(this, 34, 8);
    this.torsoNeck.setRotationPoint(0.0F, 10.8F, -0.2F);
    this.torsoNeck.addBox(-1.5F, 0.0F, -1.5F, 3, 1, 2, 0.0F);
    this.torsoLower = new ModelRenderer(this, 6, 8);
    this.torsoLower.setRotationPoint(0.0F, 13.9F, 0.0F);
    this.torsoLower.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
  }

  @Override
  public String id() {
    return "rosegold_torso";
  }

  @Override
  public Type type() {
    return Type.TORSO;
  }

  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    MMC.proxy.client().renderEngine.bindTexture(ModularRobitModel.ROSE_GOLD_TEX);
    this.torsoHead.render(f5);
    this.torsoPelvis.render(f5);
    this.torsoUpper.render(f5);
    this.torsoSpine.render(f5);
    this.torsoNeck.render(f5);
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