package mmc.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * RoseGoldRobit - Tris
 * Created using Tabula 4.1.1
 */
public class rosegoldLeftLeg extends ModelBase {
    public ModelRenderer leftLeg;
    public ModelRenderer leftLDecoration1;
    public ModelRenderer leftLDecoration2;

    public rosegoldLeftLeg() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leftLeg = new ModelRenderer(this, 0, 0);
        this.leftLeg.setRotationPoint(-0.5F, 17.5F, 0.0F);
        this.leftLeg.addBox(-2.0F, -0.5F, -1.0F, 2, 6, 2, 0.0F);
        this.leftLDecoration1 = new ModelRenderer(this, 8, 0);
        this.leftLDecoration1.setRotationPoint(-1.5F, 16.7F, -0.3F);
        this.leftLDecoration1.addBox(-0.5F, 0.0F, -1.0F, 1, 6, 1, 0.0F);
        this.leftLDecoration2 = new ModelRenderer(this, 12, 0);
        this.leftLDecoration2.setRotationPoint(-1.5F, 21.0F, 0.0F);
        this.leftLDecoration2.addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, 0.1F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.leftLeg.render(f5);
        this.leftLDecoration1.render(f5);
        this.leftLDecoration2.render(f5);
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
