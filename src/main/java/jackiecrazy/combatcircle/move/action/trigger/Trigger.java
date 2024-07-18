package jackiecrazy.combatcircle.move.action.trigger;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Trigger extends Action {
    private List<Action> execute = new ArrayList<>();

    @Override
    public void stop(MovesetWrapper wrapper, Entity performer, Entity target, boolean recursive) {
        if (recursive) {
            execute.forEach(a -> a.stop(wrapper, performer, target, true));
        }
        super.stop(wrapper, performer, target, recursive);
    }

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        runActions(wrapper, parent, execute, performer, target);
        return 0;
    }
}
