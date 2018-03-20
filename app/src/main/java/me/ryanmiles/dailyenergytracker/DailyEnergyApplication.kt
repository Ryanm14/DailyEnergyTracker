package me.ryanmiles.dailyenergytracker

import android.app.Application
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
    }
}