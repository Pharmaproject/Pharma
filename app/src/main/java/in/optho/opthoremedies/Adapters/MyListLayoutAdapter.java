package in.optho.opthoremedies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.Activities.ProductActivity;
import in.optho.opthoremedies.R;
import in.optho.opthoremedies.SessionHelper.SessionManager;

/**
 * Created by krishna on 3/10/17.
 */

public class MyListLayoutAdapter extends RecyclerView.Adapter<MyListLayoutAdapter.MyViewHolder> {

    public ArrayList<Product> productList=new ArrayList<>();
    Context context;

    public MyListLayoutAdapter(Context context, ArrayList<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Clicked "+viewType, Toast.LENGTH_SHORT).show();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTV;
        TextView snoTV;


        public MyViewHolder(final View itemView) {
            super(itemView);

            itemTV = (TextView) itemView.findViewById(R.id.itemTV);
            snoTV = (TextView) itemView.findViewById(R.id.snoTV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Product product = productList.get(getAdapterPosition());

                    Intent intent = new Intent(itemView.getContext(), ProductActivity.class);
                    intent.putExtra("PRODUCT", product);
                    itemView.getContext().startActivity(intent);

                    //increase the counter by 1 on each click
                    SessionManager sessionManager = new SessionManager(itemView.getContext());
                    sessionManager.setCounter(product.getId(),sessionManager.getCounter(product.getId()));



                }
            });

        }
    }



    public void setFilter(ArrayList<Product> newList){

        productList = new ArrayList<>();
        productList.addAll(newList);
        notifyDataSetChanged();
    }


}
