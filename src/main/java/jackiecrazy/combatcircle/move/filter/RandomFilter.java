package jackiecrazy.combatcircle.move.filter;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class RandomFilter extends Filter {
    public static final RandomFilter INSTANCE=new RandomFilter();
    @Override
    public List<Entity> filter(MovesetWrapper wrapper, Action parent, Entity performer, Entity target, List<Entity> targets) {
        List<Entity> ret = new ArrayList<>();
        while (ret.size() < limit && targets.size() > 1) {
            int index= CombatCircle.rand.nextInt(targets.size());
            ret.add(targets.remove(index));
        }
        return ret;
    }
}
