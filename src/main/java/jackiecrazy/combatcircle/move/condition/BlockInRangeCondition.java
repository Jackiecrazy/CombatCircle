package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.PositionVectorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class BlockInRangeCondition extends Condition {
    private Argument<ResourceLocation> block;
    private Condition testAsTag;
    private transient Block bloc;
    private transient TagKey<Block> tag;
    private Argument<Double> range, min_count = FixedNumberArgument.ZERO;
    private Argument<Vec3> from = PositionVectorArgument.CASTER;

    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        boolean testTag=testAsTag.resolve(wrapper, parent, performer, target);
        if (testTag) {
            if (tag == null)
                tag = new TagKey<>(ForgeRegistries.BLOCKS.getRegistryKey(), block.resolve(wrapper, parent, performer, target));
        } else if (bloc == null)
            bloc = ForgeRegistries.BLOCKS.getValue(block.resolve(wrapper, parent, performer, target));
        Vec3 pos = from.resolve(wrapper, parent, performer, target);
        double radius = range.resolve(wrapper, parent, performer, target);
        return min_count.resolve(wrapper, parent, performer, target) <= performer.level().getBlockStatesIfLoaded(new AABB(pos, pos).inflate(radius)).filter(bs -> testTag ? bs.is(tag) : bs.is(bloc)).count();
    }
}
