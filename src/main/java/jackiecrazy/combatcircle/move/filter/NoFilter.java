package jackiecrazy.combatcircle.move.filter;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class NoFilter<T> extends Filter<T> {
    public static final NoFilter INSTANCE=new NoFilter();
    @Override
    public List<T> filter(MovesetWrapper wrapper, Action parent, Entity performer, Entity target, List<T> targets) {
        return targets;
    }
}
