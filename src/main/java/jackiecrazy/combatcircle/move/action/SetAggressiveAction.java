package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;

public class SetAggressiveAction extends Action {
    Condition toggle;

    @Override
    public int perform(@Nullable TimerAction parent, Entity performer, Entity target) {
        if (performer instanceof Mob e) {
            e.setAggressive(toggle.evaluate(parent, performer, target));
        }
        return 0;
    }
}
