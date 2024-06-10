package jackiecrazy.combatcircle.move.argument.vector;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SumVectorArgument extends VectorArgument{
    VectorArgument[] addends;

    @Override
    public Vec3 _resolve(Entity caster, Entity target) {
        Vec3 start=Vec3.ZERO;
        for(VectorArgument vec: addends){
            start=start.add(vec.resolveAsVector(caster, target));
        }
        return start;
    }
}
