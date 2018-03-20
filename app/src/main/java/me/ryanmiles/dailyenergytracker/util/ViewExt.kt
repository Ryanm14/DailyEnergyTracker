package me.ryanmiles.dailyenergytracker.util

import android.support.design.widget.Snackbar
import android.view.View

/*
  * Created by Ryan Miles on 3/20/2018.
  */
fun View.showSnackBar(message: String, duration: Int) {
    Snackbar.make(this, message, duration).show()
}