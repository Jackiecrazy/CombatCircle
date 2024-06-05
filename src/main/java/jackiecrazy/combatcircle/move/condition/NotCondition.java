package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class NotCondition extends Condition {
    Condition of;

    @Override
    public boolean evaluate(TimerAction parent, Entity performer, Entity target) {
        return !of.evaluate(parent, performer, target);
    }
}
