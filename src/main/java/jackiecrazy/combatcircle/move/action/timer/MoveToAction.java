package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class MoveToAction extends TimerAction {
    private List<Action> on_start = new ArrayList<>();
    private List<Action> tick = new ArrayList<>();
    private NumberArgument speed_modifier = FixedNumberArgument.ONE;
    private VectorArgument position;

    @Override
    public void start(MovesetWrapper wrapper, Entity performer, Entity target) {
        runActions(wrapper, this, on_start, performer, target);//fixme doesn't proc continuous tasks
        Vec3 moveTo = position.resolveAsVector(wrapper, this, performer, target);
        if (performer instanceof Mob m) {
            Path path = m.getNavigation().createPath(moveTo.x, moveTo.y, moveTo.z, 0);
            if (path != null) {
                wrapper.setData(this, path);
                m.getNavigation().moveTo(path, speed_modifier.resolve(wrapper, this, performer, target));
            }
        }
        //m.getMoveControl().setWantedPosition(dir.x, dir.y, dir.z, speed_modifier.resolve(wrapper, this, performer, target));
        super.start(wrapper, performer, target);
    }

    @Override
    public boolean isFinished(MovesetWrapper wrapper, Entity performer, Entity target) {
        Path p = wrapper.getData(this);
        return p.isDone() || super.isFinished(wrapper, performer, target);
    }

    @Override
    public int tick(MovesetWrapper wrapper, Entity performer, Entity target) {
        int childRet = runActions(wrapper, this, tick, performer, target);
        if (childRet != 0) return childRet;
        return super.tick(wrapper, performer, target);
    }

    public void stop(MovesetWrapper wrapper, Entity performer, Entity target, boolean recursive) {
        if (performer instanceof Mob m)
            m.getNavigation().stop();
        if (recursive) {
            on_start.forEach(a -> a.stop(wrapper, performer, target, true));
            tick.forEach(a -> a.stop(wrapper, performer, target, true));
        }
        super.stop(wrapper, performer, target, recursive);
    }

}
