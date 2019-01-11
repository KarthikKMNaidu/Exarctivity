package imageCropPackage;

/**
 * Created by sys on 3/21/2016.
 */
public class ImageCropList {

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUserImageName() {
        return userImageName;
    }

    public void setUserImageName(String userImageName) {
        this.userImageName = userImageName;
    }

    String columnId="", imagePath="", userImageName="";
    public ImageCropList(String id, String imagePath, String userImageName) {
        this.columnId=id;
        this.imagePath=imagePath;
        this.userImageName=userImageName;

    }
}

