package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class AndCondition extends Condition {
    List<Condition> values;

    @Override
    public boolean evaluate(MovesetWrapper wrapper, TimerAction parent, Entity performer, Entity target) {
        for(Condition c: values){
            if(!c.evaluate(wrapper, parent, performer, target))return false;
        }
        return true;
    }
}
