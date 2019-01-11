package package_holidays.adapter_package;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.exarcplus.exarctivity.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import package_holidays.array_package.Array_Holiday_list;
import typeface.CustomFontTextView;



public class Adapter_Holiday_List extends RecyclerView.Adapter<Adapter_Holiday_List.ItemRowHolder> {


    OnItemClickListener mListener;
    private ArrayList<Array_Holiday_list> holiday_list=new ArrayList<>();
    private ArrayList<Array_Holiday_list> arraylist;
    Context mContext;
    int lastPosition=0;
    int delayAnimate=300;


    public Adapter_Holiday_List(Context mContext, ArrayList<Array_Holiday_list> holiday_list) {
        this.holiday_list = holiday_list;
        this.mContext = mContext;
        this.arraylist = new ArrayList<Array_Holiday_list>();
        this.arraylist.addAll(holiday_list);
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_holiday_list, viewGroup, false);

        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder itemRowHolder, @SuppressLint("RecyclerView") int i) {


        itemRowHolder.name_tv.setText(holiday_list.get(i).getName());
        itemRowHolder.date_tv.setText(holiday_list.get(i).getFrom_date());


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat showing_day = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);


        if(holiday_list.get(i).getTo_date().equals("")){

            Date getting_formate = null;
            String conv_from_date="";

            if(holiday_list.get(i).getFrom_date()!=null && !holiday_list.get(i).getFrom_date().equals("")){
                try {
                    getting_formate = simpleDateFormat.parse(holiday_list.get(i).getFrom_date());
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                conv_from_date = showing_day.format(getting_formate);
            }else{
                conv_from_date="";
            }

            itemRowHolder.date_tv.setText(conv_from_date);

        }else{
            Date from_formate = null,to_formate = null;
            String conv_from_date="";  String conv_to_date="";

            if(holiday_list.get(i).getFrom_date()!=null && !holiday_list.get(i).getFrom_date().equals("")){
                try {
                    from_formate = simpleDateFormat.parse(holiday_list.get(i).getFrom_date());
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                conv_from_date = showing_day.format(from_formate);
            }else{
                conv_from_date="";
            }


            if(holiday_list.get(i).getTo_date()!=null && !holiday_list.get(i).getTo_date().equals("")){
                try {
                    to_formate = simpleDateFormat.parse(holiday_list.get(i).getTo_date());
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                conv_to_date = showing_day.format(to_formate);
            }else{
                conv_to_date="";
            }

            itemRowHolder.date_tv.setText(conv_from_date+" to "+conv_to_date);
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
        return (null != holiday_list ? holiday_list.size() : 0);
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder {

        CustomFontTextView name_tv,date_tv;
        LinearLayout click_full;

        public ItemRowHolder(View view) {
            super(view);
            this.click_full = (LinearLayout) view.findViewById(R.id.click_full);
            this.name_tv = (CustomFontTextView) view.findViewById(R.id.name_tv);
            this.date_tv = (CustomFontTextView) view.findViewById(R.id.date_tv);

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



    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        holiday_list.clear();
        if (charText.length() == 0) {
            holiday_list.addAll(arraylist);
        }
        else
        {
            for (Array_Holiday_list wp : arraylist)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    holiday_list.add(wp);
                }else if(wp.getStatus().toLowerCase(Locale.getDefault()).contains(charText)) {
                    holiday_list.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }

}
