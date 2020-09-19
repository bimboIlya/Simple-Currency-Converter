package com.example.cfttesttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.cfttesttask.databinding.ActivityMainBinding
import com.example.cfttesttask.di.Injectable
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    @Inject lateinit var vmFactory: ViewModelProvider.Factory
    private val viewmodel by viewModels<CurrencyViewModel> { vmFactory }

    private lateinit var adapter: CurrencyAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CurrencyAdapter(viewmodel)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setSupportActionBar(toolbar)
            lifecycleOwner = this@MainActivity
            currencyList.adapter = adapter

            swipeToRefresh.setOnRefreshListener { viewmodel.updateCurrencyListFromNetwork() }
        }

        observeUi()
    }

    private fun observeUi() {
        viewmodel.isLoading.observe(this, {
            binding.swipeToRefresh.isRefreshing = it
        })

        viewmodel.currencyList.observe(this, {
            adapter.updateList(it)
        })

        viewmodel.messageResId.observe(this, { messageResId ->
            messageResId?.let {
                Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
                viewmodel.messageShown()
            }
        })
    }
}

