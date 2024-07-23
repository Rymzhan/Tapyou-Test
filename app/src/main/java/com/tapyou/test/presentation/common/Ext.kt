package com.tapyou.test.presentation.common

import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()