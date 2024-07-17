package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CanSeeCondition extends Condition {
    private Argument<Entity> reference= CasterEntityArgument.INSTANCE;
    private Condition flimsy=FalseCondition.INSTANCE;
    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        Entity ref=reference.resolve(wrapper, parent, performer, target);
        if(ref instanceof LivingEntity looker){
            return looker.hasLineOfSight(target);
        }
        return GeneralUtils.viewBlocked(ref, target, flimsy.resolve(wrapper, parent, performer, target));
    }
}
