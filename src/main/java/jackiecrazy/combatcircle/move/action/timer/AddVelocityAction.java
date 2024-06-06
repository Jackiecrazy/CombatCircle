package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AddVelocityAction extends TimerAction {
    private List<Action> on_launch;
    private List<Action> tick;
    private List<Action> on_land;
    private VectorArgument direction;
    private transient boolean flying;

    @Override
    public boolean isFinished(Entity performer, Entity target) {
        return super.isFinished(performer, target);
    }

    @Override
    public void start(Entity performer, Entity target) {
        runActions(this, on_launch, performer, target);//fixme doesn't proc continuous tasks
        Vec3 dir = direction.resolve(performer, target);
        performer.addDeltaMovement(dir);
        if(dir.y>0){
            performer.setOnGround(false);
            flying=true;
        }
        super.start(performer, target);
    }

    @Override
    public int tick(Entity performer, Entity target) {
        int childRet = runActions(this, tick, performer, target);
        if (childRet != 0) return childRet;
        if(!performer.onGround()){
            flying=true;
        }else if(flying){
            flying=false;
            childRet = runActions(this, on_land, performer, target);
            if (childRet != 0) return childRet;
        }
        return super.tick(performer, target);
    }

    @Override
    public void reset() {
        for (Action child : on_launch)
            child.reset();
        for (Action child : tick)
            child.reset();
        for (Action child : on_land)
            child.reset();
        super.reset();
    }
}
