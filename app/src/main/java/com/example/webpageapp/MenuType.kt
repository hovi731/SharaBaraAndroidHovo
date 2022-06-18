package com.example.webpageapp

import androidx.annotation.IntDef
import com.example.webpageapp.MenuType.Companion.MENU_CLOSE
import com.example.webpageapp.MenuType.Companion.MENU_MORE
import com.example.webpageapp.MenuType.Companion.MENU_SHARE
import com.example.webpageapp.MenuType.Companion.MENU_TYPE_UNDEFINED

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
@IntDef(MENU_TYPE_UNDEFINED, MENU_CLOSE, MENU_MORE, MENU_SHARE)
annotation class MenuType {
    companion object{
        const val MENU_TYPE_UNDEFINED = -1
        const val MENU_CLOSE = 0
        const val MENU_MORE = 1
        const val MENU_SHARE = 2
        const val NO_MENU = 3
    }
}