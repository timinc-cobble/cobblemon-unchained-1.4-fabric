package us.timinc.mc.cobblemon.chaining.mixins;

import com.cobblemon.mod.common.api.spawning.context.SpawningContext;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnAction;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import us.timinc.mc.cobblemon.chaining.Chaining;

import java.util.List;

@Mixin(value = PokemonSpawnAction.class, remap = false)
public abstract class PokemonSpawnActionMixin {
    @Inject(method = "createEntity()Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void modifyCreateEntityReturn(CallbackInfoReturnable<PokemonEntity> cir, List<ItemStack> list, ItemStack itemStack, PokemonEntity entity) {
        SpawningContext ctx = ((PokemonSpawnAction) (Object) this).getCtx();
        Chaining.INSTANCE.possiblyModifyIvs(entity, ctx);
        Chaining.INSTANCE.possiblyAddHiddenAbility(entity, ctx);
        Chaining.INSTANCE.possiblyMakeShiny(entity, ctx);
        Chaining.INSTANCE.possiblySynchronizeNatures(entity, ctx);
    }
}