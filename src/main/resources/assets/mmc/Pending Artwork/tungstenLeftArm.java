package mmc.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Robit - Tris
 * Created using Tabula 4.1.1
 */
public class tungstenLeftArm extends ModelBase {
    public ModelRenderer leftAShoulder;
    public ModelRenderer leftArm;
    public ModelRenderer leftADecoration1;
    public ModelRenderer leftADecoration2;

    public tungstenLeftArm() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leftADecoration1 = new ModelRenderer(this, 22, 18);
        this.leftADecoration1.setRotationPoint(-4.0F, 16.0F, 0.0F);
        this.leftADecoration1.addBox(-1.0F, 0.0F, -1.0F, 1, 2, 2, 0.2F);
        this.leftADecoration2 = new ModelRenderer(this, 28, 18);
        this.leftADecoration2.setRotationPoint(-4.0F, 14.0F, 0.0F);
        this.leftADecoration2.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.2F);
        this.leftAShoulder = new ModelRenderer(this, 0, 18);
        this.leftAShoulder.setRotationPoint(-1.3F, 12.0F, 0.0F);
        this.leftAShoulder.addBox(-4.0F, -1.5F, -1.5F, 4, 3, 3, 0.0F);
        this.leftArm = new ModelRenderer(this, 14, 18);
        this.leftArm.setRotationPoint(-4.0F, 12.5F, 0.0F);
        this.leftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.leftADecoration1.render(f5);
        this.leftADecoration2.render(f5);
        this.leftAShoulder.render(f5);
        this.leftArm.render(f5);
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
