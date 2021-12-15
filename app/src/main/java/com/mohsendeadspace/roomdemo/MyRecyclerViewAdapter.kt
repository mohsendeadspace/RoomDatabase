package com.mohsendeadspace.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mohsendeadspace.roomdemo.databinding.ListItemBinding
import com.mohsendeadspace.roomdemo.db.Subscriber
import com.mohsendeadspace.roomdemo.generated.callback.OnClickListener

class MyRecyclerViewAdapter(private val clickListener: (Subscriber)->Unit):RecyclerView.Adapter<MyViewHolder>()
{

    private val subscriberList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:ListItemBinding = DataBindingUtil.inflate(layoutInflater,R.layout.list_item,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscriberList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }

    fun setList(subscriber: List<Subscriber>){
        subscriberList.clear()
        subscriberList.addAll(subscriber)
    }
}

class MyViewHolder(val binding:ListItemBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(subscriber: Subscriber, clickListener: (Subscriber)->Unit){
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email
        binding.listItemLayout.setOnClickListener {
            clickListener(subscriber)
        }
    }
}