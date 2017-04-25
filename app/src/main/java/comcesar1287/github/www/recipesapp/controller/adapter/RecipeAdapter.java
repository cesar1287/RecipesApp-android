package comcesar1287.github.www.recipesapp.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.List;

import comcesar1287.github.www.recipesapp.R;
import comcesar1287.github.www.recipesapp.controller.interfaces.RecyclerViewOnClickListenerHack;
import comcesar1287.github.www.recipesapp.controller.domain.Recipe;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder>{

    private List<Recipe> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private Context c;

    public RecipeAdapter(Context c, List<Recipe> l){
        this.c = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.item_recipe, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        Glide.with(c)
                .load(mList.get((position)).getImage())
                .into(myViewHolder.bannerRecipe);
        myViewHolder.titleRecipe.setText(mList.get(position).getName());
        myViewHolder.timeRecipe.setText(String.valueOf(mList.get(position).getTime()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener /*View.OnCreateContextMenuListener*/{
        ImageView bannerRecipe;
        TextView titleRecipe;
        TextView timeRecipe;

        MyViewHolder(View itemView) {
            super(itemView);
            bannerRecipe = (ImageView) itemView.findViewById(R.id.recipe_banner);
            titleRecipe = (TextView) itemView.findViewById(R.id.recipe_title);
            timeRecipe = (TextView) itemView.findViewById(R.id.recipe_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }
}
