package me.ryanmiles.dailyenergytracker.data.cache

import android.util.Log
import io.realm.RealmList
import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.Entry
import me.ryanmiles.dailyenergytracker.data.model.HourlyEntry
import me.ryanmiles.dailyenergytracker.data.source.RealmDataSource
import java.util.*
import kotlin.collections.LinkedHashMap

/*
 * Created by Ryan Miles on 3/20/2018.
 */
class EntryRepository(private val realmDataSource: EntryDataSource) : EntryDataSource {

    override fun saveEntry(entry: Entry): Entry {
        val realmEntry = realmDataSource.saveEntry(entry)
        cache(realmEntry)
        return realmEntry
    }

    override fun saveNewHourlyEntry(hourlyEntries: RealmList<HourlyEntry>, newHourlyEntry: HourlyEntry) {
        realmDataSource.saveNewHourlyEntry(hourlyEntries, newHourlyEntry)
    }

    override fun saveHourlyEntry(hourlyEntries: RealmList<HourlyEntry>, hourlyEntry: HourlyEntry): HourlyEntry {
        val realmHourlyEntry = realmDataSource.saveHourlyEntry(hourlyEntries, hourlyEntry)
        cache(realmHourlyEntry)
        return realmHourlyEntry
    }

    override fun deleteAllEntries() {
        realmDataSource.deleteAllEntries()
        cachedEntries.clear()
    }

    override fun deleteHourlyEntry(hourlyEntryId: String) {
        realmDataSource.deleteHourlyEntry(hourlyEntryId)
        cachedHourlyEntries.remove(hourlyEntryId)
    }

    override fun deleteEntry(entryId: String) {
        realmDataSource.deleteEntry(entryId)
        cachedEntries.remove(entryId)
    }

    /**
     * Gets tasks from local data source (realm)
     *
     * Note: [EntryDataSource.GetEntryCallback.onDataNotAvailable] is fired if data source fails to
     * get the data.
     */
    override fun getEntry(entryId: String, callback: EntryDataSource.GetEntryCallback) {
        val entryInCache = getEntryWithId(entryId)

        // Respond immediately with cache if available
        if (entryInCache != null) {
            callback.onEntryLoaded(entryInCache)
            return
        }


        // Load the entry from the local data source
        realmDataSource.getEntry(entryId, object : EntryDataSource.GetEntryCallback {
            override fun onEntryLoaded(entry: Entry) {
                // Do in memory cache update to keep the app UI up to date
                cache(entry)
                callback.onEntryLoaded(entry)

            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getHourlyEntry(hourlyId: String, callback: EntryDataSource.GetHourlyEntryCallback) {
        val hourlyEntryInCache = getHourlyEntryWithId(hourlyId)

        if (hourlyEntryInCache != null) {
            callback.onHourlyEntryLoaded(hourlyEntryInCache)
            return
        }

        realmDataSource.getHourlyEntry(hourlyId, object : EntryDataSource.GetHourlyEntryCallback {
            override fun onHourlyEntryLoaded(hourlyEntry: HourlyEntry) {
                // Do in memory cache update to keep the app UI up to date
                cache(hourlyEntry)
                callback.onHourlyEntryLoaded(hourlyEntry)

            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getEntryWithDate(date: String): Entry? {
        return realmDataSource.getEntryWithDate(date)

    }

    private fun getHourlyEntryWithId(hourlyId: String) = cachedHourlyEntries[hourlyId]

    private fun getEntryWithId(entryId: String) = cachedEntries[entryId]

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    private var cachedEntries: LinkedHashMap<String, Entry> = LinkedHashMap()

    private var cachedHourlyEntries: LinkedHashMap<String, HourlyEntry> = LinkedHashMap()

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private var cacheIsDirty = false

    override fun refreshEntries() {
        cacheIsDirty = true
    }

    /**
     * Gets tasks from cache or local data source (Realm), whichever is
     * available first.
     *
     *
     * Note: [EntryDataSource.LoadEntriesCallback.onDataNotAvailable] is fired if all data sources fail to
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
                override fun onEntriesLoaded(entries: List<Entry>) {
                    refreshEntryCache(entries)
                    callback.onEntriesLoaded(ArrayList(cachedEntries.values))
                }

                override fun onDataNotAvailable() {
                    Log.e("EntryRepository", "No Data Available")
                }
            })
        }

    }

    private fun refreshEntryCache(entries: List<Entry>) {
        cachedEntries.clear()
        entries.forEach {
            cache(it)
        }

        cacheIsDirty = false
    }

    private fun cache(entry: Entry) {
        cachedEntries[entry.id] = entry
    }

    private fun cache(hourlyEntry: HourlyEntry) {
        cachedHourlyEntries[hourlyEntry.id] = hourlyEntry
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
            return if (INSTANCE == null) {
                EntryRepository(realmDataSource).apply { INSTANCE = this }
            } else {
                INSTANCE!!
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
