package com.journaldev.mvpdagger2.model.Selectable;


import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;

import java.util.ArrayList;

public class ImageSelectableModel extends ImageModel implements ImageSelectable {

    private boolean isSelected;

    public ImageSelectableModel(ImageModel item, boolean isSelected) {
        super(item.getImage(), item.getLike());
        this.isSelected = isSelected;
    }

    public ImageSelectableModel(ImageModel item) {
        super(item.getImage(), item.getLike());
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static String[] convertImagesToStringArray(ArrayList<Selectable> uri) {
        ArrayList<String> str = new ArrayList<>();

        for (int i = 0; i < uri.size(); i++) {
            ArrayList<ImageModel> images = uri.get(i).getImages();
            for (int q = 0; q < images.size(); q++)
                str.add(images.get(q).getImage().toString());
        }

        String[] strUri = new String[str.size()];
        return str.toArray(strUri);
    }

    @Override
    public ArrayList<ImageModel> getImages() {
        ArrayList<ImageModel> imageModels = new ArrayList<>();
        imageModels.add(this);
        return imageModels;
    }
}
