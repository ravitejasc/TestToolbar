package com.example.testtoolbar

import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/*@Composable
fun TestRecycler() {
    Scaffold(topBar = {

    }) {
        Recycler(modifier = Modifier.padding(it), list =, callback =) {

        }
    }
}

private class Holder(surface: View) : RecyclerView.ViewHolder(surface)

class LocalAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

}


*//**
 * @exception NoSuchMethodError if [androidx.recyclerview:recyclerview:1.2.1] not available.
 *//*
@Composable
fun <T> Recycler(
    modifier: Modifier = Modifier,
    reversed: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    observer: ((RecyclerView) -> RecyclerView.AdapterDataObserver?)? = null,
    orientation: Int = RecyclerView.VERTICAL,
    list: List<T>,
) {
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            RecyclerView(context).apply {
                layoutManager = LinearLayoutManager(context, orientation, reversed)
                adapter = LocalAdapter().also { adapter ->
                    observer?.invoke(this)?.let {
                        adapter.registerAdapterDataObserver(it)
                    }
                }
            }
        }
    ) {
        //(it.adapter as LocalAdapter<T>).submitList(list)
        with(density) {
            it.setPadding(
                contentPadding.calculateLeftPadding(direction).toPx().toInt(),
                contentPadding.calculateTopPadding().toPx().toInt(),
                contentPadding.calculateRightPadding(direction).toPx().toInt(),
                contentPadding.calculateBottomPadding().toPx().toInt()
            )
            it.clipToPadding = false
        }
    }
}*/
