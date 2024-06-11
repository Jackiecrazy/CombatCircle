package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeleportAction extends Action {
    private EntityArgument subject= CasterEntityArgument.INSTANCE;
    private List<Action> on_start=new ArrayList<>();
    private List<Action> on_land=new ArrayList<>();
    private VectorArgument position;

    @Override
    public int perform(MovesetWrapper wrapper, @Nullable Entity performer, Entity target) {
        Vec3 vec=position.resolveAsVector(wrapper, performer, target);
        Entity teleporter = subject.resolveAsEntity(wrapper, performer, target);
        runActions(wrapper, on_start, performer, teleporter);
        teleporter.teleportTo(vec.x,vec.y, vec.z);
        runActions(wrapper, on_land, performer, teleporter);
        return 0;
    }
}
