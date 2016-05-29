package mmc.common.tile;

import net.minecraft.tileentity.TileEntity;

public final class TileEntityAssembler
extends TileEntity {
  public enum State{
    DIVERGED,
    CONVERGED
  }

  private State state = State.CONVERGED;

  public void swapState(){
    if(this.state == State.CONVERGED){
      this.state = State.DIVERGED;
    } else{
      this.state = State.CONVERGED;
    }
  }

  public State state(){
    return this.state;
  }
}