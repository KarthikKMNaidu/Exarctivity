package package_holidays.array_package;

public class Array_Holiday_list {

    String id;
    String name;
    String from_date;
    String to_date;
    String status;


    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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


    public Array_Holiday_list(String id, String name, String from_date, String to_date, String status){
        this.id = id;
        this.name = name;
        this.from_date = from_date;
        this.to_date = to_date;
        this.status = status;
    }


}
