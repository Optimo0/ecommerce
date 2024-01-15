package com.example.ecommerce.fragment

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.adapter.CategoryAdapter
import com.example.ecommerce.adapter.ProductsAdapter
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.model.Product
import com.example.ecommerce.viewmodel.HomeViewModel
import java.util.*


class HomeFragment : Fragment(), ProductsAdapter.Listener, CategoryAdapter.Listener {
    private var isDone: Boolean = false
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var matchedProduct: ArrayList<Product> = arrayListOf()
    private var products = ArrayList<Product>()
    private lateinit var productsAdapter: ProductsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.searchBarProduct.onActionViewExpanded()
        binding.searchBarProduct.clearFocus()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        //viewModel.getDataFromUrl()
        viewModel.getData(requireContext())
        viewModel.products.observe(viewLifecycleOwner, androidx.lifecycle.Observer { products ->
            products.let {
                productsAdapter = products?.let { it1 ->
                    ProductsAdapter(
                        it1,
                        requireContext() // TODO Fragment HomeFragment not attached to a context.
                    )
                }!!
                binding.recyclerView.adapter = productsAdapter
            }

        })
        binding.searchBarProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                search(newText)
                return true
            }
        })
        var state = 1

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                state = newState
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && (state == 0 || state == 2)) {
                    binding.collapsingToolBarLayout.visibility = View.GONE

                } else if (dy < -10) {
                    binding.collapsingToolBarLayout.visibility = View.VISIBLE
                }
            }
        })
    }
    override fun onItemClick(products: Product) {
        Toast.makeText(activity, "item is clicked", Toast.LENGTH_SHORT).show()
    }

    override fun categoryButtonClicked(
        products: ArrayList<Product>,
        holder: CategoryAdapter.PlaceHolder,
        position: Int
    ) {
        products.forEach {
            if (it.category == products[position].category) {
                println(it.category)
            }
        }
    }

    fun search(text: String?) {
        matchedProduct = arrayListOf()
        text?.let {
            products.forEach { product ->
                if ((product.title?.contains(text, true) == true) ||
                    product.description.toString().contains(text, true) ||
                    product.category.toString().contains(text, true) ||
                    product.brand.toString().contains(text, true)
                ) {
                    matchedProduct.add(product)
                }
            }
            updateRecyclerView()
            if (matchedProduct.isEmpty()) {
                binding.linearLayoutEmptySearchMessage.visibility = View.VISIBLE

            } else {
                binding.linearLayoutEmptySearchMessage.visibility = View.INVISIBLE
            }
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        binding.recyclerView.apply {
            productsAdapter = ProductsAdapter(
                matchedProduct,
                requireContext()
            )
            binding.recyclerView.adapter = productsAdapter
        }
    }
}