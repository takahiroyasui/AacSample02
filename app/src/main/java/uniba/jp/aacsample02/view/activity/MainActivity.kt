package uniba.jp.aacsample02.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.view.*
import timber.log.Timber
import uniba.jp.aacsample02.R
import uniba.jp.aacsample02.databinding.ActivityMainBinding
import uniba.jp.aacsample02.models.User
import uniba.jp.aacsample02.view.adapter.UserRvAdapter
import uniba.jp.aacsample02.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewManager: LinearLayoutManager
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel

        lifecycle.addObserver(viewModel)
        binding.lifecycleOwner = this

        viewManager = LinearLayoutManager(this)

        viewModel.getViewAdapter().setOnItemClickListener(object: UserRvAdapter.OnItemClickListener {
            override fun onClick(view: View, data: User) {
                val editText = EditText(this@MainActivity)
                val position= binding.recyclerView.getChildAdapterPosition(view)

                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle(data.name + " を編集")
                    setView(editText)
                    setPositiveButton("OK") { _, _ ->
                        val newName = editText.text.toString()
                        Timber.d("%d - %s - %d", data.uid, newName, position)
                        viewModel.updateUser(data.uid!!, newName, position)
                    }
                    setNegativeButton("Cancel") { _, _ -> Timber.d("Cancel") }
                    show()
                }
            }
        })

        viewModel.getViewAdapter().setOnItemLongClickListener(object: UserRvAdapter.OnItemLongClickListener {
            override fun onLongClick(view: View, data: User) {
                val position= binding.recyclerView.getChildAdapterPosition(view)

                AlertDialog.Builder(this@MainActivity).apply {
                    setMessage(data.name + " を削除しますか？")
                    setPositiveButton("OK") { _, _ ->
                        viewModel.deleteUser(data.uid!!, position)
                    }
                    setNegativeButton("Cancel") { _, _ -> Timber.d("Cancel") }
                    show()
                }
            }
        })

        binding.toolbar.edit_text.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && binding.toolbar.edit_text.text.isNotEmpty()) {
                val name = binding.toolbar.edit_text.text.toString()
                binding.toolbar.edit_text.text.clear()
                viewModel.addData(name)
            }
            false
        }
    }

    override fun onResume() {
        Timber.d("onResume")
        super.onResume()

        viewModel.clear()

        viewModel.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ users ->
                viewModel.setUsers(users)

                binding.recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewModel.getViewAdapter()
                }

            }, Timber::e)
            .addTo(compositeDisposable)
    }
}
