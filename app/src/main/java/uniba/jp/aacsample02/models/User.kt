package uniba.jp.aacsample02.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey(autoGenerate = true)
        var uid: Long = 0,

        var name: String = ""
)