package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class NormalizeVectorArgument extends VectorArgument{
    Argument<Vec3> from;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return from.resolve(wrapper, parent, caster, target).normalize();
    }
}
