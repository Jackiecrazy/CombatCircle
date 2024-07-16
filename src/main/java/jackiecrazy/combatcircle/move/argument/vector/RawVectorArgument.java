package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RawVectorArgument extends VectorArgument {
    public static final VectorArgument ZERO = new VectorArgument() {

        @Override
        public Vec3 _resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
            return Vec3.ZERO;
        }
    };
    NumberArgument x, y, z;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
        return new Vec3(x.resolve(wrapper, parent, caster, target), y.resolve(wrapper, parent, caster, target), z.resolve(wrapper, parent, caster, target));
    }
}
