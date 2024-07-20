package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.vector.RawVectorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AddVelocityAction extends TimerAction {
    private List<Action> on_launch=new ArrayList<>();
    private List<Action> tick=new ArrayList<>();
    private List<Action> on_land=new ArrayList<>();
    private Argument<Vec3> direction= RawVectorArgument.ZERO;

    @Override
    public void start(MovesetWrapper wrapper, Entity performer, Entity target) {
        runActions(wrapper, this, on_launch, performer, target);
        Vec3 dir = direction.resolve(wrapper, this, performer, target);
        performer.addDeltaMovement(dir);
        wrapper.setData(this, false);
        if (dir.y > 0) {
            performer.setOnGround(false);
            wrapper.setData(this, true);
        }
        super.start(wrapper, performer, target);
    }

    @Override
    public int tick(MovesetWrapper wrapper, Entity performer, Entity target) {
        int childRet = runActions(wrapper, this, tick, performer, target);
        if (childRet != 0) return childRet;
        if (!performer.onGround()) {
            wrapper.setData(this, true);
        } else if (wrapper.getData(this)) {
            wrapper.setData(this, false);
            childRet = runActions(wrapper, this, on_land, performer, target);
            if (childRet != 0) return childRet;
        }
        return super.tick(wrapper, performer, target);
    }

    public void stop(MovesetWrapper wrapper, Entity performer, Entity target, boolean recursive) {
        if(recursive){
            on_launch.forEach(a->a.stop(wrapper, performer, target, true));
            tick.forEach(a->a.stop(wrapper, performer, target, true));
            on_land.forEach(a->a.stop(wrapper, performer, target, true));
        }
        super.stop(wrapper, performer, target, recursive);
    }

}
