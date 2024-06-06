package jackiecrazy.combatcircle.move;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

public class Movesets extends SimpleJsonResourceReloadListener {
    public static final HashMap<EntityType<?>, MovesetFactory[]> moves = new HashMap<>();

    public Movesets() {
        super(JsonAdapters.gson, "combat_circle_movesets");
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new Movesets());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        moves.clear();
        object.forEach((key, value) -> {
            JsonObject file = value.getAsJsonObject();
            CombatCircle.LOGGER.debug("loading mob moveset definition found under {}", key);
            try {
                EntityType<?> et = ForgeRegistries.ENTITY_TYPES.getValue(JsonAdapters.gson.fromJson(file.get("mob"), ResourceLocation.class));
                MovesetFactory[] mf = JsonAdapters.gson.fromJson(file.get("moveset"), MovesetFactory[].class);
                for (MovesetFactory msf : mf)
                    if (!msf.validate())
                        CombatCircle.LOGGER.error("{} is an invalid moveset factory!", key);
                moves.merge(et, mf, ArrayUtils::addAll);
            } catch (Exception e) {
                CombatCircle.LOGGER.error("{} is an invalid moveset definition!", key);
                e.printStackTrace();
            }
        });
    }
}
