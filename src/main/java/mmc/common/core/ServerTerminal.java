package mmc.common.core;

import io.github.s0cks.mmc.Binary;
import io.github.s0cks.mmc.assembler.Parser;
import io.github.s0cks.mmc.linker.Linker;
import mmc.api.computer.IProcessor;
import mmc.api.computer.ITerminal;
import mmc.api.fs.IFileSystem;
import mmc.common.core.computer.MMCProcessor;
import mmc.common.core.fs.Ext9001FileSystem;
import mmc.common.net.MMCNetwork;
import mmc.common.net.PacketTerminalUpdate;
import net.minecraft.nbt.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.ForkJoinPool;

public final class ServerTerminal
implements ITerminal {
  private final ForkJoinPool threads = new ForkJoinPool();
  private final IFileSystem fileSystem = new Ext9001FileSystem();
  private final IProcessor processor = new MMCProcessor(this);
  private final String id;
  private State state = State.OFF;

  public ServerTerminal(String terminalID){
    this.id = terminalID;
  }

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public void onKeydown(int code, char c) {

  }

  @Override
  public void turnOff() {
    this.state = State.OFF;
  }

  @Override
  public void turnOn() {
    if(this.state != State.ON){
      this.state = State.ON;
      this.threads.execute(() -> {
        try(InputStream biosIn = this.fileSystem.openInputStream("/bios.S");
            InputStream stdlibIn = this.fileSystem.openInputStream("/stdlib.S");
            ByteArrayOutputStream stdlibBos = new ByteArrayOutputStream()){
          (new Parser(stdlibIn)).compile(stdlibBos);
          Linker.SimpleObjectFile stdlibSof = new Linker.SimpleObjectFile(new ByteArrayInputStream(stdlibBos.toByteArray()));

          Binary bin = (new Linker()).link(biosIn, stdlibSof);
          this.processor.loadBinary(bin);
          while(this.state == State.ON){
            this.processor.tick();
          }
        } catch(Exception e){
          e.printStackTrace(System.err);
        }
      });
    }
  }

  @Override
  public IProcessor owner() {
    return this.processor;
  }

  @Override
  public String line(int i) {
    return null;
  }

  @Override
  public State state() {
    return this.state;
  }

  public void writeToNBT(NBTTagCompound compound){
    compound.setIntArray("Memory", this.processor.memory());
    compound.setString("State", this.state.name());
  }

  public void readFromNBT(NBTTagCompound compound){
    if(compound.hasKey("Memory")){
      this.processor.setMemory(compound.getIntArray("Memory"));
    }
    if(compound.hasKey("State")){
      this.state = State.valueOf(compound.getString("State"));
    }
  }

  public void sync(){
    NBTTagCompound data = new NBTTagCompound();
    this.writeToNBT(data);
    MMCNetwork.INSTANCE.sendToAll(new PacketTerminalUpdate(this.id, data));
  }
}