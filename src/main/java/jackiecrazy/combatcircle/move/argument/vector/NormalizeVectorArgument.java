package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class NormalizeVectorArgument extends VectorArgument{
    VectorArgument from;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
        return from.resolveAsVector(wrapper, parent, caster, target).normalize();
    }
}
