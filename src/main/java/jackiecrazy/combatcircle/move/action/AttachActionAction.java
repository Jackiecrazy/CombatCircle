package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.Moves;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import jackiecrazy.combatcircle.move.capability.Marks;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AttachActionAction extends Action {
    private EntityArgument performer = CasterEntityArgument.INSTANCE;
    private EntityArgument recipient = TargetEntityArgument.INSTANCE;
    private ResourceLocation effect;

    @Override
    public int perform(@Nullable TimerAction parent, Entity perform, Entity target) {
        ArrayList<TimerAction> timers = new ArrayList<>(List.of(JsonAdapters.gson.fromJson(Moves.moves.get(effect), TimerAction[].class)));
        Marks.getCap(recipient.resolve(perform, target)).mark(performer.resolve(perform, target), new MovesetWrapper(timers));
        return 0;
    }
}
