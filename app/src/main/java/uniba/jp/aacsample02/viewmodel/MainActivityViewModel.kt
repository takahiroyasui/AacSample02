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

        Completable.fromAction { App.database.userDao().insert(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        listData.add(user)
        viewAdapter.addData(startIndex, 1)
    }

    fun getUsers(): Single<List<User>> {
        return Single.create { emitter ->  emitter.onSuccess( App.database.userDao().getUsers()) }
    }

    fun setUsers(users: List<User>) {
        listData.addAll(users)
        viewAdapter.addData(viewAdapter.itemCount, users.size)
    }

    fun updateUser(id: Long, name: String, position: Int) {
        Completable.fromAction { App.database.userDao().update(id, name) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewAdapter.updateItem(position)
                }, Timber::e)
                .addTo(compositeDisposable)
    }

    fun deleteUser(id: Long, position: Int) {
        Completable.fromAction { App.database.userDao().delete(id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewAdapter.updateItem(position)
                }, Timber::e)
                .addTo(compositeDisposable)
    }


    fun update() {
        viewAdapter.update()
    }

    fun clear() {
        listData.clear()
        update()
    }
}