package jackiecrazy.combatcircle.move.action;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.RawVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.FalseCondition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class PlayParticleAction extends Action {
    private ResourceLocation particle;
    private transient ParticleType play;
    private String particle_parameters = "";
    private Condition seen_by_player = TrueCondition.INSTANCE;
    private Condition force = FalseCondition.INSTANCE;
    private VectorArgument position = CasterEntityArgument.INSTANCE, direction = RawVectorArgument.ZERO;
    private NumberArgument quantity = NumberArgument.ZERO;

    @Override
    public int perform(MovesetWrapper wrapper, TimerAction parent, @Nullable Entity performer, Entity target) {
        if (play == null)
            play = ForgeRegistries.PARTICLE_TYPES.getValue(particle);
        if (play == null) return 0;
        //type/data, force, pos xyz, quantity, vel xyz, max speed
        ParticleOptions p;
        Vec3 pos = position.resolveAsVector(wrapper, parent, performer, target);
        Vec3 dir = direction.resolveAsVector(wrapper, parent, performer, target);
        try {
            p = play.getDeserializer().fromCommand(play, new StringReader(" " + particle_parameters));
        } catch (CommandSyntaxException cse) {
            throw new RuntimeException(cse);
        }
        if (target.level() instanceof ServerLevel sl) {
            for (ServerPlayer sp : sl.players()) {
                if (seen_by_player.resolve(wrapper, parent, performer, sp)) {
                    sl.sendParticles(sp, p, force.resolve(wrapper, parent, performer, target), pos.x, pos.y, pos.z, (int) quantity.resolve(wrapper, parent, performer, target), dir.x, dir.y, dir.z, dir.length());
                }
            }
        }
        return 0;
    }
    //position, type, direction if any
}
