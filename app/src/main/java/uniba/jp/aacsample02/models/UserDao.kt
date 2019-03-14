package uniba.jp.aacsample02.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUsers(): Single<List<User>>

    @Insert
    fun insert(user: User): Single<Long>

    @Query("DELETE FROM user WHERE uid = :id")
    fun delete(id: Long)

    @Query("UPDATE user SET `name` = :name WHERE uid = :id")
    fun update(id: Long, name: String)
}