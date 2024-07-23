package com.tapyou.test.presentation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tapyou.test.R
import com.tapyou.test.databinding.FragmentMainBinding
import com.tapyou.test.presentation.common.ViewBindingHolder
import com.tapyou.test.presentation.common.ViewBindingHolderImpl
import com.tapyou.test.presentation.ui.details.PointsFragment

class MainFragment : Fragment(), ViewBindingHolder<FragmentMainBinding> by ViewBindingHolderImpl() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = initBinding(
        FragmentMainBinding.inflate(inflater, container, false),
        this
    ) {
        bindListeners()
    }

    private fun bindListeners() = requireBinding {
        confirmBtn.setOnClickListener {
            val pointsCount = pointsEditText.text.toString().toInt()
            val fragment = PointsFragment.newInstance(pointsCount)
            parentFragmentManager.beginTransaction().replace(
                R.id.main__container,
                fragment,
                fragment::class.java.name
            ).addToBackStack(fragment::class.java.name).commit()
        }
    }
}