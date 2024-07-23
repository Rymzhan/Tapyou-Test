package com.tapyou.test.presentation.ui.details

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.tapyou.test.databinding.FragmentPointsBinding
import com.tapyou.test.domain.common.ContentUiState
import com.tapyou.test.domain.common.UiText
import com.tapyou.test.domain.model.Points
import com.tapyou.test.presentation.common.ViewBindingHolder
import com.tapyou.test.presentation.common.ViewBindingHolderImpl
import com.tapyou.test.presentation.common.dpToPx
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PointsFragment : Fragment(),
    ViewBindingHolder<FragmentPointsBinding> by ViewBindingHolderImpl() {

    private val vm by viewModel<PointsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = initBinding(
        FragmentPointsBinding.inflate(inflater, container, false),
        this
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        bindObservers()
    }

    private fun initArgs() {
        arguments?.getInt(COUNT_KEY)?.let { count ->
            vm.sendAction(PointsContract.Action.OnFetchPointsData(count))
        }
    }

    private fun bindObservers() {
        lifecycleScope.launch {
            vm.uiState.collect { state ->
                when (state.pointsContent) {
                    is ContentUiState.Loading -> showProgress(true)
                    is ContentUiState.Success -> handlePointsData(state.pointsContent.data.sortedBy { it.x })
                    is ContentUiState.Error -> showError(state.pointsContent.error)
                    else -> Unit
                }
            }
        }
    }

    private fun handlePointsData(pointsContent: List<Points>) {
        showProgress(false)
        drawTable(pointsContent)
        drawGraph(pointsContent)
    }

    private fun drawGraph(pointsContent: List<Points>) = requireBinding {
        val entries = mutableListOf<Entry>()

        pointsContent.forEach { point ->
            val x = point.x?.toFloat() ?: -1f
            val y = point.y?.toFloat() ?: -1f
            entries.add(Entry(x, y))
        }

        entries.sortBy { it.x }

        val dataSet = LineDataSet(entries, "Points")
        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.invalidate()
        lineChart.isVisible = true
    }

    private fun drawTable(pointsContent: List<Points>) = requireBinding {
        pointsContent.forEach { point ->
            val tableRow = TableRow(context)
            val xTextView = TextView(context).apply {
                text = point.x.toString()
                setTvPadding(this)
            }
            val yTextView = TextView(context).apply {
                text = point.y.toString()
                setTvPadding(this)
            }
            tableRow.addView(xTextView)
            tableRow.addView(yTextView)
            coordinatesTable.addView(tableRow)
        }
        coordinatesTable.isVisible = true
    }

    private fun setTvPadding(textView: TextView) {
        textView.apply {
            setPadding(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx())
        }
    }

    private fun showProgress(isVisible: Boolean) = requireBinding {
        progressBar.isVisible = isVisible
    }

    private fun showError(error: UiText) {
        showProgress(false)
        Toast.makeText(requireContext(), error.getText(requireContext()), Toast.LENGTH_LONG).show()
        parentFragmentManager.popBackStack()
    }

    companion object {
        private const val COUNT_KEY = "COUNT_KEY"

        fun newInstance(count: Int) =
            PointsFragment().apply {
                arguments = Bundle().apply {
                    putInt(COUNT_KEY, count)
                }
            }
    }
}