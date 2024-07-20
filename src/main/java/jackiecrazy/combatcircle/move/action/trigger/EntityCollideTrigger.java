package jackiecrazy.combatcircle.move.action.trigger;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.footwork.utils.MovementUtils;
import net.minecraft.world.entity.Entity;

public class EntityCollideTrigger extends Trigger{
    @Override
    public boolean canRun(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        MovementUtils.collidingEntity(performer);
        return super.canRun(wrapper, parent, performer, target);
    }
}
