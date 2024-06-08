package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class MoveToAction extends TimerAction {
    private List<Action> on_start=new ArrayList<>();
    private List<Action> tick=new ArrayList<>();
    private NumberArgument speed_modifier= FixedNumberArgument.ONE;
    private VectorArgument position;

    @Override
    public boolean isFinished(Entity performer, Entity target) {
        return super.isFinished(performer, target);
    }

    @Override
    public void start(Entity performer, Entity target) {
        runActions(this, on_start, performer, target);//fixme doesn't proc continuous tasks
        Vec3 dir = position.resolve(performer, target);
        if (performer instanceof Mob m)
            m.getMoveControl().setWantedPosition(dir.x, dir.y, dir.z, speed_modifier.resolve(performer, target));
        super.start(performer, target);
    }

    @Override
    public int tick(Entity performer, Entity target) {
        int childRet = runActions(this, tick, performer, target);
        if (childRet != 0) return childRet;
        return super.tick(performer, target);
    }

    @Override
    public void reset() {
        for (Action child : on_start)
            child.reset();
        for (Action child : tick)
            child.reset();
        super.reset();
    }
}
