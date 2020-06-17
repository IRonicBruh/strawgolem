package com.commodorethrawn.strawgolem.entity.ai;

import com.commodorethrawn.strawgolem.entity.EntityStrawGolem;
import net.minecraft.entity.ai.goal.WanderAroundGoal;

public class GolemWanderGoal extends WanderAroundGoal {

    private final EntityStrawGolem strawGolem;

    public GolemWanderGoal(EntityStrawGolem creature, double speedIn) {
        super(creature, speedIn);
        strawGolem = creature;
    }

    @Override
    public boolean canStart() {
        return strawGolem.isHandEmpty() && super.canStart();
    }
}
