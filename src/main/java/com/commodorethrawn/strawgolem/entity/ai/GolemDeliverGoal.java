package com.commodorethrawn.strawgolem.entity.ai;

import com.commodorethrawn.strawgolem.config.ConfigHelper;
import com.commodorethrawn.strawgolem.entity.EntityStrawGolem;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.WorldView;

public class GolemDeliverGoal extends MoveToTargetPosGoal {
    private final EntityStrawGolem strawGolem;
    private Boolean deliveringBlock;

    public GolemDeliverGoal(EntityStrawGolem strawGolem, double speedIn) {
        super(strawGolem, speedIn, ConfigHelper.getSearchRangeHorizontal(), ConfigHelper.getSearchRangeVertical());
        this.strawGolem = strawGolem;
    }

    @Override
    public boolean canStart() {
        if (this.findTargetPos() && !strawGolem.isHandEmpty()) {
            this.cooldown = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && !strawGolem.isHandEmpty();
    }

    @Override
    protected boolean findTargetPos() {
        BlockPos.Mutable pos = new BlockPos.Mutable(strawGolem.getChestPos());
        if (isTargetPos(strawGolem.world, pos)) {
            this.targetPos = pos;
            return true;
        }
        strawGolem.removeChestPos(pos);
        return super.findTargetPos();
    }

    @Override
    public void tick() {
        if (deliveringBlock == null) {
            deliveringBlock = strawGolem.holdingBlockCrop();
        }
        this.strawGolem.getLookControl().lookAt(
                this.targetPos.getX() + 0.5D,
                this.targetPos.getY(),
                this.targetPos.getZ() + 0.5D,
                10.0F,
                this.strawGolem.getLookPitchSpeed());
        if (!this.targetPos.isWithinDistance(strawGolem.getPos(), 1.0D)) {
            ++this.tryingTime;
            if (this.shouldResetPath()) {
                this.strawGolem.getNavigation().startMovingTo(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1D, this.targetPos.getZ() + 0.5D, speed);
            }
        } else {
            tryingTime = 0;
            doDeposit();
        }
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        Vec3d posVec = strawGolem.getPos();
        if (posVec.getY() % 1 > 0.01)
            posVec = posVec.add(0, 1, 0); // Used to patch the ray trace colliding with non-full-height blocks
        RayTraceContext ctx = new RayTraceContext(posVec, new Vec3d(pos), RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, strawGolem);
        if (world.getBlockEntity(pos) instanceof ChestBlockEntity) {
            if (world.rayTrace(ctx).getPos().equals(pos)) {
                strawGolem.addChestPos(pos);
                return true;
            }
        }
        return false;
    }

    private void doDeposit() {
        ServerWorld worldIn = (ServerWorld) this.strawGolem.world;
        BlockPos pos = this.targetPos;
        ChestBlockEntity chest = (ChestBlockEntity) worldIn.getBlockEntity(pos);
        ItemStack insertStack = this.strawGolem.inventory.extractItem(0, 64, false);
        for (int i = 0; i < chest.getInvSize(); ++i) {
            if (chest.getInvStack(i).getItem() == Items.AIR
                    || (chest.getInvStack(i).getItem() == insertStack.getItem() && chest.getInvStack(i).getCount() < chest.getInvStack(0).getMaxCount())) {
                chest.setInvStack(i, insertStack);
                break;
            }
        }
        worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

}
