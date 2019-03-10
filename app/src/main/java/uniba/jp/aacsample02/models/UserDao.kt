package uniba.jp.aacsample02.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllPeople(): LiveData<List<User>>

    @Insert
    fun insert(person: User)

    @Query("DELETE FROM user WHERE uid = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM user")
    fun getUsers(): List<User>

    @Query("UPDATE user SET `name` = :name WHERE uid = :id")
    fun update(id: Long, name: String)
}