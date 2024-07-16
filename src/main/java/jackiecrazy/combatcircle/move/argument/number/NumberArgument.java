package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public abstract class NumberArgument extends Argument {
    public static final FixedNumberArgument ZERO = new FixedNumberArgument(0);
    public static final FixedNumberArgument ONE = new FixedNumberArgument(1);
    public static final FixedNumberArgument MAX = new FixedNumberArgument(Integer.MAX_VALUE);
    public static final FixedNumberArgument MIN = new FixedNumberArgument(Integer.MIN_VALUE);

    public abstract double resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target);


    public static class Store extends Action {
        private NumberArgument value;
        private String into;

        @Override
        public int perform(MovesetWrapper wrapper, TimerAction parent, @Nullable Entity performer, Entity target) {
            final double vec = value.resolve(wrapper, parent, performer, target);
            performer.getPersistentData().putDouble(into, vec);
            return 0;
        }
    }

    public static class Get extends NumberArgument {
        private String from;

        @Override
        public double resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
            return caster.getPersistentData().getDouble(from);
        }
    }
}
