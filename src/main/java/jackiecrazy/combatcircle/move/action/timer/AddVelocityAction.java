package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class AddVelocityAction extends TimerAction {
    private List<Action> on_launch;
    private List<Action> while_flying;
    private List<Action> on_land;
    private VectorArgument direction;
    private transient boolean flying;

    @Override
    public boolean isFinished(Entity performer, Entity target) {
        return super.isFinished(performer, target);
    }

    @Override
    public void start(Entity performer, Entity target) {
        runActions(this, on_launch, performer, target);
        performer.addDeltaMovement(direction.resolve(performer, target));
        super.start(performer, target);
    }

    @Override
    public int tick(Entity performer, Entity target) {
        if(!performer.onGround()){
            flying=true;
            int childRet = runActions(this, while_flying, performer, target);
            if (childRet != 0) return childRet;
        }else if(flying){
            flying=false;
            int childRet = runActions(this, on_land, performer, target);
            if (childRet != 0) return childRet;
        }
        return super.tick(performer, target);
    }

    @Override
    public void reset() {
        for (Action child : on_launch)
            child.reset();
        for (Action child : while_flying)
            child.reset();
        for (Action child : on_land)
            child.reset();
        super.reset();
    }
}
