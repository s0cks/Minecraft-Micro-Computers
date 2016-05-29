package mmc.common.item;

import mmc.common.entity.EntityRobit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class ItemRobit
extends Item {
  @Override
  public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
    if(!world.isRemote){
      EntityRobit robit = new EntityRobit(world);
      robit.setPosition(pos.getX(), pos.getY(), pos.getZ());
      world.spawnEntityInWorld(robit);
    }
    return EnumActionResult.PASS;
  }
}