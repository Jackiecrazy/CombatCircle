package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RawVectorArgument extends VectorArgument {
    NumberArgument x, y, z;
    transient Vec3 vec;

    @Override
    public Vec3 _resolve(Entity caster, Entity target) {
        if (vec == null) vec = new Vec3(x.resolve(caster, target), y.resolve(caster, target), z.resolve(caster, target));
        return vec;
    }
}
