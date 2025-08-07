package dev.gabereal.eplayers.mixin;

import dev.gabereal.eplayers.EliminatePlayers;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    private List<PlayerListEntry> eplayers$modifyDeathMessage(List<PlayerListEntry> list) {
        list.removeIf((entry) -> EliminatePlayers.bannedUuids.contains(entry.getProfile().getId()));
        return list;
    }
}