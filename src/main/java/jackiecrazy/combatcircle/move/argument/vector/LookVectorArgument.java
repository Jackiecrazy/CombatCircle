package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class LookVectorArgument extends VectorArgument{
    EntityArgument reference_point=CasterEntityArgument.INSTANCE;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        return reference_point.resolveAsEntity(wrapper, caster, target).getLookAngle();
    }
}
