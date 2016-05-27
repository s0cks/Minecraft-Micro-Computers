package mmc.common.net;

import io.netty.buffer.ByteBuf;
import mmc.common.MMC;
import mmc.common.core.ServerTerminal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketRequestUpdate
implements IMessage,
           IMessageHandler<PacketRequestUpdate, IMessage>{
  private String terminalID;

  public PacketRequestUpdate(){}

  public PacketRequestUpdate(String terminalID){
    this.terminalID = terminalID;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.terminalID = ByteBufUtils.readUTF8String(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf, this.terminalID);
  }

  @Override
  public IMessage onMessage(PacketRequestUpdate message, MessageContext ctx) {
    if(!MMC.SERVER_TERMINAL_REGISTRY.contains(message.terminalID)) return null;
    NBTTagCompound compound = new NBTTagCompound();
    ServerTerminal terminal = MMC.SERVER_TERMINAL_REGISTRY.get(message.terminalID);
    terminal.writeToNBT(compound);
    PacketTerminalUpdate updatePck = new PacketTerminalUpdate(message.terminalID, compound);
    MMCNetwork.INSTANCE.sendTo(updatePck, ctx.getServerHandler().playerEntity);
    return null;
  }
}