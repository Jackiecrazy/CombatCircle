package jackiecrazy.combatcircle.ai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class MoveManager extends JsonReloadListener {
    /*
    moveset
      condition
      cooldown
      priority
      weight
      force abort time
      move sequence
        move
          inherits (so you can point it to another move and override as needed)
          initiation: point action
          perform: timed action
          recovery: point action

    actions:

    action over time:
      time
      interval
      damage interrupt threshold
      posture interrupt threshold
      cancel condition (default null)
      can turn
      can move
      can perform other moves
      tick action
      interrupt action
      cancel action
    area of effect:
      conditions
      action
    add potion effect
    set fire
    record damage
    knockback
    damage
      condition
      color
      windup time
      damage time
      cooldown time
      shape:
        origin: user/target
        ray:
          distance
          width
          angle offset
          distance offset
          action on impact
          piercing
        circle:
          radius
          distance offset
        cone:
          horizontal angle
          vertical angle
          angle offset
          distance offset
      action on attack
      action on damage
      indiscriminate
    summon mob
      mob type
      distance offset
      angle offset
    shoot projectile
      projectile type
      distance offset
      angle offset
    place block
    emit particles
    play sound
    stall
      time
    invoke other move (both on others and self)
      recursion limit

    conditions:
      number comparison
      recorded damage
      health
        percentage
        flat
      entity in area
        type
        count
        designation (target/ally)
        condition
      and
      or
      not

    offsets:
      vector angle/distance
      frame of reference
      random x/y/z bounds

    if it reads condition or action, feed the json object to a registry of conditions/actions, each with their own codec
     */
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public MoveManager() {
        super(GSON, "combatcircle/move");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> json, IResourceManager ign, IProfiler ored) {

    }
}
