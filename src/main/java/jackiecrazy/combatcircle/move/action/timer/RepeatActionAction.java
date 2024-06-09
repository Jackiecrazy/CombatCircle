package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class RepeatActionAction extends TimerAction {
    private int every;
    private List<Action> run = new ArrayList<>();

    @Override
    public int tick(MovesetWrapper wrapper, Entity performer, Entity target) {
        if (this.timer % every == 0) {
            int childRet = runActions(wrapper, this, run, performer, target);
            if (childRet != 0) return childRet;
        }
        return super.tick(wrapper, performer, target);
    }

    @Override
    public void reset() {
        for (Action child : run)
            child.reset();
        super.reset();
    }
}
