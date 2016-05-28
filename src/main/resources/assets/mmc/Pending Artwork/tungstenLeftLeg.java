package mmc.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Robit - Tris
 * Created using Tabula 4.1.1
 */
public class tungstenLeftLeg extends ModelBase {
    public ModelRenderer leftLeg;
    public ModelRenderer leftLFoot;
    public ModelRenderer leftLDecoration1;
    public ModelRenderer leftLDecoration2;

    public tungstenLeftLeg() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leftLFoot = new ModelRenderer(this, 8, 0);
        this.leftLFoot.setRotationPoint(-1.5F, 23.0F, 0.0F);
        this.leftLFoot.addBox(-1.0F, 0.0F, -2.0F, 2, 1, 1, 0.0F);
        this.leftLDecoration1 = new ModelRenderer(this, 14, 0);
        this.leftLDecoration1.setRotationPoint(-1.5F, 17.0F, 0.0F);
        this.leftLDecoration1.addBox(-1.0F, 0.0F, -1.0F, 1, 3, 2, 0.2F);
        this.leftLDecoration2 = new ModelRenderer(this, 20, 0);
        this.leftLDecoration2.setRotationPoint(-1.5F, 20.0F, 0.0F);
        this.leftLDecoration2.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 1, 0.0F);
        this.setRotateAngle(leftLDecoration2, -0.2617993877991494F, 0.0F, 0.0F);
        this.leftLeg = new ModelRenderer(this, 0, 0);
        this.leftLeg.setRotationPoint(-0.5F, 17.5F, 0.0F);
        this.leftLeg.addBox(-2.0F, -0.5F, -1.0F, 2, 7, 2, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.leftLFoot.render(f5);
        this.leftLDecoration1.render(f5);
        this.leftLDecoration2.render(f5);
        this.leftLeg.render(f5);
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
