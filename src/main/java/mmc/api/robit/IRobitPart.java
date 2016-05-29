package mmc.api.robit;

import net.minecraft.entity.Entity;

public interface IRobitPart{
  public static enum Type{
    TORSO,
    L_ARM,
    R_ARM,
    L_LEG,
    R_LEG;
  }

  public String id();
  public Type type();
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5);
}