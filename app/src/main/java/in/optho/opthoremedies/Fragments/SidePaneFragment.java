package in.optho.opthoremedies.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.optho.opthoremedies.Activities.MainListActivity;
import in.optho.opthoremedies.Adapters.MyListLayoutAdapter;
import in.optho.opthoremedies.Adapters.MySideListAdapter;
import in.optho.opthoremedies.Database.ProductDatabaseHelper;
import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;

import java.util.ArrayList;
import java.util.List;


public class SidePaneFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    ArrayList<Product> productdb=new ArrayList<>();

    private MySideListAdapter listAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SidePaneFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SidePaneFragment newInstance(int columnCount) {
        SidePaneFragment fragment = new SidePaneFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }




        ProductDatabaseHelper db = new ProductDatabaseHelper(getActivity());
        productdb=db.getProductList();
        listAdapter = new MySideListAdapter(getActivity(), productdb);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sidepane_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(listAdapter);
            recyclerView.setHasFixedSize(true);        }
        return view;
    }

}
