package gabereal.amogus.eliminate_players

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

object EliminatePlayers : ModInitializer {
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("EliminatePlayers")

    @JvmField
    // val bannedUuids: MutableList<UUID> = mutableListOf()
    val bannedUuids: ArrayList<UUID> = ArrayList()

    override fun onInitialize(mod: ModContainer) {
        bannedUuids.add(UUID.fromString("d0815131-c51a-4831-b973-f69da01e6326"))
        // bannedUuids.addAll(EliminatePlayersStorage.loadBannedUUIDs())

        // LOGGER.info("Loaded ${bannedUuids.size} banned UUID(s).")
        // LOGGER.info("EliminatePlayers initialized with ${bannedUuids.size} banned player(s).")
        LOGGER.info("Hello Quilt world from ${mod.metadata().name()}!")
    }
}
