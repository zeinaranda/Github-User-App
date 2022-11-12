package com.dicoding.picodiploma.testgithub2.ui.follow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.testgithub2.modal.UserResponse
import com.dicoding.picodiploma.testgithub2.adapter.FollowAdapter
import com.dicoding.picodiploma.testgithub2.api.ApiConfig
import com.dicoding.picodiploma.testgithub2.databinding.FragmentFollowBinding
import com.dicoding.picodiploma.testgithub2.ui.detail.DetailUserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding

    var listUser: ArrayList<UserResponse>? = null
    var followAdapter: FollowAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username: String? = arguments?.getString(EXTRA_USERNAME)
        username?.let { getFollower(it) }
    }

    private fun getFollower(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollower(username)
        client.enqueue(object : Callback<ArrayList<UserResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UserResponse>>,
                response: Response<ArrayList<UserResponse>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    listUser = response.body()
                    followAdapter = listUser?.let { FollowAdapter(it) }
                    showRecyclerList()

                }
            }

            override fun onFailure(
                call: Call<ArrayList<UserResponse>>,
                t: Throwable
            ) {
                showLoading(false)
                TODO("Not yet implemented")
            }
        })
    }

    private fun showRecyclerList() {

        val layoutManager = LinearLayoutManager(view?.context)
        binding.rvFollow.layoutManager = layoutManager

        binding.rvFollow.adapter = followAdapter
        followAdapter?.setOnItemClickCallback(object :
            FollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                val intentToDetail =
                    Intent(context, DetailUserActivity::class.java)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
                startActivity(intentToDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        @JvmStatic
        fun newInstance(data: String?) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_USERNAME, data)
                }
            }
    }
}

