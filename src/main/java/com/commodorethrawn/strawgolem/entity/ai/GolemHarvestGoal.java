package com.commodorethrawn.strawgolem.entity.ai;

import com.commodorethrawn.strawgolem.config.ConfigHelper;
import com.commodorethrawn.strawgolem.entity.EntityStrawGolem;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.WorldView;

import java.util.List;

public class GolemHarvestGoal extends MoveToTargetPosGoal {
    private final EntityStrawGolem strawgolem;

    public GolemHarvestGoal(EntityStrawGolem strawgolem, double speedIn) {
        super(strawgolem, speedIn, ConfigHelper.getSearchRangeHorizontal(), ConfigHelper.getSearchRangeVertical());
        this.strawgolem = strawgolem;
    }

    @Override
    public boolean canStart() {
        if (super.canStart() && strawgolem.isHandEmpty()) {
            this.cooldown = 0;
            return true;
        } else return false;
    }

    @Override
	public void tick() {
        this.strawgolem.getLookControl().lookAt(
                this.targetPos.getX() + 0.5D,
                this.targetPos.getY(),
                this.targetPos.getZ() + 0.5D,
                10.0F,
                this.strawgolem.getLookPitchSpeed());
        double targetDistance = strawgolem.world.getBlockState(targetPos).getBlock() instanceof GourdBlock ? 1.2D : 1.0D;
        if (!this.targetPos.isWithinDistance(this.strawgolem.getPos(), targetDistance)) {
            ++this.tryingTime;
            if (this.shouldResetPath()) {
                this.strawgolem.getNavigation().startMovingTo(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1D, this.targetPos.getZ() + 0.5D, this.movementSpeed);
            }
        } else {
            --this.tryingTime;
            doHarvest();
        }
    }


    @Override
    protected boolean isTargetPos(WorldView worldIn, BlockPos pos) {
        Vec3d posVec = strawgolem.getPos();
        if (posVec.getY() % 1 > 0.01) posVec = posVec.add(0, 1, 0);
        RayTraceContext ctx = new RayTraceContext(posVec, new Vec3d(pos), RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, strawgolem);
        if (!worldIn.rayTrace(ctx).getPos().equals(pos)) return false;
        BlockState block = worldIn.getBlockState(pos);
        if (ConfigHelperblockHarvestAllowed(block.getBlock())) {
            if (block.getBlock() instanceof CropBlock) {
                return ((CropBlock) block.getBlock()).isMature(block);
            } else if (block.getBlock() instanceof GourdBlock) {
                return true;
            } else if (block.getBlock() == Blocks.NETHER_WART) {
                return block == Blocks.NETHER_WART.getDefaultState().with(NetherWartBlock.AGE, 3);
            }
        }
        return false;
    }

    private void doHarvest() {
        ServerWorld worldIn = (ServerWorld) this.strawgolem.world;
        BlockPos pos = this.targetPos;
        Block block = worldIn.getBlockState(pos).getBlock();
        if (isTargetPos(worldIn, this.targetPos)
                && worldIn.breakBlock(pos, true)
                && ConfigHelperisReplantEnabled()) {
            if (!(block instanceof GourdBlock)) {
                worldIn.setBlockState(pos, block.getDefaultState());
                List<ItemEntity> dropList = worldIn.getEntities(ItemEntity.class, new Box(pos).expand(1.0F), e -> true);
                for (ItemEntity drop : dropList) {
                    if (ConfigHelperisDeliveryEnabled() && !(drop.getStack().getItem() instanceof BlockItem) || drop.getStack().getUseAction() == UseAction.EAT) {
                        this.strawgolem.inventory.insertItem(0, drop.getStack(), false);
                    }
                    drop.remove();
                }
            } else {
                if (ConfigHelperisDeliveryEnabled()) {
                    strawgolem.inventory.insertItem(0, new ItemStack(Item.BLOCK_ITEMS.getOrDefault(block, Items.AIR)), false);
                }
                List<ItemEntity> dropList = worldIn.getEntities(ItemEntity.class, new Box(pos).expand(1.0F), e -> true);
                for (ItemEntity drop : dropList) {
                    drop.remove();
                }
            }
        }
    }

}
