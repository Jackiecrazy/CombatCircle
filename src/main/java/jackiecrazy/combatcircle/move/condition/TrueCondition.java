package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class TrueCondition extends Condition {
    public static final TrueCondition INSTANCE = new TrueCondition();

    @Override
    public boolean evaluate(TimerAction parent, Entity performer, Entity target) {
        return true;
    }
}
