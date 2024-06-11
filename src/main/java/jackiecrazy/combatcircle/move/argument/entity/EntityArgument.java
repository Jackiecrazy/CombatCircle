package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class EntityArgument extends VectorArgument {
    public abstract Entity resolveAsEntity(MovesetWrapper wrapper, Entity caster, Entity target);

    public Vec3 _resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        return resolveAsEntity(wrapper, caster, target).position();
    }

    public static class Store extends Action {
        private EntityArgument toStore;
        private String into;

        @Override
        public int perform(MovesetWrapper wrapper, @Nullable Entity performer, Entity target) {
            final Entity vec = toStore.resolveAsEntity(wrapper, performer, target);
            performer.getPersistentData().putInt(into, vec.getId());
            return 0;
        }
    }

    public static class Get extends EntityArgument {
        private String from;

        @Override
        public Entity resolveAsEntity(MovesetWrapper wrapper, Entity caster, Entity target) {
            return caster.level().getEntity(caster.getPersistentData().getInt(from));
        }
    }
}
