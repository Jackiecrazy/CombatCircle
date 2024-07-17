package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class StopAction extends Action {
    boolean recursive=true;
    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        parent.stop(wrapper, performer, target, recursive);
        return -1;
    }
}
