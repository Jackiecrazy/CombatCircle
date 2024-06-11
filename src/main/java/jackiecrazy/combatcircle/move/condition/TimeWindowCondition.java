package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class TimeWindowCondition extends Condition {

    private int time;

    @Override
    public boolean evaluate(MovesetWrapper wrapper, Entity performer, Entity target) {
        return wrapper.getCurrentMove().getTimer() >= time;
    }
}
