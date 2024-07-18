package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

public class ParentDataArgument<T> implements Argument<T>{
    public static final ParentDataArgument<?> INSTANCE=new ParentDataArgument<>();
    @Override
    public T resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        //TODO how cast?
        return wrapper.getData(parent);
    }
}
