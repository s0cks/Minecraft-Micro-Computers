package mmc.common.net;

import io.netty.buffer.ByteBuf;
import mmc.common.tile.TileEntityComputer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketTerminalCreated
implements IMessage,
           IMessageHandler<PacketTerminalCreated, IMessage>{
  private BlockPos pos;
  private String id;

  public PacketTerminalCreated(){}

  public PacketTerminalCreated(BlockPos pos, String id){
    this.id = id;
    this.pos = pos;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    this.id = ByteBufUtils.readUTF8String(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.pos.getX());
    buf.writeInt(this.pos.getY());
    buf.writeInt(this.pos.getZ());
    ByteBufUtils.writeUTF8String(buf, this.id);
  }

  @Override
  public IMessage onMessage(PacketTerminalCreated message, MessageContext ctx) {
    World world = FMLClientHandler.instance().getWorldClient();
    ((TileEntityComputer) world.getTileEntity(message.pos)).setTerminalID(message.id);
    return null;
  }
}