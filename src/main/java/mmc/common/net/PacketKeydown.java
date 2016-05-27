package mmc.common.net;

import io.netty.buffer.ByteBuf;
import mmc.common.MMC;
import mmc.common.core.ServerTerminal;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketKeydown
implements IMessage,
           IMessageHandler<PacketKeydown, IMessage>{
  private char key;
  private int code = -1;
  private String terminalID;

  public PacketKeydown(){}

  public PacketKeydown(String terminalID, int code, char key){
    this.key = key;
    this.code = code;
    this.terminalID = terminalID;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.key = buf.readChar();
    this.code = buf.readInt();
    this.terminalID = ByteBufUtils.readUTF8String(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    if(this.code != -1){
      buf.writeChar(this.key);
      buf.writeInt(this.code);
      ByteBufUtils.writeUTF8String(buf, this.terminalID);
    }
  }

  @Override
  public IMessage onMessage(PacketKeydown message, MessageContext ctx) {
    ServerTerminal terminal = MMC.SERVER_TERMINAL_REGISTRY.get(message.terminalID);
    terminal.onKeydown(message.code, message.key);
    return null;
  }
}