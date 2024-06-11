package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class StartAtCondition extends Condition {
    public StartAtCondition(int at) {
        time = at;
    }

    private int time;

    @Override
    public boolean evaluate(TimerAction parent, Entity performer, Entity target) {
        return parent != null && parent.getTimer() >= time;
    }
}
