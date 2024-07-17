package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

public class ParentDataArgument<T> implements Argument<T>{
    @Override
    public T resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return wrapper.getData(parent);
    }
}
