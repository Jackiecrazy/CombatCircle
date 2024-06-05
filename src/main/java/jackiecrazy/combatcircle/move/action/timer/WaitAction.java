package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class WaitAction extends TimerAction {
    private List<Action> waiting = new ArrayList<>();

    @Override
    public int tick(Entity performer, Entity target) {
        int childRet=runActions(this, waiting, performer, target);
        if(childRet!=0)return childRet;
        return super.tick(performer, target);
    }

    @Override
    public void reset() {
        for (Action child : waiting)
            child.reset();
        super.reset();
    }
}
