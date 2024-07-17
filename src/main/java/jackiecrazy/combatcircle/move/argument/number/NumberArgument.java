package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public abstract class NumberArgument implements Argument<Double> {

    public abstract Double resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target);


    public static class Store extends Action {
        private Argument<Double> value;
        private String into;

        @Override
        public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
            final double vec = value.resolve(wrapper, parent, performer, target);
            performer.getPersistentData().putDouble(into, vec);
            return 0;
        }
    }

    public static class Get implements Argument<Double> {
        private String from;

        @Override
        public Double resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
            return caster.getPersistentData().getDouble(from);
        }
    }
}
