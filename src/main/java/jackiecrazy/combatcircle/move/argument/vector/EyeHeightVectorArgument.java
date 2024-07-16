package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EyeHeightVectorArgument extends VectorArgument {
    private EntityArgument reference_point;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
        return new Vec3(0, reference_point.resolveAsEntity(wrapper, parent, caster, target).getEyeY(), 0);
    }
}
