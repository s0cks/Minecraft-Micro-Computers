package mmc.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public final class ContainerComputer
extends Container {
  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }
}