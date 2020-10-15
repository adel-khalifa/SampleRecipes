package com.greycom.samplerecipes.ui.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.greycom.samplerecipes.R
import com.greycom.samplerecipes.network.NetworkState
import com.greycom.samplerecipes.network.NetworkState.OnFailure
import com.greycom.samplerecipes.network.NetworkState.OnLoading
import com.greycom.samplerecipes.pojo.RecipesResponse
import com.greycom.samplerecipes.pojo.RecipesResponse.RecipesResponseItem
import com.greycom.samplerecipes.ui.details.DetailsActivity
import com.greycom.samplerecipes.utils.Constants
import com.greycom.samplerecipes.utils.Constants.EXTRA_RECIPE
import com.greycom.samplerecipes.viewmodel.MainRepo
import com.greycom.samplerecipes.viewmodel.RecipesFactory
import com.greycom.samplerecipes.viewmodel.RecipesViewModel
import com.greycom.samplerecipes.utils.Constants.SORT_TYPE
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity(), RecipesAdapter.OnRecipeClickListener {
    private lateinit var recipesList: MutableList<RecipesResponseItem>
    private lateinit var recipesAdapter: RecipesAdapter
    private var isSortedByFat = true
    private lateinit var recipesViewModel: RecipesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            isSortedByFat = savedInstanceState.getBoolean(SORT_TYPE)
        }
        recipesList = mutableListOf()
        setContentView(R.layout.activity_list)
        setSupportActionBar(la_toolbar)
        initRecycler()
        initViewModel()
        recipesLiveDataObserver()
        searchObserver()
        search_edt.setOnQueryTextFocusChangeListener { view, bool ->
            recipesAdapter.asyncListDiffer.submitList(recipesList.toList())
        }


    }

    private fun recipesLiveDataObserver() {
        recipesViewModel.recipesListLiveData.observe(this) { networkStateContainsResponse ->

            when (networkStateContainsResponse) {
                is OnLoading -> setDataOnLoadingState()
                is OnFailure -> setDataOnFailureState(networkStateContainsResponse.failureMessage)
                is NetworkState.OnSuccess -> {
                    setDataOnSuccessState(networkStateContainsResponse.bodyData!!)
                }
            }
        }

    }


    private fun setDataOnSuccessState(bodyData: RecipesResponse) {
        recipesList = bodyData.toMutableList()
        main_rv.visibility = View.VISIBLE
        tv_error.visibility = View.GONE
        main_progress.visibility =View.GONE

        if (isSortedByFat) sortAndApplyListByFats()
        else sortAndApplyListByCalories()
        main_rv.smoothScrollToPosition(0)


        Log.d("ListActivity", "On Success State")
        Log.d(
            "ListActivity",
            "On Success : FavoriteList : size= ${recipesList.size}"
        )
    }

    private fun sortAndApplyListByFats() {
        recipesList.sortBy { it.fats }
        recipesAdapter.asyncListDiffer.submitList(recipesList.toList())
    }

    private fun sortAndApplyListByCalories() {
        recipesList.sortBy { it.calories }
        recipesAdapter.asyncListDiffer.submitList(recipesList.toList())
    }


    private fun setDataOnFailureState(message: String?) {
        main_rv.visibility = View.INVISIBLE
        tv_error.visibility = View.VISIBLE
        main_progress.visibility =View.GONE
        tv_error.text = message


        Log.d("ListActivity", "On Failure State ")
        Log.d(
            "ListActivity",
            "On Failure : Message =$message"
        )
    }

    private fun setDataOnLoadingState() {
        main_progress.visibility =View.VISIBLE

        Log.d("ListActivity", "On Loading State  ")

    }


    private fun initViewModel() {
        val recipesFactory = RecipesFactory(application, MainRepo)
        recipesViewModel = ViewModelProvider(viewModelStore, recipesFactory)
            .get(RecipesViewModel::class.java)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        isSortedByFat = savedInstanceState.getBoolean(SORT_TYPE)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putBoolean(SORT_TYPE, isSortedByFat)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.sort_by_fat -> {
                isSortedByFat = true
                sortAndApplyListByFats()
                return true
            }
            R.id.sort_by_cal -> {
                isSortedByFat = false
                sortAndApplyListByCalories()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sort_menu, menu)
        return true
    }


    private fun initRecycler() {
        recipesAdapter = RecipesAdapter()
        recipesAdapter.setOnRecipeClickListener(this)

        main_rv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = recipesAdapter
        }
    }

    private fun searchObserver() {
        search_edt.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (!query.isNullOrBlank()) {
                    Log.i("ListActivity", "onQueryTextSubmit: ")
                    querySearch(query)
                    false
                } else true
            }

            private fun querySearch(query: String) {
                val searchResultList = recipesList.filter {
                    it.name.contains(query)
                }
                if (searchResultList.isNotEmpty()) {
                    main_rv.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    recipesAdapter.asyncListDiffer.submitList(searchResultList.toList())
                }
                else {
                    main_rv.visibility = View.GONE
                    tv_error.visibility = View.VISIBLE
                    tv_error.text = getString(R.string.no_result)
                }
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                return if (!newText.isNullOrBlank()) {
                    Log.i("ListActivity", "onQueryTextChange: ")
                    // do Search
                    false
                } else true
            }

        })
    }

    override fun onRecipeClick(recipeItem: RecipesResponseItem) {
        startActivity(Intent(this, DetailsActivity::class.java).also {
            it.putExtra(EXTRA_RECIPE, recipeItem)
        })
    }
}