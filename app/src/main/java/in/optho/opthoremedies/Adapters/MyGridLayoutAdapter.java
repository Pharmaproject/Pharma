package in.optho.opthoremedies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.optho.opthoremedies.Activities.ProductActivity;
import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;

import static android.content.Context.MODE_PRIVATE;

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
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });


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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTV;
        ImageView imageView;
        public MyViewHolder(final View itemView) {
            super(itemView);

            itemTV = (TextView) itemView.findViewById(R.id.itemTV);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Product product = productList.get(getAdapterPosition());
//                    Toast.makeText(view.getContext(), "Clicked : "+product.getName(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(itemView.getContext(), ProductActivity.class);
                    intent.putExtra("PRODUCT", product);
                    itemView.getContext().startActivity(intent);

                    //increase the counter by 1 on each click

                    SharedPreferences storeddata;
                    SharedPreferences.Editor edit;

                    storeddata = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
                    edit = storeddata.edit();

                    int temp = storeddata.getInt(product.getId(), 0);
                    edit.putInt(product.getId(),temp+1);

                }
            });


        }
    }
    public void sort(Comparator comparator) {
        Collections.sort(productList, comparator);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void setFilter(ArrayList<Product> newList){

        productList = new ArrayList<>();
        productList.addAll(newList);
        notifyDataSetChanged();
    }


}
