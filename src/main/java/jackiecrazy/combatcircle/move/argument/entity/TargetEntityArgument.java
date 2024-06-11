package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class TargetEntityArgument extends EntityArgument{
    public static final TargetEntityArgument INSTANCE=new TargetEntityArgument();
    @Override
    public Entity resolveAsEntity(MovesetWrapper wrapper, Entity caster, Entity target) {
        return target;
    }
}
