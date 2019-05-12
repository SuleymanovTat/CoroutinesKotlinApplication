package com.suleymanovtat.coroutineskotlinapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainActivity : AppCompatActivity(), PostAdapter.OnNoteClickListener {
    override fun onNoteClick(item: Posts) {

    }

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    lateinit var mAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = PostAdapter(arrayListOf(), this)
        with(recyclerId) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
//        var mJob = ioScope.launch {
//            delay(10000)
//            Log.e("my", "mainIO")
//            mainScope.launch {
//                tvMessage.setText("Message Hello")
//            }
//        }
//        GlobalScope.launch(Dispatchers.Main) {
//            tvBegin.setText("Begin")
//        }

        val service = RetrofitFactory.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {
            val request = service.getPosts()
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        mAdapter.items = response.body() as ArrayList<Posts>
                    } else {
                        toast("Error: ${response.code()}")
                    }
                } catch (e: HttpException) {
                    toast("Exception ${e.message}")
                } catch (e: Throwable) {
                    toast("Ooops: Something else went wrong")
                }
            }
        }

    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
