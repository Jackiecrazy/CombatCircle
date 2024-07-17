package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class EntityArgument implements Argument<Entity> {
    public abstract Entity resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target);

    public Vec3 _resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return resolve(wrapper, parent, caster, target).position();
    }

    public static class Store extends Action {
        private Argument<Entity> value;
        private String into;

        @Override
        public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
            final Entity vec = value.resolve(wrapper, parent, performer, target);
            performer.getPersistentData().putInt(into, vec.getId());
            return 0;
        }
    }

    public static class Get implements Argument<Entity> {
        private String from;

        @Override
        public Entity resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
            return caster.level().getEntity(caster.getPersistentData().getInt(from));
        }
    }
}
