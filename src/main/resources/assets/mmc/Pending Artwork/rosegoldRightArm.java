package mmc.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * RoseGoldRobit - Tris
 * Created using Tabula 4.1.1
 */
public class rosegoldRightArm extends ModelBase {
    public ModelRenderer rightAShoulder;
    public ModelRenderer rightArm;
    public ModelRenderer rightADecoration1;
    public ModelRenderer rightADecoration2;

    public rosegoldRightArm() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.rightAShoulder = new ModelRenderer(this, 0, 14);
        this.rightAShoulder.mirror = true;
        this.rightAShoulder.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.rightAShoulder.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, 0.1F);
        this.rightArm = new ModelRenderer(this, 8, 14);
        this.rightArm.mirror = true;
        this.rightArm.setRotationPoint(2.8F, 13.0F, 0.0F);
        this.rightArm.addBox(0.0F, 0.0F, -1.0F, 1, 5, 2, 0.0F);
        this.rightADecoration1 = new ModelRenderer(this, 14, 14);
        this.rightADecoration1.mirror = true;
        this.rightADecoration1.setRotationPoint(2.8F, 15.0F, 0.0F);
        this.rightADecoration1.addBox(0.0F, 0.0F, -1.0F, 1, 2, 2, 0.1F);
        this.rightADecoration2 = new ModelRenderer(this, 20, 14);
        this.rightADecoration2.mirror = true;
        this.rightADecoration2.setRotationPoint(2.8F, 13.2F, 0.0F);
        this.rightADecoration2.addBox(0.0F, 0.0F, -1.0F, 1, 1, 2, 0.1F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.rightAShoulder.render(f5);
        this.rightArm.render(f5);
        this.rightADecoration1.render(f5);
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
}
