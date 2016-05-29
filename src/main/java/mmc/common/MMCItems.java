package mmc.common;

import mmc.common.item.ItemRobit;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class MMCItems {
  private MMCItems() {}

  public static final Item itemRobit = new ItemRobit()
                                       .setCreativeTab(MMC.tab)
                                       .setUnlocalizedName("robit");

  public static void init(){
    register(itemRobit);
  }

  private static void register(Item item){
    String unlocName = item.getUnlocalizedName();
    ResourceLocation loc = new ResourceLocation("mmc", unlocName.substring(unlocName.lastIndexOf('.') + 1));
    GameRegistry.register(item, loc);
  }
}