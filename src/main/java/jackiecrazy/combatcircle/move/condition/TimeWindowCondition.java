package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class TimeWindowCondition extends Condition {

    private int from=0, to=Integer.MAX_VALUE;

    @Override
    public boolean resolve(MovesetWrapper wrapper, TimerAction parent, Entity performer, Entity target) {
        int time = wrapper.getTimer(parent);
        return time >= from && time < to;
    }
}
