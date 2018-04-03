package me.ryanmiles.dailyenergytracker

import android.app.Application
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm


/*
 * Created by Ryan Miles on 3/20/2018.
 */

/**
 * Application class to initialize Realm Database
 */
class DailyEnergyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build())
    }
}