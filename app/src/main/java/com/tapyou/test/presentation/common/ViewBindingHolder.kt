package com.tapyou.test.presentation.common

import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.viewbinding.ViewBinding

interface ViewBindingHolder<T : ViewBinding> {

    val binding: T?

    /**
     * Saves the binding for cleanup on onDestroy, calls the specified function [onBound] with `this` value
     * as its receiver and returns the bound view root.
     */
    fun initBinding(binding: T, fragment: Fragment, onBound: (T.() -> Unit)? = null): View

    /**
     * Calls the specified [block] with the binding as `this` value and returns the binding. As a consequence, this method
     * can be used with a code block lambda in [block] or to initialize a variable with the return type.
     *
     * @throws IllegalStateException if not currently holding a ViewBinding (when called outside of an active fragment's lifecycle)
     */
    fun requireBinding(block: (T.() -> Unit)? = null): T

}

class ViewBindingHolderImpl<T : ViewBinding> : ViewBindingHolder<T>, LifecycleObserver {

    override var binding: T? = null
    var lifecycle: Lifecycle? = null

    private lateinit var fragmentName: String

    override fun requireBinding(block: (T.() -> Unit)?) =
        binding?.apply { block?.invoke(this) }
            ?: throw IllegalStateException("Accessing binding outside of Fragment lifecycle: $fragmentName")

    override fun initBinding(binding: T, fragment: Fragment, onBound: (T.() -> Unit)?): View {
        this.binding = binding
        lifecycle = fragment.viewLifecycleOwner.lifecycle
        lifecycle?.addObserver(this)
        fragmentName = fragment::class.simpleName ?: "N/A"
        addLifecycleObserver()
        onBound?.invoke(binding)


        return binding.root
    }

    /**
     * To not leak memory we nullify the binding when the view is destroyed.
     */
    private fun addLifecycleObserver() {
        LifecycleEventObserver { _, event ->
            when (event.targetState) {
                Lifecycle.State.DESTROYED -> {
                    lifecycle?.removeObserver(this) // not mandatory, but preferred
                    lifecycle = null
                    binding = null
                }
                Lifecycle.State.INITIALIZED,
                Lifecycle.State.CREATED,
                Lifecycle.State.STARTED,
                Lifecycle.State.RESUMED -> { /* Empty */
                }
            }
        }
    }
}

inline fun <T : ViewBinding> ComponentActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

