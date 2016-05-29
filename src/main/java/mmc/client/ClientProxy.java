package mmc.client;

import mmc.client.render.entity.RenderEntityRobit;
import mmc.client.render.tile.RenderTileAssembler;
import mmc.common.CommonProxy;
import mmc.common.MMCBlocks;
import mmc.common.entity.EntityRobit;
import mmc.common.tile.TileEntityAssembler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public final class ClientProxy
extends CommonProxy{
  @Override
  public void registerRenders() {
    this.registerRender(MMCBlocks.blockComputer);
  }

  @Override
  public void preInit() {
    MinecraftForge.EVENT_BUS.register(ClientEventHandler.instance());

    RenderingRegistry.registerEntityRenderingHandler(EntityRobit.class, RenderEntityRobit::new);
  }

  @Override
  public void init() {
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAssembler.class, new RenderTileAssembler());
  }

  private void registerRender(Block b){
    String unlocName = b.getUnlocalizedName();
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(new ResourceLocation("mmc", unlocName.substring(unlocName.lastIndexOf('.') + 1)), "inventory"));
  }

  @Override
  public Minecraft client() {
    return FMLClientHandler.instance().getClient();
  }
}