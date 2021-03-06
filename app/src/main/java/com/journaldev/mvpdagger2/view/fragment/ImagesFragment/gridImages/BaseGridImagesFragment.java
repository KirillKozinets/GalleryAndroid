package com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.data.Image.ImageRepository;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.Selectable.ImageSelectableModel;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;
import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.view.activity.GetAlbumActivity;
import com.journaldev.mvpdagger2.view.activity.ViewImagesActivity;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.ImagesAdapter;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.SelectableAdapter;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.SelectableViewHolder;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.BaseSelectableFragment;
import com.journaldev.mvpdagger2.view.utils.DialogsUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseGridImagesFragment extends BaseSelectableFragment implements SelectableViewHolder.OnItemClickListener, SelectableViewHolder.OnItemSelectedListener {

    protected ArrayList<ImageModel> images = null;
    private Unbinder unbinder;
    protected ImagesAdapter adapter;

    @BindView(R.id.DataList)
    RecyclerView DataList;
    @BindView(R.id.exitButton)
    Button exitButton;
    @BindView(R.id.itemSelected)
    TextView itemSelected;
    @BindView(R.id.deleteItemsSelected)
    Button deleteItemsSelected;
    @BindView(R.id.shareButton)
    Button shareButton;
    @BindView(R.id.selectableMenu)
    ConstraintLayout selectablemenu;
    @BindView(R.id.showMenuButton)
    Button showMenuButton;

    private ArrayList<Selectable> selectedItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            images = getArguments().getParcelableArrayList("image");

        if (images == null) {
            App.getApp().initRepositories();
            images = App.getImageRepository().getUrls();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewallimages, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }

    private void showSelectableInstrumentsMenu() {
        selectablemenu.setVisibility(View.VISIBLE);
        itemSelected.setText(Integer.toString(selectedItems.size()));
    }

    protected void initRecyclerView() {
        adapter = new ImagesAdapter(getContext(), images);
        adapter.setSelectedItemClickListener(this);
        adapter.setClickListener(this);
        DataList.setAdapter(adapter);
        DataList.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ViewImagesActivity.class);
        intent.putExtra("idImage", position);
        intent.putParcelableArrayListExtra("uri", images);

        if (App.getAppPreference().getIsAnim()) {
            String name = view.getTransitionName();
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, name);
            startActivity(intent, options.toBundle());
        } else
            startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected() {
        selectedItems = adapter.getSelectedItems();

        if (!adapter.isSelectable())
            showStartInstrumentsMenu();
        else {
            showSelectableInstrumentsMenu();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.selectLove) {
            selectOnlyLove();
        }
        if (item.getItemId() == R.id.move) {
            Intent intent = new Intent(getActivity(), GetAlbumActivity.class);
            startActivityForResult(intent, 1);
        }
        return super.onMenuItemClick(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String albumName = data.getStringExtra("albumName");
            boolean isCreate = data.getBooleanExtra("isCreateAlbum",false);

            if (isCreate) {
                DialogsUtils.showAlbumNameDialog(getContext(), getString(R.string.album_name), this);
            } else {
                createAlbum(albumName);
            }

        }
    }

    private void selectOnlyLove() {
        ArrayList<Selectable> items = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageModel item = images.get(i);
            boolean isLike = item.getLike();
            items.add(new ImageSelectableModel(item, isLike));
        }
        getAdapter().setSelectable(true);
        getAdapter().setImages(items);
    }

    @Override
    protected ArrayList<ImageModel> getImages() {
        return images;
    }

    @Override
    protected ArrayList<Selectable> getSelectedItems() {
        return selectedItems;
    }

    @Override
    protected void setSelectedItems(ArrayList<Selectable> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public void showStartInstrumentsMenu() {
        selectablemenu.setVisibility(View.GONE);
    }

    @Override
    protected SelectableAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected int getMenu() {
        return R.menu.image_selectable_menu;
    }
}

