package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class OrCondition extends Condition {
    List<Condition> values;

    @Override
    public boolean resolve(MovesetWrapper wrapper, Entity performer, Entity target) {
        for(Condition c: values){
            if(c.resolve(wrapper, performer, target))return true;
        }
        return false;
    }
}
