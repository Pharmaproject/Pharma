package in.optho.opthoremedies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;

/**
 * Created by krishna on 3/10/17.
 */

public class MyListLayoutAdapter extends RecyclerView.Adapter<MyListLayoutAdapter.MyViewHolder> {

    ArrayList<Product> productList=new ArrayList<>();
    Context context;

    public MyListLayoutAdapter(Context context, ArrayList<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,null);
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
        holder.snoTV.setText(productList.get(position).getId());


    }

    @Override
    public int getItemCount() {

        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTV;
        TextView snoTV;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemTV = (TextView) itemView.findViewById(R.id.itemTV);
            snoTV = (TextView) itemView.findViewById(R.id.snoTV);

        }
    }



    public void setFilter(ArrayList<Product> newList){

        productList = new ArrayList<>();
        productList.addAll(newList);
        notifyDataSetChanged();
    }


}
