package com.example.datasharedapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    private var mText = MutableLiveData<String>()
    val lvData: LiveData<String>
        get() = mText


    suspend fun addData(text: String){
        mText.value = text
    }


}