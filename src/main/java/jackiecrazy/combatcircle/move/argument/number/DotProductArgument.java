package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.world.entity.Entity;

public class DotProductArgument extends NumberArgument {
    private VectorArgument first, second;

    @Override
    public double resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
        return first.resolveAsVector(wrapper, parent, caster, target).dot(second.resolveAsVector(wrapper, parent, caster, target));
    }
}
