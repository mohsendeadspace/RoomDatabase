package com.mohsendeadspace.roomdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsendeadspace.roomdemo.db.SubscriberRepository
import java.lang.IllegalArgumentException

class SubscriberViewModleFactory(private val repository: SubscriberRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SubscriberViewModle::class.java)){
            return SubscriberViewModle(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}