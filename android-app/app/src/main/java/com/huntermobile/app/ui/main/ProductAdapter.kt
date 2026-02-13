package com.huntermobile.app.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.huntermobile.app.R
import com.huntermobile.app.data.Product
import com.huntermobile.app.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val listener: OnProductClickListener
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback) {

    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    interface OnProductClickListener {
        fun onProductClicked(link: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textTitle.text = product.nome
            binding.textPrice.text = currencyFormat.format(product.preco)
            binding.textOrigin.apply {
                text = product.origem
                isVisible = product.origem.isNotBlank()
            }

            binding.root.setOnClickListener {
                listener.onProductClicked(product.link)
            }

            if (product.imagemUrl.isNullOrBlank()) {
                binding.imageThumb.setImageResource(R.drawable.ic_placeholder)
            } else {
                binding.imageThumb.load(product.imagemUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_placeholder)
                }
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
    }
}
