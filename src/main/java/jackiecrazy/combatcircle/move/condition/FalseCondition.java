package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.filter.NoFilter;
import net.minecraft.world.entity.Entity;

public class FalseCondition extends Condition{
    public static final FalseCondition INSTANCE=new FalseCondition();
    @Override
    public boolean evaluate(TimerAction parent, Entity performer, Entity target) {
        return false;
    }
}
