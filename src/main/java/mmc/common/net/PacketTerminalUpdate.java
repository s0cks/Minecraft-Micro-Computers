package mmc.common.net;

import io.netty.buffer.ByteBuf;
import mmc.common.MMC;
import mmc.common.core.ClientTerminal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketTerminalUpdate
implements IMessage,
           IMessageHandler<PacketTerminalUpdate, IMessage>{
  private String terminalID;
  private NBTTagCompound data;

  public PacketTerminalUpdate(){}

  public PacketTerminalUpdate(String id, NBTTagCompound data){
    this.terminalID = id;
    this.data = data;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.terminalID = ByteBufUtils.readUTF8String(buf);
    this.data = ByteBufUtils.readTag(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf, this.terminalID);
    ByteBufUtils.writeTag(buf, this.data);
  }

  @Override
  public IMessage onMessage(PacketTerminalUpdate message, MessageContext ctx) {
    if(!MMC.CLIENT_TERMINAL_REGISTRY.contains(message.terminalID)){
      MMC.CLIENT_TERMINAL_REGISTRY.register(new ClientTerminal(message.terminalID));
    }
    ClientTerminal terminal = MMC.CLIENT_TERMINAL_REGISTRY.get(message.terminalID);
    terminal.onSync(message.data);
    return null;
  }
}