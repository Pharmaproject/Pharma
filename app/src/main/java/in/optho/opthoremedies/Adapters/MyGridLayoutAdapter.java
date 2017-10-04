package in.optho.opthoremedies.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;

/**
 * Created by krishna on 3/10/17.
 */

public class MyGridLayoutAdapter extends RecyclerView.Adapter<MyGridLayoutAdapter.MyViewHolder> {

    ArrayList<Product> productList=new ArrayList<>();
    Context context;

    public MyGridLayoutAdapter(Context context, ArrayList<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,null);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.itemTV.setText(productList.get(position).getName());
//        holder.imageView.

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTV;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);

            itemTV = (TextView) itemView.findViewById(R.id.itemTV);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
    public void sort(Comparator comparator) {
        Collections.sort(productList, comparator);
        notifyItemRangeChanged(0, getItemCount());
    }


}
