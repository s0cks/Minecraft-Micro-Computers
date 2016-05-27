package mmc.common;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public final class MMCTickHandler{
  @SubscribeEvent
  public void onConnectionOpened(FMLNetworkEvent.ClientConnectedToServerEvent e){
    MMC.CLIENT_TERMINAL_REGISTRY.reset();
  }

  @SubscribeEvent
  public void onConnectionClosed(FMLNetworkEvent.ClientDisconnectionFromServerEvent e){
    MMC.CLIENT_TERMINAL_REGISTRY.reset();
  }

  @SubscribeEvent
  public void onTick(TickEvent.ServerTickEvent e){
    if(e.phase.equals(TickEvent.Phase.START)){
      MMC.SERVER_TERMINAL_REGISTRY.update();
    }
  }
}