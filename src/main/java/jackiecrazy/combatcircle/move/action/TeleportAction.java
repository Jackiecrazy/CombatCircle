package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeleportAction extends Action {
    private Argument<Entity> subject= CasterEntityArgument.INSTANCE;
    private List<Action> on_start=new ArrayList<>();
    private List<Action> on_land=new ArrayList<>();
    private Argument<Vec3> position;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        Vec3 vec=position.resolve(wrapper, parent, performer, target);
        Entity teleporter = subject.resolve(wrapper, parent, performer, target);
        runActions(wrapper, parent, on_start, performer, teleporter);
        teleporter.teleportTo(vec.x,vec.y, vec.z);
        runActions(wrapper, parent, on_land, performer, teleporter);
        return 0;
    }
}
