package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.move.CircleEnums;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.EyePositionVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.LookVectorArgument;
import jackiecrazy.combatcircle.move.filter.Filter;
import jackiecrazy.combatcircle.move.filter.NoFilter;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SelectorArgument implements Argument<List<Entity>> {
    //base point
    private CircleEnums.SWEEPTYPE shape;
    private Argument<Double> range= FixedNumberArgument.ZERO;
    private Argument<Double> width= FixedNumberArgument.ZERO;
    private Filter<Entity> filter;
    private Argument<Vec3> position;
    private Argument<Vec3> vector;

    public SelectorArgument() {
        shape = CircleEnums.SWEEPTYPE.CIRCLE;
        filter = NoFilter.INSTANCE;
        position = new EyePositionVectorArgument();
        vector = new LookVectorArgument();
    }

    public List<Entity> resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        List<Entity> resolved = new ArrayList<>();
        Vec3 pos = position.resolve(wrapper, parent, caster, target);
        Vec3 look = vector.resolve(wrapper, parent, caster, target);
        double ra = range.resolve(wrapper, parent, caster, target);
        if (shape == CircleEnums.SWEEPTYPE.NONE) {
            if (GeneralUtils.getDistSqCompensated(target, pos) < ra * ra) resolved.add(target);
            return resolved;
        }
        double radius = width.resolve(wrapper, parent, caster, target);
        for (Entity ent : filter.filter(wrapper, parent, caster, caster, target.level().getEntities(null, new AABB(pos, pos).inflate(ra * 1.5)))) {
            //type specific sweep checks
            switch (shape) {
                case CONE -> {
                    if (!GeneralUtils.isFacingEntity(pos, look, ent, (int) radius, 40)) continue;
                    if (GeneralUtils.getDistSqCompensated(ent, pos) > ra * ra) continue;
                }
                case CLEAVE -> {
                    if (!GeneralUtils.isFacingEntity(pos, look, ent, 40, (int) radius)) continue;
                    if (GeneralUtils.getDistSqCompensated(ent, pos) > ra * ra) continue;
                }
                case CIRCLE -> {
                    if (GeneralUtils.getDistSqCompensated(ent, pos) > radius * radius) continue;
                }
                case LINE -> {
                    Vec3 end = pos.add(look.normalize().scale(radius));
                    if (!ent.getBoundingBox().inflate(radius).intersects(pos, end)) continue;
                }
            }
            resolved.add(ent);
        }
        return resolved;
    }
}
