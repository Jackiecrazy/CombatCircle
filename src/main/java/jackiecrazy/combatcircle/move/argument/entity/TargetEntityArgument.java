package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

public class TargetEntityArgument extends EntityArgument{
    public static final TargetEntityArgument INSTANCE=new TargetEntityArgument();
    @Override
    public Entity resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return target;
    }
}
