package mmc.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Robit - Tris
 * Created using Tabula 4.1.1
 */
public class tungstenRightLeg extends ModelBase {
    public ModelRenderer rightLeg;
    public ModelRenderer rightLFoot;
    public ModelRenderer rightLDecoration1;
    public ModelRenderer rightLDecoration2;

    public tungstenRightLeg() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.rightLDecoration2 = new ModelRenderer(this, 20, 0);
        this.rightLDecoration2.mirror = true;
        this.rightLDecoration2.setRotationPoint(1.5F, 20.0F, 0.0F);
        this.rightLDecoration2.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 1, 0.0F);
        this.setRotateAngle(rightLDecoration2, -0.2617993877991494F, 0.0F, 0.0F);
        this.rightLFoot = new ModelRenderer(this, 8, 0);
        this.rightLFoot.mirror = true;
        this.rightLFoot.setRotationPoint(1.5F, 23.0F, 0.0F);
        this.rightLFoot.addBox(-1.0F, 0.0F, -2.0F, 2, 1, 1, 0.0F);
        this.rightLDecoration1 = new ModelRenderer(this, 14, 0);
        this.rightLDecoration1.mirror = true;
        this.rightLDecoration1.setRotationPoint(1.5F, 17.0F, 0.0F);
        this.rightLDecoration1.addBox(0.0F, 0.0F, -1.0F, 1, 3, 2, 0.2F);
        this.rightLeg = new ModelRenderer(this, 0, 0);
        this.rightLeg.mirror = true;
        this.rightLeg.setRotationPoint(0.5F, 17.5F, 0.0F);
        this.rightLeg.addBox(0.0F, -0.5F, -1.0F, 2, 7, 2, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.rightLDecoration2.render(f5);
        this.rightLFoot.render(f5);
        this.rightLDecoration1.render(f5);
        this.rightLeg.render(f5);
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
