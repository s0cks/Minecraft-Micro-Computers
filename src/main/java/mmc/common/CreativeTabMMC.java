package mmc.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public final class CreativeTabMMC
extends CreativeTabs {
  public CreativeTabMMC(){
    super("mmc");
  }

  @Override
  public Item getTabIconItem() {
    return Item.getItemFromBlock(MMCBlocks.blockComputer);
  }
}