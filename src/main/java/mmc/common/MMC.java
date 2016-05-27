package mmc.common;

import mmc.api.MMCApi;
import mmc.client.gui.GuiComputer;
import mmc.common.core.ClientTerminal;
import mmc.common.core.ClientTerminalRegistry;
import mmc.common.core.ServerTerminal;
import mmc.common.core.ServerTerminalRegistry;
import mmc.common.core.TerminalRegistry;
import mmc.common.core.fs.Ext9001ResourceMount;
import mmc.common.core.fs.MountRegistry;
import mmc.common.gui.ContainerComputer;
import mmc.common.net.MMCNetwork;
import mmc.common.tile.TileEntityComputer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "mmc",
     name = "MMC",
     version = "0.0.0.0")
public final class MMC
implements IGuiHandler {
  @Mod.Instance("mmc")
  public static MMC instance;

  static {
    MMCApi.MOUNT_REGISTRY = new MountRegistry();
  }

  @SidedProxy(clientSide = "mmc.client.ClientProxy",
              serverSide = "mmc.common.CommonProxy")
  public static CommonProxy proxy;

  public static final CreativeTabs tab = new CreativeTabMMC();
  public static final byte GUI_COMPUTER = 0x0;
  public static final TerminalRegistry<ClientTerminal> CLIENT_TERMINAL_REGISTRY = new ClientTerminalRegistry();
  public static final TerminalRegistry<ServerTerminal> SERVER_TERMINAL_REGISTRY = new ServerTerminalRegistry();

  @Mod.EventHandler
  public void onPreInit(FMLPreInitializationEvent e) {
    MMCBlocks.init();

    proxy.registerRenders();
  }

  @Mod.EventHandler
  public void onInit(FMLInitializationEvent e) {
    MMCNetwork.init();

    NetworkRegistry.INSTANCE.registerGuiHandler(instance, instance);

    MinecraftForge.EVENT_BUS.register(new MMCTickHandler());

    GameRegistry.registerTileEntity(TileEntityComputer.class, "computer");
  }

  @Mod.EventHandler
  public void onPostInit(FMLPostInitializationEvent e) {
    MMCApi.MOUNT_REGISTRY.mount("/", new Ext9001ResourceMount(new ResourceLocation("mmc", "root")));
  }

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    switch (ID) {
      case GUI_COMPUTER:
        return new ContainerComputer();
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    switch (ID) {
      case GUI_COMPUTER:
        return new GuiComputer(((TileEntityComputer) world.getTileEntity(new BlockPos(x, y, z))));
    }
    return null;
  }
}