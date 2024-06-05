package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class IsTargetCondition extends Condition {
    @Override
    public boolean evaluate(TimerAction parent, Entity performer, Entity target) {
        return performer instanceof Mob mob && mob.getTarget() == target;
    }
}
