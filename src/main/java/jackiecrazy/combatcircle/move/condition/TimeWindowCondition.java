package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class TimeWindowCondition extends Condition {

    private int from = 0, to = Integer.MAX_VALUE;

    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        if (!(parent instanceof TimerAction ta)) return false;
        int time = wrapper.getTimer(ta);
        return time >= from && time < to;
    }
}
