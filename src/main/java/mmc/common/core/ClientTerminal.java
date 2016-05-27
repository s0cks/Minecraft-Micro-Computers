package mmc.common.core;

import mmc.api.computer.IProcessor;
import mmc.api.computer.ITerminal;
import net.minecraft.nbt.NBTTagCompound;

public final class ClientTerminal
implements ITerminal{
  private final String id;
  private int[] memory = new int[65535];

  public ClientTerminal(String id){
    this.id = id;
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

  }

  @Override
  public void turnOn() {

  }

  @Override
  public IProcessor owner() {
    return null;
  }

  @Override
  public String line(int i) {
    String str = "";
    for(int ch = (0x8000 + (i * 32)); ch < (0x8000 + (i * 32) + 32); ch += 2){
      int value = ((char) (this.memory[ch] | (this.memory[ch + 1] << 8)));
      if(value == 0x0){
        str += ' ';
      } else{
        str += ((char) value);
      }
    }
    return str;
  }

  @Override
  public State state() {
    return State.OFF;
  }

  public void onSync(NBTTagCompound compound){
    if(compound.hasKey("Memory")){
      this.memory = compound.getIntArray("Memory");
    }
  }
}