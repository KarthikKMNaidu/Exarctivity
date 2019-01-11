package package_announcement;


public class Announcement_Array {

    String id;
    String name;
    String title;
    String description;
    String date;
    String status;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Announcement_Array(String id, String name, String title, String description, String date, String status){

        this.id=id;
        this.name=name;
        this.title=title;
        this.description=description;
        this.date=date;
        this.status=status;

    }



}
