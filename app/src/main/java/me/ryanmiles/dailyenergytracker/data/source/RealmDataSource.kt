package me.ryanmiles.dailyenergytracker.data.source

import android.support.annotation.VisibleForTesting
import io.realm.Realm
import io.realm.RealmList
import io.realm.Sort
import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.Entry
import me.ryanmiles.dailyenergytracker.data.model.HourlyEntry


/*
 * Created by Ryan Miles on 3/20/2018.
 */

class RealmDataSource : EntryDataSource {

    val realm: Realm = Realm.getDefaultInstance()

    override fun saveEntry(entry: Entry): Entry {
        realm.beginTransaction()
        val realmEntry = realm.copyToRealmOrUpdate(entry)
        realm.commitTransaction()
        return realmEntry
    }

    override fun saveHourlyEntry(hourlyEntries: RealmList<HourlyEntry>, hourlyEntry: HourlyEntry): HourlyEntry {
        realm.beginTransaction()
        val realmHourlyEntry = realm.copyToRealmOrUpdate(hourlyEntry)
        hourlyEntries.sort()
        realm.commitTransaction()
        return realmHourlyEntry
    }

    override fun saveNewHourlyEntry(hourlyEntries: RealmList<HourlyEntry>, newHourlyEntry: HourlyEntry) {
        realm.executeTransaction {
            hourlyEntries.add(newHourlyEntry)
            hourlyEntries.sort()
        }
    }

    override fun refreshEntries() {
        // Not required because the {@link EntryRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllEntries() {
        realm.executeTransaction {
            val entry = realm.where(Entry::class.java).findAll()
            if (entry.isNotEmpty()) {
                entry.deleteAllFromRealm()
            }
        }
    }

    override fun deleteEntry(entryId: String) {
        realm.executeTransaction {
            realm.where(Entry::class.java).equalTo("id", entryId).findFirst()?.deleteFromRealm()
        }

    }

    override fun deleteHourlyEntry(hourlyEntryId: String) {
        realm.executeTransaction {
            realm.where(HourlyEntry::class.java).equalTo("id", hourlyEntryId).findFirst()?.deleteFromRealm()
        }
    }


    override fun getEntries(callback: EntryDataSource.LoadEntriesCallback) {
        val entries = realm.where(Entry::class.java).sort("date", Sort.DESCENDING).findAll()
        if (entries.isEmpty()) {
            callback.onDataNotAvailable()
        } else {
            callback.onEntriesLoaded(entries)
        }
    }

    override fun getEntry(entryId: String, callback: EntryDataSource.GetEntryCallback) {
        val entry = realm.where(Entry::class.java).equalTo("id", entryId).findFirst()
        if (entry == null) {
            callback.onDataNotAvailable()
        } else {
            callback.onEntryLoaded(entry)
        }
    }

    override fun getEntryWithDate(date: String): Entry? {
        return realm.where(Entry::class.java).equalTo("date", date).findFirst()
    }

    override fun getHourlyEntry(hourlyId: String, callback: EntryDataSource.GetHourlyEntryCallback) {
        val hourlyEntry = realm.where(HourlyEntry::class.java).equalTo("id", hourlyId).findFirst()
        if (hourlyEntry == null) {
            callback.onDataNotAvailable()
        } else {
            callback.onHourlyEntryLoaded(hourlyEntry)
        }
    }

    companion object {
        private var INSTANCE: RealmDataSource? = null

        @JvmStatic
        fun getInstance(): RealmDataSource {
            if (INSTANCE == null) {
                synchronized(RealmDataSource::javaClass) {
                    INSTANCE = RealmDataSource()
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
