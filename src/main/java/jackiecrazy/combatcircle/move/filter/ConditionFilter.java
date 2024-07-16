package jackiecrazy.combatcircle.move.filter;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class ConditionFilter extends Filter {
    private Condition condition;
    @Override
    public List<Entity> filter(MovesetWrapper wrapper, TimerAction parent, Entity performer, Entity target, List<Entity> targets) {
        return targets.stream().filter(a->condition.resolve(wrapper, parent, performer, a)).toList();
    }
}
