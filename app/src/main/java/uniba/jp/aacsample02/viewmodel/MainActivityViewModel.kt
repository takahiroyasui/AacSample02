package uniba.jp.aacsample02.viewmodel

import androidx.lifecycle.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import uniba.jp.aacsample02.App
import uniba.jp.aacsample02.models.User
import uniba.jp.aacsample02.view.adapter.UserRvAdapter

class MainActivityViewModel : ViewModel(), LifecycleObserver {

    private val listData: ArrayList<User> = ArrayList()
    private val viewAdapter: UserRvAdapter = UserRvAdapter(listData)
    private val compositeDisposable = CompositeDisposable()

    fun getViewAdapter(): UserRvAdapter {
        return viewAdapter
    }

    fun addData(name: String) {
        val startIndex = viewAdapter.itemCount
        val user = User(name = name)

        Single.fromCallable<Long> { App.database.userDao().insert(user) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("id: %d", it)
                    user.uid = it
                    listData.add(user)
                    viewAdapter.addData(startIndex, 1)
                }, Timber::e)
                .addTo(compositeDisposable)
    }

    fun getUsers() {
        Single.fromCallable<List<User>> { App.database.userDao().getUsers() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ users ->
                    listData.addAll(users)
                    viewAdapter.addData(viewAdapter.itemCount, users.size)
                }, Timber::e)
                .addTo(compositeDisposable)
    }

    fun updateUser(user: User, position: Int) {
        Timber.d("update user:[%d] - %s - %d", user.uid, user.name, position)

        Completable.fromAction { App.database.userDao().update(user.uid, user.name) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listData[position] = user
                    viewAdapter.updateItem(position)
                }, Timber::e)
                .addTo(compositeDisposable)
    }

    fun deleteUser(id: Long, position: Int) {
        Timber.d("delete user:[%d] - %d", id, position)

        Completable.fromAction { App.database.userDao().delete(id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listData.removeAt(position)
                    viewAdapter.removeItem(position)
                }, Timber::e)
                .addTo(compositeDisposable)
    }

    fun clear() {
        listData.clear()
        viewAdapter.update()
    }
}