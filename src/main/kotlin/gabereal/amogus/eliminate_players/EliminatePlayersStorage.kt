package gabereal.amogus.eliminate_players

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

object EliminatePlayersStorage {
    private val GSON = Gson()
    private val STORAGE_FILE = File("resources/config/removed.json")

    fun saveBannedUUIDs(uuids: Collection<UUID>) {
        try {
            STORAGE_FILE.parentFile.mkdirs()
            val uuidStrings = uuids.map(UUID::toString)
            FileWriter(STORAGE_FILE).use { writer ->
                GSON.toJson(uuidStrings, writer)
            }
        } catch (e: Exception) {
            System.err.println("Failed to save banned UUIDs: ${e.message}")
        }
    }

    fun loadBannedUUIDs(): Set<UUID> {
        return try {
            if (!STORAGE_FILE.exists()) return emptySet()
            FileReader(STORAGE_FILE).use { reader ->
                val type = object : TypeToken<List<String>>() {}.type
                val uuidStrings: List<String> = GSON.fromJson(reader, type)
                uuidStrings.mapNotNull { runCatching { UUID.fromString(it) }.getOrNull() }.toSet()
            }
        } catch (e: Exception) {
            System.err.println("Failed to load banned UUIDs: ${e.message}")
            emptySet()
        }
    }
}
