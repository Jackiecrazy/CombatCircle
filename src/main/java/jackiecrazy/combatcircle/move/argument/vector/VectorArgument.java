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

    public Vec3 resolveAsVector(Entity caster, Entity target) {
        double minLength = min_length.resolve(caster, target);
        double maxLength = max_length.resolve(caster, target);
        double sc = scale.resolve(caster, target);
        Vec3 ret = _resolve(caster, target).scale(sc);
        if (minLength > 0 && ret.lengthSqr() < minLength * minLength) {
            ret=ret.scale(minLength / ret.length());
        }
        if (maxLength > 0 && ret.lengthSqr() > maxLength * maxLength) {
            ret=ret.scale(maxLength / ret.length());
        }
        return ret;
    }

    public abstract Vec3 _resolve(Entity caster, Entity target);

    public static class Store extends Action{
        private VectorArgument toStore;
        private String id;
        @Override
        public int perform(MovesetWrapper wrapper, @Nullable TimerAction parent, Entity performer, Entity target) {
            final Vec3 vec=toStore.resolveAsVector(performer, target);
            performer.getPersistentData().putDouble(id+"_x", vec.x);
            performer.getPersistentData().putDouble(id+"_y", vec.y);
            performer.getPersistentData().putDouble(id+"_z", vec.z);
            return 0;
        }
    }

    public static class Get extends VectorArgument{
        private String id;

        @Override
        public Vec3 _resolve(Entity caster, Entity target) {
            return new Vec3(caster.getPersistentData().getDouble(id+"_x"), caster.getPersistentData().getDouble(id+"_y"), caster.getPersistentData().getDouble(id+"_z"));
        }
    }
}
