package jackiecrazy.combatcircle.move.argument.number;

import net.minecraft.world.entity.Entity;

public class SumArgument extends NumberArgument{
    private NumberArgument[] addends;
    @Override
    public double resolve(Entity caster, Entity target) {
        double ret=0;
        for(NumberArgument args:addends){
            ret+=args.resolve(caster, target);
        }
        return ret;
    }
}
