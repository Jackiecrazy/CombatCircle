package jackiecrazy.combatcircle.actions;

import net.minecraft.world.entity.LivingEntity;

public abstract class Action{
    public abstract void perform(LivingEntity performer, LivingEntity target);
}
