package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.DamageArgument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.PositionVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.FalseCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ExplodeAction extends Action {

    private NumberArgument damage;//todo use custom explosion class
    private NumberArgument radius;
    private DamageArgument damage_source;
    private VectorArgument position = new PositionVectorArgument();
    private EntityArgument exploder = CasterEntityArgument.INSTANCE;

    private Condition self_damage = FalseCondition.INSTANCE;
    private Condition fire = FalseCondition.INSTANCE;
    private Level.ExplosionInteraction griefing = Level.ExplosionInteraction.NONE;
    private List<Action> on_hit = new ArrayList<>();
    private List<Action> on_damage = new ArrayList<>();

    @Override
    public int perform(MovesetWrapper wrapper, @Nullable TimerAction parent, Entity performer, Entity target) {
        Vec3 pos = position.resolveAsVector(wrapper, parent, performer, target);
        performer.level().explode(exploder.resolveAsEntity(wrapper, parent, performer, target), damage_source.bake(wrapper, parent, performer, target), null, pos.x, pos.y, pos.z, (float) radius.resolve(wrapper, parent, performer, target), fire.evaluate(wrapper, parent, performer, target), griefing);
        return 0;
    }
}
