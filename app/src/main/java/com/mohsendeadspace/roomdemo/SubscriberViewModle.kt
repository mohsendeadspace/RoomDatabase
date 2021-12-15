package com.mohsendeadspace.roomdemo

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsendeadspace.roomdemo.db.Event
import com.mohsendeadspace.roomdemo.db.Subscriber
import com.mohsendeadspace.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SubscriberViewModle(private val repository: SubscriberRepository) : ViewModel(), Observable {

    val subscriber =  repository.subscribers
    private var isUpdateAndDelete = false
    private lateinit var subscriberToUpdateOrDelete : Subscriber


    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val saveOrupdateButtonText = MutableLiveData<String>()
    @Bindable
    val clearOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrupdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear"
    }

    fun saveOrUpdate(){
        if (inputName.value == null && inputEmail.value == null){
            statusMessage.value = Event("Please complete filed")
        }else if (inputName.value == null){
            statusMessage.value = Event("Please enter subscriber name")
        }else if (inputEmail.value == null){
            statusMessage.value = Event("Please enter subscriber Email")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Please enter a correct Email")
        }else {

            if (isUpdateAndDelete){
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)
            }else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0, name, email))

                inputName.value = null
                inputEmail.value = null
            }
        }

    }

    fun clearAllOrDelete(){
        if (isUpdateAndDelete){
            delete(subscriberToUpdateOrDelete)
        }else {
            clearAll()
        }

    }

    fun insert(subscriber: Subscriber):Job = viewModelScope.launch {
        val newRowId:Long = repository.insert(subscriber)
        if (newRowId>-1){
            statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
        }else {
            statusMessage.value = Event("Error Occurred")
        }

    }

    fun update(subscriber: Subscriber):Job = viewModelScope.launch {
        val noOfRow:Int = repository.update(subscriber)
        if (noOfRow > 0){
            inputName.value = null
            inputEmail.value = null
            isUpdateAndDelete = false
            saveOrupdateButtonText.value = "Save"
            clearOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRow Row Updated Successfully")
        }else {
            statusMessage.value = Event("Error Occurred")
        }

    }

    fun delete(subscriber: Subscriber):Job = viewModelScope.launch {
        val noOfRowsDeleted:Int = repository.delete(subscriber)
        if (noOfRowsDeleted > 0){
            inputName.value = null
            inputEmail.value = null
            isUpdateAndDelete = false
            saveOrupdateButtonText.value = "Save"
            clearOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Deleted Successfully")
        }else {
            statusMessage.value = Event("Error Occurred")
        }

    }

    fun clearAll():Job = viewModelScope.launch {
        val noOfRowsDeleted:Int = repository.deleteAll()
        if (noOfRowsDeleted > 0){
            statusMessage.value = Event("$noOfRowsDeleted Deleted Successfully")
        }
        else {
            statusMessage.value = Event("Error Occurred")
        }

    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateAndDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrupdateButtonText.value = "Update"
        clearOrDeleteButtonText.value = "Delete"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}