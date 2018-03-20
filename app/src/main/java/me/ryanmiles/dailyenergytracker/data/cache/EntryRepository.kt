package me.ryanmiles.dailyenergytracker.data.cache

import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.DailyEntry
import java.util.*

/*
 * Created by Ryan Miles on 3/20/2018.
 */
class EntryRepository : EntryDataSource {

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    var cachedTasks: LinkedHashMap<String, DailyEntry> = LinkedHashMap()

    init {
        cachedTasks.put("Test1", DailyEntry(UUID.randomUUID().toString(), "Test1"))
        cachedTasks.put("Test2", DailyEntry(UUID.randomUUID().toString(), "Test2"))
        cachedTasks.put("Test3", DailyEntry(UUID.randomUUID().toString(), "Test3"))
    }


    fun refreshTasks() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getEntries(callback: EntryDataSource.LoadEntriesCallback) {
        callback.onEntriesLoaded(ArrayList(cachedTasks.values))
    }
}