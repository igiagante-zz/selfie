package model;

import android.graphics.Bitmap;

/**
 * Created by igiagante on 18/8/15.
 */
public class Item {

    private Long id;
    private String name;
    private String path;
    private Bitmap image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
