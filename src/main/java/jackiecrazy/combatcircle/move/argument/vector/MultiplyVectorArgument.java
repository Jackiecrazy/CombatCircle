package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MultiplyVectorArgument extends VectorArgument {
    VectorArgument base;
    VectorArgument multiply;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        return base.resolveAsVector(wrapper, caster, target).multiply(multiply.resolveAsVector(wrapper, caster, target));
    }
}
