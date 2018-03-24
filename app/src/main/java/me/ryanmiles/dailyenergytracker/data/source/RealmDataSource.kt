package me.ryanmiles.dailyenergytracker.data.source

import android.support.annotation.VisibleForTesting
import io.realm.Realm
import io.realm.Sort
import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.Entry

/*
 * Created by Ryan Miles on 3/20/2018.
 */

class RealmDataSource : EntryDataSource {

    val realm: Realm = Realm.getDefaultInstance()

    //TODO Make sure saving does work Realm only manages the returned realmEntry
    override fun saveEntry(entry: Entry): Entry {
        realm.beginTransaction()
        val realmEntry = realm.copyToRealm(entry)
        realm.commitTransaction()
        return realmEntry
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
            val entry = realm.where(Entry::class.java).equalTo("id", entryId).findAll()
            if (entry.isNotEmpty()) {
                entry.deleteFirstFromRealm()
            }
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
        val entry = realm.where(Entry::class.java).equalTo("id", entryId).findAll()
        if (entry.isEmpty()) {
            callback.onDataNotAvailable()
        } else {
            callback.onEntryLoaded(entry.first()!!)
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
