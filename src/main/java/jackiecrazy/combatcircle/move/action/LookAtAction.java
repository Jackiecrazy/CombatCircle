package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;

public class LookAtAction extends Action {
    private EntityArgument looker = CasterEntityArgument.INSTANCE;
    private EntityArgument entity_target;
    private VectorArgument vector_target;
    private NumberArgument head_rotation_x = new FixedNumberArgument(30);
    private NumberArgument head_rotation_y = new FixedNumberArgument(30);
    private EntityAnchorArgument.Anchor anchor = EntityAnchorArgument.Anchor.EYES;

    @Override
    public int perform(@Nullable TimerAction parent, Entity performer, Entity target) {
        Entity toLook = looker.resolve(performer, target);
        if (vector_target != null)
            toLook.lookAt(anchor, vector_target.resolve(performer, target));
        if (toLook instanceof Mob e && entity_target != null) {
            e.getLookControl().setLookAt(entity_target.resolve(performer, target), (float) head_rotation_x.resolve(performer, target), (float) head_rotation_y.resolve(performer, target));
        }
        return 0;
    }
}
