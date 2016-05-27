package mmc.client;

import mmc.common.CommonProxy;
import mmc.common.MMCBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public final class ClientProxy
extends CommonProxy{
  @Override
  public void registerRenders() {
    this.registerRender(MMCBlocks.blockComputer);
  }

  private void registerRender(Block b){
    String unlocName = b.getUnlocalizedName();
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(new ResourceLocation("mmc", unlocName.substring(unlocName.lastIndexOf('.') + 1)), "inventory"));
  }
}