package package_attendance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.exarcplus.exarctivity.R;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;
import typeface.CustomFontTextView;



public class Adapter_Attendance extends RecyclerView.Adapter<Adapter_Attendance.ItemRowHolder> {


    OnItemClickListener mListener;
    private ArrayList<Attendance_Array> attendance_Array=new ArrayList<>();
    Context mContext;
   

    public Adapter_Attendance(Context mContext, ArrayList<Attendance_Array> attendance_Array) {
        this.attendance_Array = attendance_Array;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_attendance_list, viewGroup, false);

        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder itemRowHolder, @SuppressLint("RecyclerView") int i) {


        itemRowHolder.name_tv.setText(attendance_Array.get(i).getName());


        if(attendance_Array.get(i).getProfile_image()!=null && !attendance_Array.get(i).getProfile_image().equals("")){
            Picasso.with(mContext).load(attendance_Array.get(i).getProfile_image()).placeholder(R.mipmap.profile_gray).into(itemRowHolder.pro_image);
        }else{
            itemRowHolder.pro_image.setImageResource(R.mipmap.profile_gray);
        }


        if(attendance_Array.get(i).getOut_time().equals("") && attendance_Array.get(i).getIn_time().equals("") ){
            itemRowHolder.time_tv.setText("Not Scanned");
        }else if(attendance_Array.get(i).getOut_time().equals("")){
            itemRowHolder.time_tv.setText("IN : "+attendance_Array.get(i).getIn_time());
        }else{
            itemRowHolder.time_tv.setText("IN : "+attendance_Array.get(i).getIn_time()+", OUT : "+attendance_Array.get(i).getOut_time());
        }



        if(!attendance_Array.get(i).getIn_time().equals("") && !attendance_Array.get(i).getOut_time().equals("")){
            itemRowHolder.total_time_tv.setVisibility(View.VISIBLE);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            Date date_in = null,date_out = null;
            try {
                date_in = simpleDateFormat.parse(attendance_Array.get(i).getIn_time());
                date_out = simpleDateFormat.parse(attendance_Array.get(i).getOut_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert date_out != null;
            Long difference = date_out.getTime() - date_in.getTime();
            String days = String.valueOf(difference / (1000 * 60 * 60 * 24));
            String hours = String.valueOf((difference - 1000 * 60 * 60 * 24 * Integer.parseInt(days)) / (1000 * 60 * 60));
            String min = String.valueOf((difference - 1000 * 60 * 60 * 24 * Integer.parseInt(days) - 1000 * 60 * 60 * Integer.parseInt(hours))/(1000 * 60));
            // hours = if(hours < 0) -hours else hours

            if(Integer.parseInt(hours) < 0){
                int hrs = -Integer.parseInt(hours);
                hours = String.valueOf(hrs);
            }else{
            }

            itemRowHolder.total_time_tv.setText("Total hrs : "+hours+" Hrs "+min+" Min");
            Log.e("Fragment_QR_Scan", " difference_hours ==> "+hours);
        }else{
            itemRowHolder.total_time_tv.setVisibility(View.GONE);
            itemRowHolder.total_time_tv.setText("");
        }



        itemRowHolder.click_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, -1, "view_all", itemRowHolder.getPosition());
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        // return  notify_list.size();
        return (null != attendance_Array ? attendance_Array.size() : 0);
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder {

        CustomFontTextView name_tv,time_tv,total_time_tv;
        LinearLayout click_full;
        CircleImageView pro_image;


        public ItemRowHolder(View view) {
            super(view);
            this.click_full = view.findViewById(R.id.click_full);
            this.name_tv = view.findViewById(R.id.name_tv);
            this.time_tv = view.findViewById(R.id.time_tv);
            this.total_time_tv = view.findViewById(R.id.total_time_tv);
            this.pro_image = view.findViewById(R.id.pro_image);

        }

    }



    public interface OnItemClickListener

    {
        public void onItemClick(View view, int position, String status, int viewPosition);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mListener = mItemClickListener;
    }




}
