package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class VectorArgument extends Argument {
    private NumberArgument scale = NumberArgument.ONE;
    private NumberArgument min_length = NumberArgument.ZERO;
    private NumberArgument max_length = NumberArgument.ZERO;

    public Vec3 resolveAsVector(MovesetWrapper wrapper, Entity caster, Entity target) {
        double minLength = min_length.resolve(wrapper, caster, target);
        double maxLength = max_length.resolve(wrapper, caster, target);
        double sc = scale.resolve(wrapper, caster, target);
        Vec3 ret = _resolve(wrapper, caster, target).scale(sc);
        if (minLength > 0 && ret.lengthSqr() < minLength * minLength) {
            ret=ret.scale(minLength / ret.length());
        }
        if (maxLength > 0 && ret.lengthSqr() > maxLength * maxLength) {
            ret=ret.scale(maxLength / ret.length());
        }
        return ret;
    }

    public abstract Vec3 _resolve(MovesetWrapper wrapper, Entity caster, Entity target);

    public static class Store extends Action{
        private VectorArgument toStore;
        private String into;
        @Override
        public int perform(MovesetWrapper wrapper, @Nullable Entity performer, Entity target) {
            final Vec3 vec=toStore.resolveAsVector(wrapper, performer, target);
            performer.getPersistentData().putDouble(into +"_x", vec.x);
            performer.getPersistentData().putDouble(into +"_y", vec.y);
            performer.getPersistentData().putDouble(into +"_z", vec.z);
            return 0;
        }
    }

    public static class Get extends VectorArgument{
        private String from;

        @Override
        public Vec3 _resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
            return new Vec3(caster.getPersistentData().getDouble(from +"_x"), caster.getPersistentData().getDouble(from +"_y"), caster.getPersistentData().getDouble(from +"_z"));
        }
    }
}
