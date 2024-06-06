package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class IsTargetCondition extends Condition {
    private EntityArgument reference= CasterEntityArgument.INSTANCE;
    @Override
    public boolean evaluate(TimerAction parent, Entity performer, Entity target) {
        Entity ref=reference.resolve(performer, target);
        return ref instanceof Mob mob && mob.getTarget() == target;
    }
}
