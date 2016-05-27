package mmc.common.core;

public final class ServerTerminalRegistry
extends TerminalRegistry<ServerTerminal>{
  @Override
  public void register(ServerTerminal terminal) {
    super.register(terminal);
  }

  @Override
  public void update() {
    this.all().forEach(ServerTerminal::sync);
  }
}