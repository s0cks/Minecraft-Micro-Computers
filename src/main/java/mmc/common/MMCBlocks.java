package mmc.common;

import mmc.common.block.BlockAssembler;
import mmc.common.block.BlockComputer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class MMCBlocks {
  private MMCBlocks() {}

  public static final Block blockComputer = new BlockComputer()
                                            .setCreativeTab(MMC.tab)
                                            .setUnlocalizedName("computer");
  public static final Block blockAssembler = new BlockAssembler()
                                             .setCreativeTab(MMC.tab)
                                             .setUnlocalizedName("assembler");

  public static void init() {
    register(blockAssembler);
    register(blockComputer);
  }

  private static void register(Block b) {
    String unlocName = b.getUnlocalizedName();
    ResourceLocation loc = new ResourceLocation("mmc", unlocName.substring(unlocName.lastIndexOf('.') + 1));
    GameRegistry.register(b, loc);
    GameRegistry.register(new ItemBlock(b), loc);
  }
}