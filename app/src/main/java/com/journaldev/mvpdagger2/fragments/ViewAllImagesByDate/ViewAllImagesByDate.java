package com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate;


//grid:layout_columnWeight="1"

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.Data.ItemPhotoData;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.adapters.GridPhotoAdapter;
import com.journaldev.mvpdagger2.utils.FabricEvents;
import com.journaldev.mvpdagger2.utils.MeasurementLaunchTime;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAllImagesByDate extends Fragment {


    @BindView(R.id.DataList)
    ListView DataList;
    Unbinder unbinder;

    public static ViewAllImagesByDate getInstance() {
        ViewAllImagesByDate fragment = new ViewAllImagesByDate();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmentviewallimagesbydate, container, false);
        unbinder = ButterKnife.bind(this, view);
        Uri[] uri = ImageUrls.getUrls(getContext());
        ItemPhotoData photo = new ItemPhotoData(uri);

        final ArrayList<ItemPhotoData> arrayList = new ArrayList<>();
        arrayList.add(photo);

        GridPhotoAdapter adapter = new GridPhotoAdapter(getActivity().getApplicationContext(), arrayList);
        DataList.setAdapter(adapter);

        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                MeasurementLaunchTime.loadTime = System.currentTimeMillis();
                FabricEvents.sendLaunchTime(MeasurementLaunchTime.getLaunchTime());
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
