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

    private val compositeDisposable = CompositeDisposable()

    fun addData(name: String) {
        val user = User(name = name)

        App.database.userDao().insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, Timber::e)
                .addTo(compositeDisposable)
    }



    fun getUsers(): LiveData<List<User>> {
        return App.database.userDao().getUsers()
    }

    fun updateUser(uid: Long, name: String) {
        Timber.d("update user:[%d] - %s", uid, name)

        Completable.fromAction { App.database.userDao().update(uid, name) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, Timber::e)
                .addTo(compositeDisposable)
    }

    fun deleteUser(id: Long, position: Int) {
        Timber.d("delete user:[%d] - %d", id, position)

        Completable.fromAction { App.database.userDao().delete(id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, Timber::e)
                .addTo(compositeDisposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Timber.d("onStop")
        compositeDisposable.clear()
    }
}