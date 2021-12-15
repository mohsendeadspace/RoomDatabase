package com.mohsendeadspace.roomdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class],version = 1)
abstract class SubscriberDataBase :RoomDatabase() {

    abstract val subscriberDAO:SubscriberDAO

    companion object {
        /*
        همانطور که قبلاً هم اشاره کردیم، volatile در محیط‌های Multi Threading استفاده می‌شود
        و هنگامی از آن استفاده می‌کنیم که یک متغیر مشترک (shared variable) بین Thread ها داریم
        و می‌خواهیم عمل نوشتن و خواندن آن به صورت اتمیک صورت پذیرد. اساساً استفاده
         از volatile در برنامه‌های تک ریسمانی بیهوده بوده و باعت کاهش کارایی (پرفرمنس) برنامه (به دلیل عدم استفاده از کش) می‌شود
         */
        @Volatile
        private var INSTANCE:SubscriberDataBase? = null
        fun getInstance(context: Context):SubscriberDataBase{
           synchronized(this){
               var instance:SubscriberDataBase? =  INSTANCE
               if (instance == null){
                   instance = Room.databaseBuilder(context.applicationContext,
                       SubscriberDataBase::class.java,
                       "subscriber_data_database").build()
               }
               return instance
           }
        }
    }

}