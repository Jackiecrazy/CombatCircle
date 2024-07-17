package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.LookVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.PositionVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.RawVectorArgument;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpawnEntityAction extends Action {
    private Argument<Entity> summoner = CasterEntityArgument.INSTANCE;
    private ResourceLocation entity;
    private CompoundTag tag;
    private Argument<Double> quantity = FixedNumberArgument.ONE;
    private Argument<Double> spread = FixedNumberArgument.ZERO;
    private Argument<Vec3> position = PositionVectorArgument.CASTER;
    private Argument<Vec3> facing = new LookVectorArgument();
    private Argument<Vec3> velocity = RawVectorArgument.ZERO;
    private List<Action> on_spawn = new ArrayList<>();

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        Entity summoner = this.summoner.resolve(wrapper, parent, performer, target);
        int toSpawn = quantity.resolve(wrapper, parent, performer, target).intValue();
        double deviation = spread.resolve(wrapper, parent, performer, target);
        Vec3 vec = position.resolve(wrapper, parent, performer, target);
        Level level = performer.level();
        if (level instanceof ServerLevel serverlevel) {
            for (int x = 0; x < toSpawn; x++) {
                Vec3 rand = new Vec3((CombatCircle.rand.nextDouble()) * deviation, (CombatCircle.rand.nextDouble()) * deviation, (CombatCircle.rand.nextDouble()) * deviation);
                final Vec3 pos = vec.add(rand);
                CompoundTag compoundtag = tag == null ? new CompoundTag() : tag.copy();
                compoundtag.putString("id", entity.toString());
                Vec3 look = facing.resolve(wrapper, parent, performer, target);
                Entity summon = EntityType.loadEntityRecursive(compoundtag, serverlevel, (toSummon) -> {
                    double flatDist = Math.sqrt(look.x * look.x + look.z * look.z);
                    toSummon.moveTo(pos.x, pos.y, pos.z, (float) Mth.wrapDegrees(GeneralUtils.deg((float) Mth.atan2(look.z, look.x)) - 90.0F), Mth.wrapDegrees(-GeneralUtils.deg((float) Mth.atan2(look.y, flatDist))));
                    if (toSummon instanceof Projectile p) {
                        p.setOwner(summoner);
                    }
                    if (toSummon instanceof TamableAnimal p) {
                        p.setOwnerUUID(summoner.getUUID());
                    }
                    toSummon.setDeltaMovement(velocity.resolve(wrapper, parent, performer, target));
                    return toSummon;
                });
                if (summon != null) {
                    if (tag == null && summon instanceof Mob m) {
                        m.finalizeSpawn(serverlevel, serverlevel.getCurrentDifficultyAt(summon.blockPosition()), MobSpawnType.COMMAND, null, null);
                    }

                    serverlevel.tryAddFreshEntityWithPassengers(summon);
                    for (Action a : on_spawn) {
                        a.perform(wrapper, parent, summoner, summon);
                    }

                }
            }
        }
        return 0;
    }
}
