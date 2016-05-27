package mmc.common.core;

import mmc.common.net.MMCNetwork;
import mmc.common.net.PacketRequestUpdate;

public final class ClientTerminalRegistry
extends TerminalRegistry<ClientTerminal>{

  @Override
  public void register(ClientTerminal terminal) {
    super.register(terminal);
    MMCNetwork.INSTANCE.sendToServer(new PacketRequestUpdate(terminal.id()));
  }

  @Override
  public void update() {

  }
}