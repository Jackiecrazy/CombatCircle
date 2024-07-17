package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.DamageArgument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.vector.PositionVectorArgument;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.FalseCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ExplodeAction extends Action {

    private Argument<Double> damage;//todo use custom explosion class
    private Argument<Double> radius;
    private DamageArgument damage_source;
    private Argument<Vec3> position = new PositionVectorArgument();
    private Argument<Entity> exploder = CasterEntityArgument.INSTANCE;

    private Condition self_damage = FalseCondition.INSTANCE;
    private Condition fire = FalseCondition.INSTANCE;
    private Level.ExplosionInteraction griefing = Level.ExplosionInteraction.NONE;
    private List<Action> on_hit = new ArrayList<>();
    private List<Action> on_damage = new ArrayList<>();

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        Vec3 pos = position.resolve(wrapper, parent, performer, target);
        performer.level().explode(exploder.resolve(wrapper, parent, performer, target), damage_source.resolve(wrapper, parent, performer, target), null, pos.x, pos.y, pos.z, radius.resolve(wrapper, parent, performer, target).floatValue(), fire.resolve(wrapper, parent, performer, target), griefing);
        return 0;
    }
}
