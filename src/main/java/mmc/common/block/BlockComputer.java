package mmc.common.block;

import mmc.common.MMC;
import mmc.common.tile.TileEntityComputer;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class BlockComputer
extends BlockContainer{
  public BlockComputer(){
    super(Material.iron);
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    if(playerIn.getHeldItem(EnumHand.MAIN_HAND) == null){
      if(!playerIn.worldObj.isRemote){
        ((TileEntityComputer) worldIn.getTileEntity(pos)).createServerTerminal().turnOn();
        playerIn.openGui(MMC.instance, MMC.GUI_COMPUTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
      }
      return true;
    }
    return false;
  }

  @Override
  public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    super.onBlockDestroyedByPlayer(worldIn, pos, state);
  }

  @Override
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    super.onBlockAdded(worldIn, pos, state);
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityComputer();
  }
}