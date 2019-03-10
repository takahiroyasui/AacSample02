package uniba.jp.aacsample02

import android.app.Application
import androidx.room.Room
import timber.log.Timber
import uniba.jp.aacsample02.models.MyDatabase

class App : Application() {

    companion object {
        lateinit var database: MyDatabase
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        App.database = Room.databaseBuilder(this, MyDatabase::class.java, "database-name").build()
    }
}