package com.commodorethrawn.strawgolem.entity;

import com.commodorethrawn.strawgolem.Strawgolem;
import com.commodorethrawn.strawgolem.entity.ai.GolemDeliverGoal;
import com.commodorethrawn.strawgolem.entity.ai.GolemHarvestGoal;
import com.commodorethrawn.strawgolem.entity.ai.GolemWanderGoal;
import com.commodorethrawn.strawgolem.entity.capability.lifespan.ILifespan;
import com.commodorethrawn.strawgolem.entity.capability.memory.IMemory;
import com.commodorethrawn.strawgolem.entity.capability.memory.MemoryProvider;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.GourdBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class EntityStrawGolem extends GolemEntity {

    public static final EntityType<EntityStrawGolem> STRAW_GOLEM =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier(Strawgolem.MODID, "strawgolem"),
                    FabricEntityTypeBuilder.create(EntityCategory.CREATURE, EntityStrawGolem::new).dimensions(EntityDimensions.fixed(0.6F, 0.9F)).build()
            );

    public static final Identifier LOOT = new Identifier(Strawgolem.MODID, "strawgolem");
    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(EntityStrawGolem.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<Integer> LIFE = DataTracker.registerData(EntityStrawGolem.class, TrackedDataHandlerRegistry.INTEGER);
    private ILifespan lifespan;
    private IMemory memory;

    @Override
    protected Identifier getLootTableId() {
        return LOOT;
    }

    public EntityStrawGolem(EntityType type, World worldIn) {
        super(type, worldIn);
    }


    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(2.0D);
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);

    }

    @Override
    protected void initGoals() {
        int priority = 0;
        this.goalSelector.add(priority, new SwimGoal(this));
        this.goalSelector.add(++priority, new FleeEntityGoal(this, Monster.class, 10.0F, 0.6D, 0.75D));
        this.goalSelector.add(++priority, new GolemHarvestGoal(this, 0.6D));
        this.goalSelector.add(++priority, new GolemDeliverGoal(this, 0.6D));
        this.goalSelector.add(++priority, new GolemWanderGoal(this, 0.5D));
        this.goalSelector.add(++priority, new LookAtEntityGoal(this, PlayerEntity.class, 5.0F));
        this.goalSelector.add(++priority, new LookAroundGoal(this));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(ITEM, ItemStack.EMPTY);
        this.getDataTracker().startTracking(LIFE, 0);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (memory == null)
            memory = getCapability(MemoryProvider.MEMORY_CAP, null).orElseThrow(() -> new IllegalArgumentException("cant be empty"));

        int currentLife = dataTracker.get(LIFE);
        if (holdingBlockCrop()) ++currentLife;
        dataTracker.set(LIFE, ++currentLife);

        if (currentLife >= Strawgolem.config.lifespan)
            damage(DamageSource.MAGIC, getMaximumHealth() * 100);
    }

    @Override
    public void onDeath(DamageSource source) {
        ItemEntity heldItem = new ItemEntity(this.world, this.prevX, this.prevY, this.prevZ, this.getEquippedStack(EquipmentSlot.MAINHAND));
        this.getEntityWorld().spawnEntity(heldItem);
        super.onDeath(source);
    }

    public boolean isHandEmpty() {
        return getEquippedStack(EquipmentSlot.MAINHAND).isEmpty();
    }

    public boolean holdingBlockCrop() {
        return Block.getBlockFromItem(getEquippedStack(EquipmentSlot.MAINHAND).getItem()) instanceof GourdBlock;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.dataTracker.get(ITEM);
        }
        return ItemStack.EMPTY;
    }

    public void setEquippedStack(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            this.dataTracker.set(ITEM, stack);
        }
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        ItemStack stack = getEquippedStack(EquipmentSlot.MAINHAND);
        tag.put("carriedStack", stack.getTag());
        tag.putInt("lifetime", dataTracker.get(LIFE));
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        ItemStack stack = ItemStack.fromTag(tag.getCompound("carriedStack"));
        setEquippedStack(EquipmentSlot.MAINHAND, stack);
        dataTracker.set(LIFE, tag.getInt("lifetime"));
    }

    public void addChestPos(BlockPos pos) {
        if (!memory.containsPosition(pos))
            memory.addPosition(pos);
    }

    public BlockPos getChestPos() {
        return memory.getClosestPosition(this.getBlockPos());
    }

    public void removeChestPos(BlockPos pos) {
        memory.removePosition(pos);
    }
}
