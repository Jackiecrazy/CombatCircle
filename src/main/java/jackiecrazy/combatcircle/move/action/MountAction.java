package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class MountAction extends Action{
    private Argument<Entity> mounter = CasterEntityArgument.INSTANCE;
    private Argument<Entity> mount = TargetEntityArgument.INSTANCE;
    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        mounter.resolve(wrapper, parent, performer, target).startRiding(mount.resolve(wrapper, parent, performer, target), true);
        return 0;
    }
}
