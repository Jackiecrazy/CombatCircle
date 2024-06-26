package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class CrossProductArgument extends VectorArgument {
    private VectorArgument first, second;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        return first.resolveAsVector(wrapper, caster, target).cross(second.resolveAsVector(wrapper, caster, target));
    }
}
