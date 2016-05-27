package mmc.common.tile;

import mmc.api.computer.ITerminal;
import mmc.common.MMC;
import mmc.common.core.ClientTerminal;
import mmc.common.core.ServerTerminal;
import mmc.common.net.MMCNetwork;
import mmc.common.net.PacketTerminalCreated;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public final class TileEntityComputer
extends TileEntity{
  private String terminalID = null;

  public ITerminal createTerminal() {
    return this.getWorld().isRemote
           ? this.createClientTerminal()
           : this.createServerTerminal();
  }

  public ITerminal getTerminal() {
    return this.getWorld().isRemote
           ? this.getClientTerminal()
           : this.getServerTerminal();
  }

  public void setTerminalID(String id){
    System.out.println("[" + FMLCommonHandler.instance().getEffectiveSide() + "] Setting terminal ID: " + id);
    this.terminalID = id;
  }

  public ServerTerminal getServerTerminal(){
    if(!MMC.SERVER_TERMINAL_REGISTRY.contains(this.terminalID)) return null;
    return MMC.SERVER_TERMINAL_REGISTRY.get(this.terminalID);
  }

  public ClientTerminal getClientTerminal(){
    if(!MMC.CLIENT_TERMINAL_REGISTRY.contains(this.terminalID)) return null;
    return MMC.CLIENT_TERMINAL_REGISTRY.get(this.terminalID);
  }

  public ClientTerminal createClientTerminal() {
    if(this.getWorld().isRemote){
      System.out.println("Validating: " + this.terminalID);
      if (this.terminalID != null) {
        ClientTerminal terminal = new ClientTerminal(this.terminalID);
        MMC.CLIENT_TERMINAL_REGISTRY.register(terminal);
        return terminal;
      }
    }
    return null;
  }

  public ServerTerminal createServerTerminal() {
    if (this.getWorld().isRemote) return null;

    if (this.terminalID == null) {
      do {
        this.terminalID = UUID.randomUUID()
                              .toString();
      } while (MMC.CLIENT_TERMINAL_REGISTRY.contains(this.terminalID));
      MMCNetwork.INSTANCE.sendToAll(new PacketTerminalCreated(this.getPos(), this.terminalID));
    }

    if (!MMC.SERVER_TERMINAL_REGISTRY.contains(this.terminalID)) {
      ServerTerminal terminal = new ServerTerminal(this.terminalID);
      MMC.SERVER_TERMINAL_REGISTRY.register(terminal);
    }

    return MMC.SERVER_TERMINAL_REGISTRY.get(this.terminalID);
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    if(this.terminalID != null){
      compound.setString("TerminalID", this.terminalID);
    }

    ITerminal terminal = this.getTerminal();
    if(terminal instanceof ServerTerminal){
      NBTTagCompound terminalCompound = new NBTTagCompound();
      ((ServerTerminal) terminal).writeToNBT(terminalCompound);
      compound.setTag("Terminal", terminalCompound);
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if(compound.hasKey("TerminalID")){
      this.terminalID = compound.getString("TerminalID");
    }
    if(compound.hasKey("Terminal")){
      ITerminal terminal = this.getServerTerminal();
      if(terminal != null){
        ((ServerTerminal) terminal).readFromNBT(compound.getCompoundTag("Terminal"));
      }
    }
  }

  @Override
  public Packet<?> getDescriptionPacket() {
    NBTTagCompound compound = new NBTTagCompound();
    this.writeToNBT(compound);
    return new SPacketUpdateTileEntity(this.getPos(), 0, compound);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }
}