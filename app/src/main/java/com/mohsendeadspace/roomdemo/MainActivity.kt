package com.mohsendeadspace.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsendeadspace.roomdemo.databinding.ActivityMainBinding
import com.mohsendeadspace.roomdemo.db.Subscriber
import com.mohsendeadspace.roomdemo.db.SubscriberDataBase
import com.mohsendeadspace.roomdemo.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var subscriberViewModle: SubscriberViewModle
    private lateinit var adapter: MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao = SubscriberDataBase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModleFactory(repository)
        subscriberViewModle = ViewModelProvider(this,factory).get(SubscriberViewModle::class.java)
        binding.myViewModle = subscriberViewModle
        binding.lifecycleOwner = this
        initRecyclerView()
        subscriberViewModle.message.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView(){
        binding.subscriberRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter =  MyRecyclerViewAdapter({selectedItem:Subscriber->listItemClicked(selectedItem)})
        binding.subscriberRecyclerview.adapter = adapter
        displaySubscribersList()
    }

    private fun displaySubscribersList(){
        subscriberViewModle.subscriber.observe(this, Observer {
            Log.i("MYTAG",it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(subscriber: Subscriber){
        Toast.makeText(this,"selected name is ${subscriber.name}",Toast.LENGTH_SHORT).show()
        subscriberViewModle.initUpdateAndDelete(subscriber)
    }

}