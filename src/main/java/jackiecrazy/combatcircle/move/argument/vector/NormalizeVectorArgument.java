package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class NormalizeVectorArgument extends VectorArgument{
    VectorArgument from;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        return from.resolveAsVector(wrapper, caster, target).normalize();
    }
}
