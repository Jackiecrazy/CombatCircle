package jackiecrazy.combatcircle.move.filter;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class NoFilter extends Filter {
    public static final NoFilter INSTANCE=new NoFilter();
    @Override
    public List<Entity> filter(MovesetWrapper wrapper, Entity performer, Entity target, List<Entity> targets) {
        return targets;
    }
}
