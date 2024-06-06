package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class CanSeeCondition extends Condition {
    private EntityArgument reference= CasterEntityArgument.INSTANCE;
    private Condition flimsy=FalseCondition.INSTANCE;
    @Override
    public boolean evaluate(TimerAction parent, Entity performer, Entity target) {
        Entity ref=reference.resolve(performer, target);
        if(ref instanceof LivingEntity looker){
            return looker.hasLineOfSight(target);
        }
        return GeneralUtils.viewBlocked(ref, target, flimsy.evaluate(parent, performer, target));
    }
}
