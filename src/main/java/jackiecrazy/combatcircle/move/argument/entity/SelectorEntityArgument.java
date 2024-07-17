package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class SelectorEntityArgument extends EntityArgument{
    private Argument<List<Entity>> select;
    @Override
    public Entity resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return select.resolve(wrapper, parent, caster, target).get(0);
    }
}
