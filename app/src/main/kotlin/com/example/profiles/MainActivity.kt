package com.example.profiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.profiles.data.Outcome
import com.example.profiles.data.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_user.view.*
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val mainViewModel by viewModel<MainViewModel>()

    private val userObserver = Observer<Outcome<List<User>>> {
        when (it) {
            is Outcome.Progress -> swipeRefresh.isRefreshing = it.loading
            is Outcome.Failure -> toast(it.e.localizedMessage ?: "Something went wrong")
            is Outcome.Success -> getAdapter().addUsers(it.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.users.observe(this, userObserver)
    }

    override fun onRefresh() {
        mainViewModel.fetchUsers()
    }

    private fun getAdapter() = userList.adapter as UserAdapter


    private class UserAdapter(private val users: ArrayList<User> = arrayListOf()) :
        RecyclerView.Adapter<UserAdapter.ViewHolder>() {

        fun addUsers(newUsers: List<User>) {
            users.clear()
            users.addAll(newUsers)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bindUser(users[position])

        override fun getItemCount(): Int = users.size

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            fun bindUser(user: User) = with(user) {
                itemView.userFullName.text = name
                itemView.userEmail.text = email
            }
        }
    }
}
