package jackiecrazy.combatcircle.move.filter;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.condition.Condition;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class ConditionFilter<T> extends Filter<T> {
    private Condition condition;
    @Override
    public List<T> filter(MovesetWrapper wrapper, Action parent, Entity performer, Entity target, List<T> targets) {
        return targets.stream().filter(a->condition.resolve(wrapper, parent, performer, a instanceof Entity e?e:null)).toList();
    }
}
