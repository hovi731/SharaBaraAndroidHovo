package com.example.webpageapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebViewModel: ViewModel() {
    val menuTypeLiveData: MutableLiveData<@MenuType Int> = MutableLiveData()
}