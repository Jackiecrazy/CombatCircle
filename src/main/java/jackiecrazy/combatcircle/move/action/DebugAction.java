package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class DebugAction extends Action {
    private Argument<?> parameter;
    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        System.out.println("here are the performer and target:");
        System.out.println(performer);
        System.out.println(target);
        if (parameter != null)
            System.out.println("the parameter resolves to " + parameter.resolve(wrapper, parent, performer, target).toString());
        return 0;
    }
}
