package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IfElseAction extends Action{
    private List<Action> then=new ArrayList<>();
    private List<Action> otherwise=new ArrayList<>();

    @Override
    public boolean canRun(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        return true;
    }

    @Override
    public boolean repeatable(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        return true;
    }

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        if(condition.resolve(wrapper, parent, performer, target)){
            return runActions(wrapper, parent, then, performer, target);
        }else return runActions(wrapper, parent, otherwise, performer, target);
    }
}
