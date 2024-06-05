package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.move.CircleEnums;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.EyePositionVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.LookVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import jackiecrazy.combatcircle.move.filter.Filter;
import jackiecrazy.combatcircle.move.filter.NoFilter;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SelectorArgument extends Argument {
    //base point
    private CircleEnums.SWEEPTYPE shape;
    private NumberArgument range;
    private NumberArgument width;
    private Filter selector;
    private VectorArgument position;
    private VectorArgument vector;

    public SelectorArgument() {
        shape = CircleEnums.SWEEPTYPE.CIRCLE;
        selector = NoFilter.INSTANCE;
        position = new EyePositionVectorArgument(CasterEntityArgument.INSTANCE);
        vector = new LookVectorArgument(CasterEntityArgument.INSTANCE);
    }

    public List<Entity> resolve(Entity caster, Entity target) {
        List<Entity> resolved = new ArrayList<>();
        Vec3 pos = position.resolve(caster, target);
        Vec3 look = vector.resolve(caster, target);
        double ra = range.resolve(caster, target);
        if (shape == CircleEnums.SWEEPTYPE.NONE) {
            if (GeneralUtils.getDistSqCompensated(target, pos) < ra * ra) resolved.add(target);
            return resolved;
        }
        double radius = width.resolve(caster, target);
        for (Entity ent : selector.filter(caster, target, caster.level().getEntities(null, new AABB(pos, pos).inflate(ra * 1.5)))) {
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
