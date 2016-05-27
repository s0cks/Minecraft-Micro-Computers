package mmc.common.net;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class MMCNetwork{
  public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("mmc");

  public static void init(){
    INSTANCE.registerMessage(PacketKeydown.class, PacketKeydown.class, 0x0, Side.SERVER);
    INSTANCE.registerMessage(PacketTerminalUpdate.class, PacketTerminalUpdate.class, 0x1, Side.CLIENT);
    INSTANCE.registerMessage(PacketRequestUpdate.class, PacketRequestUpdate.class, 0x2, Side.SERVER);
    INSTANCE.registerMessage(PacketTerminalCreated.class, PacketTerminalCreated.class, 0x3, Side.CLIENT);
  }
}