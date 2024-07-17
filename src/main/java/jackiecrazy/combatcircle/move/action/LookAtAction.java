package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class LookAtAction extends Action {
    private Argument<Entity> looker = CasterEntityArgument.INSTANCE;
    private Argument<Entity> entity_target;
    private Argument<Vec3> vector_target;
    private Argument<Double> head_rotation_x = new FixedNumberArgument(30);
    private Argument<Double> head_rotation_y = new FixedNumberArgument(30);
    private EntityAnchorArgument.Anchor anchor = EntityAnchorArgument.Anchor.EYES;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        Entity toLook = looker.resolve(wrapper, parent, performer, target);
        if (vector_target != null)
            toLook.lookAt(anchor, vector_target.resolve(wrapper, parent, performer, target));
        if (toLook instanceof Mob e && entity_target != null) {
            e.getLookControl().setLookAt(entity_target.resolve(wrapper, parent, performer, target), (float) head_rotation_x.resolve(wrapper, parent, performer, target).floatValue(), (float) head_rotation_y.resolve(wrapper, parent, performer, target).floatValue());
        }
        return 0;
    }
}
