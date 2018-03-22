package me.ryanmiles.dailyenergytracker.data.cache

import android.util.Log
import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.DailyEntry
import me.ryanmiles.dailyenergytracker.data.source.RealmDataSource
import java.util.*

/*
 * Created by Ryan Miles on 3/20/2018.
 */
class EntryRepository(val realmDataSource: RealmDataSource) : EntryDataSource {
    override fun getEntry(entryId: String, callback: EntryDataSource.getEntryCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    var cachedEntries: LinkedHashMap<String, DailyEntry> = LinkedHashMap()

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    var cacheIsDirty = false

    fun refreshEntries() {
        cacheIsDirty = true
    }

    /**
     * Gets tasks from cache or local data source (Realm), whichever is
     * available first.
     *
     *
     * Note: [EntryDataSource.LoadEntriesCallBack.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getEntries(callback: EntryDataSource.LoadEntriesCallback) {
        // Respond immediately with cache if available and not dirty
        if (cachedEntries.isNotEmpty() && !cacheIsDirty) {
            callback.onEntriesLoaded(ArrayList(cachedEntries.values))
            return
        } else {
            // Query the Realm
            realmDataSource.getEntries(object : EntryDataSource.LoadEntriesCallback {
                override fun onEntriesLoaded(entries: List<DailyEntry>) {
                    refreshCache(entries)
                    callback.onEntriesLoaded(ArrayList(cachedEntries.values))
                }

                override fun onDataNotAvailable() {
                    Log.e("EntryRepository", "No Data Available")
                }
            })
        }

    }

    private fun refreshCache(entries: List<DailyEntry>) {
        cachedEntries.clear()
        entries.forEach {
            val cachedEntry = DailyEntry(it.id, it.date, it.note, it.entries)
            cachedEntries[cachedEntry.id] = cachedEntry
        }

        cacheIsDirty = false
    }

    companion object {

        private var INSTANCE: EntryRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param realmDataSource  the device storage data source
         * *
         * @return the [EntryRepository] instance
         */
        @JvmStatic
        fun getInstance(realmDataSource: RealmDataSource): EntryRepository {
            if (INSTANCE == null) {
                return EntryRepository(realmDataSource).apply { INSTANCE = this }
            } else {
                return INSTANCE!!
            }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
