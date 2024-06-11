package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class TrueCondition extends Condition {
    public static final TrueCondition INSTANCE = new TrueCondition();

    @Override
    public boolean evaluate(MovesetWrapper wrapper, Entity performer, Entity target) {
        return true;
    }
}
