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

    public abstract double resolve(Entity caster, Entity target);


    public static class Store extends Action {
        private NumberArgument toStore;
        private String id;

        @Override
        public int perform(MovesetWrapper wrapper, @Nullable TimerAction parent, Entity performer, Entity target) {
            final double vec = toStore.resolve(performer, target);
            performer.getPersistentData().putDouble(id, vec);
            return 0;
        }
    }

    public static class Get extends NumberArgument {
        private String id;

        @Override
        public double resolve(Entity caster, Entity target) {
            return caster.getPersistentData().getDouble(id);
        }
    }
}
