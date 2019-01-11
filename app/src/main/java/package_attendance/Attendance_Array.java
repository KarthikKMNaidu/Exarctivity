package package_attendance;

public class Attendance_Array {


    String id;
    String admin_status;
    String profile_image;
    String name;
    String date;
    String in_time;
    String out_time;
    String status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmin_status() {
        return admin_status;
    }

    public void setAdmin_status(String admin_status) {
        this.admin_status = admin_status;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public Attendance_Array(String id, String admin_status, String profile_image, String name, String date, String in_time, String out_time, String status){

        this.id=id;
        this.admin_status=admin_status;
        this.profile_image=profile_image;
        this.name=name;
        this.date=date;
        this.in_time=in_time;
        this.out_time=out_time;
        this.status=status;
    }




}
