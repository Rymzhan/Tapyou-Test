package com.tapyou.test.presentation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.tapyou.test.R
import com.tapyou.test.databinding.ActivityMainBinding
import com.tapyou.test.presentation.common.inTransaction
import com.tapyou.test.presentation.common.viewBinding
import com.tapyou.test.presentation.ui.main.MainFragment

class MainActivity : FragmentActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportFragmentManager.inTransaction {
            replace(R.id.main__container, MainFragment())
        }
    }

}