package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.capability.MovesetData;
import jackiecrazy.combatcircle.move.Moves;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AttachActionAction extends Action {
    private Argument<Entity> performer = CasterEntityArgument.INSTANCE;
    private Argument<Entity> recipient = TargetEntityArgument.INSTANCE;
    private ResourceLocation effect;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity perform, Entity target) {
        ArrayList<TimerAction> timers = new ArrayList<>(List.of(JsonAdapters.gson.fromJson(Moves.moves.get(effect), TimerAction[].class)));
        MovesetData.getCap(recipient.resolve(wrapper, parent, perform, target)).mark(performer.resolve(wrapper, parent, perform, target), new MovesetWrapper(timers));
        return 0;
    }
}
