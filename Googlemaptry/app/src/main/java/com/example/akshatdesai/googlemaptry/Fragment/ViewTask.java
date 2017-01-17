package com.example.akshatdesai.googlemaptry.Fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.General.EnablePermission;
import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.example.akshatdesai.googlemaptry.client.ViewPagerAdapter;


public class ViewTask extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private SwipeRefreshLayout swipeContainer;
    private ViewPager pager;
    private TabLayout slidingTabLayout;



    public ViewTask() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_task_manager, container, false);

       // pb = (ProgressBar) view.findViewById(R.id.progressBar_managerviewtask);
        //toolbar = (Toolbar)view.findViewById(R.id.toolbar);


        pager = (ViewPager) view.findViewById(R.id.task_pager);
        setViewPager(pager);
        slidingTabLayout = (TabLayout) view.findViewById(R.id.task_tabs);
        slidingTabLayout.setupWithViewPager(pager);

     /*   swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.activity_manager_view_task);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                if(EnablePermission.isInternetConnected(getActivity())) {
                   // new Web().execute();
                   // notifyDataSetChanged();
                   // adapter.refresh();
                }else{
                    Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_LONG).show();
                }

                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/







        return view;
    }

    private void setViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new TaskComplete(),"Completed");
        adapter.addFragment(new TaskRunning(),"Running");
        adapter.addFragment(new TaskScheduled(),"Scheduled");
        viewPager.setAdapter(adapter);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
