package mmc.client.render.entity.model;

import mmc.api.robit.IRobitPart;
import mmc.client.render.entity.model.rosegold.RoseGoldLeftArm;
import mmc.client.render.entity.model.rosegold.RoseGoldLeftLeg;
import mmc.client.render.entity.model.rosegold.RoseGoldRightArm;
import mmc.client.render.entity.model.rosegold.RoseGoldRightLeg;
import mmc.client.render.entity.model.rosegold.RoseGoldTorso;
import mmc.client.render.entity.model.tungsten.TungstenLeftArm;
import mmc.client.render.entity.model.tungsten.TungstenLeftLeg;
import mmc.client.render.entity.model.tungsten.TungstenRightArm;
import mmc.client.render.entity.model.tungsten.TungstenRightLeg;
import mmc.client.render.entity.model.tungsten.TungstenTorso;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class ModularRobitModel{
  public static final ResourceLocation ROSE_GOLD_TEX = new ResourceLocation("mmc", "textures/entity/robit_rosegold.png");
  public static final ResourceLocation TUNGSTEN_TEX = new ResourceLocation("mmc", "textures/entity/robit_tungsten.png");

  public static final ModularRobitModel TUNGSTEN = new ModularRobitModel();
  public static final ModularRobitModel ROSE_GOLD = new ModularRobitModel();
  public static final ModularRobitModel TUNG_GOLD = new ModularRobitModel();

  static{
    // Tungsten
    // Legs
    TUNGSTEN.addPart(new TungstenLeftLeg());
    TUNGSTEN.addPart(new TungstenRightLeg());
    // Arms
    TUNGSTEN.addPart(new TungstenLeftArm());
    TUNGSTEN.addPart(new TungstenRightArm());
    // Torso
    TUNGSTEN.addPart(new TungstenTorso());

    // Rose Gold
    // Legs
    ROSE_GOLD.addPart(new RoseGoldLeftLeg());
    ROSE_GOLD.addPart(new RoseGoldRightLeg());
    // Arms
    ROSE_GOLD.addPart(new RoseGoldLeftArm());
    ROSE_GOLD.addPart(new RoseGoldRightArm());
    // Torso
    ROSE_GOLD.addPart(new RoseGoldTorso());

    // TUNG_GOLD
    TUNG_GOLD.addPart(new RoseGoldLeftLeg());
    TUNG_GOLD.addPart(new RoseGoldRightLeg());
    TUNG_GOLD.addPart(new TungstenTorso());
    TUNG_GOLD.addPart(new TungstenRightArm());
    TUNG_GOLD.addPart(new RoseGoldLeftArm());
  }

  private final Map<IRobitPart.Type, IRobitPart> parts = new HashMap<>();

  public void addPart(IRobitPart part){
    this.parts.put(part.type(), part);
  }

  public void render(Entity entity, float f){
    for(Map.Entry<IRobitPart.Type, IRobitPart> part : this.parts.entrySet()){
      part.getValue().render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f);
    }
  }
}