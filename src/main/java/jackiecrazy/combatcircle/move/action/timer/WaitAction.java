package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class WaitAction extends TimerAction {
    private List<Action> waiting = new ArrayList<>();

    @Override
    public int tick(MovesetWrapper wrapper, Entity performer, Entity target) {
        int childRet=runActions(wrapper, waiting, performer, target);
        if(childRet!=0)return childRet;
        return super.tick(wrapper, performer, target);
    }

    @Override
    public void reset() {
        for (Action child : waiting)
            child.reset();
        super.reset();
    }
}
