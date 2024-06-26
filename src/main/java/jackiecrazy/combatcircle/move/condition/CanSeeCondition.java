package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CanSeeCondition extends Condition {
    private EntityArgument reference= CasterEntityArgument.INSTANCE;
    private Condition flimsy=FalseCondition.INSTANCE;
    @Override
    public boolean evaluate(MovesetWrapper wrapper, Entity performer, Entity target) {
        Entity ref=reference.resolveAsEntity(wrapper, performer, target);
        if(ref instanceof LivingEntity looker){
            return looker.hasLineOfSight(target);
        }
        return GeneralUtils.viewBlocked(ref, target, flimsy.evaluate(wrapper, performer, target));
    }
}
