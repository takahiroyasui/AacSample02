package uniba.jp.aacsample02.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUsers(): List<User>

    @Insert
    fun insert(user: User): Long

    @Query("DELETE FROM user WHERE uid = :id")
    fun delete(id: Long)

    @Query("UPDATE user SET `name` = :name WHERE uid = :id")
    fun update(id: Long, name: String)
}