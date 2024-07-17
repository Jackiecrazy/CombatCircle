package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SumVectorArgument extends VectorArgument{
    VectorArgument[] addends;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        Vec3 start=Vec3.ZERO;
        for(VectorArgument vec: addends){
            start=start.add(vec.resolve(wrapper, parent, caster, target));
        }
        return start;
    }
}
