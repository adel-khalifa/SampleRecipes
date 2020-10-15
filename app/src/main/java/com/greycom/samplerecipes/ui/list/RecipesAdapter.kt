package com.greycom.samplerecipes.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greycom.samplerecipes.R
import com.greycom.samplerecipes.pojo.RecipesResponse.RecipesResponseItem
import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {
    private lateinit var listener: OnRecipeClickListener

    inner class RecipesViewHolder(view: View) : RecyclerView.ViewHolder(view)



    class DiffImpl : DiffUtil.ItemCallback<RecipesResponseItem>() {
        override fun areItemsTheSame(oldItem: RecipesResponseItem, newItem: RecipesResponseItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: RecipesResponseItem, newItem: RecipesResponseItem): Boolean {
            return oldItem == newItem
        }
    }
    private val diffCallBack = DiffImpl()

    val asyncListDiffer = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder = RecipesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
    )

    override fun getItemCount()= asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {

        asyncListDiffer.currentList.let {
            val recipeItem = it[position]

            holder.itemView.apply {
                recipe_item_tv_name.text = recipeItem.name
                recipe_item_tv_details.text = recipeItem.headline
                Glide.with(context).load(recipeItem.image).into(recipe_item_iv_track)

                setOnClickListener {
                    listener.onRecipeClick(recipeItem)
                }
            }


        }
    }

    interface OnRecipeClickListener {
        fun onRecipeClick(recipeItem : RecipesResponseItem)
    }

    fun setOnRecipeClickListener(OnRecipeClickListener: OnRecipeClickListener) {
        listener = OnRecipeClickListener
    }
}