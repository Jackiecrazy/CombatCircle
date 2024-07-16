package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class FalseCondition extends Condition{
    public static final FalseCondition INSTANCE=new FalseCondition();
    @Override
    public boolean resolve(MovesetWrapper wrapper, Entity performer, Entity target) {
        return false;
    }
}
