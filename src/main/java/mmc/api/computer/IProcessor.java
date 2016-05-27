package mmc.api.computer;

import io.github.s0cks.mmc.Address;
import io.github.s0cks.mmc.Binary;
import io.github.s0cks.mmc.Register;

public interface IProcessor
extends ITickable{
  public void setMemory(int[] mem);
  public void loadBinary(Binary binary);
  public int memoryValue(Address addr);
  public int memoryValue(int address);
  public int registerValue(Register reg);
  public int[] memory();
}