/*package gabereal.amogus.eliminate_players.mixin;

import gabereal.amogus.eliminate_players.EliminatePlayers;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
	@Inject(method = "getPlayerEntries", at = @At("RETURN"), cancellable = true)
	private void modifyPlayerList(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
		List<PlayerListEntry> entries = new ArrayList<>(cir.getReturnValue());
		entries.removeIf(entry -> EliminatePlayers.bannedUuids.contains(entry.getProfile().getId()));
		cir.setReturnValue(entries);
	}
}*/

//TODO: Check if this works because it throws a warning in the console.
