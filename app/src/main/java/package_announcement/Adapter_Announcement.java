package package_announcement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.exarcplus.exarctivity.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import setting_package.SessionManager;
import typeface.CustomFontTextView;


public class Adapter_Announcement extends PagerAdapter {

    private Context context;
    ArrayList<Announcement_Array> arrayList=new ArrayList<>();
    private LayoutInflater layoutInflater;
    SessionManager session;


    public Adapter_Announcement(Context context, ArrayList<Announcement_Array> arrayList) {
        this.context = context;
        this.arrayList=arrayList;

        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((CardView)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_announcement, container, false);


        String conv_to_time="", conv_from_time="";

        CustomFontTextView title_tv=(CustomFontTextView)view.findViewById(R.id.title_tv);
        CustomFontTextView description_tv=(CustomFontTextView)view.findViewById(R.id.description_tv);


        title_tv.setText(arrayList.get(position).getTitle());
        description_tv.setText(arrayList.get(position).getDescription());

        description_tv.cancelLongPress();
        description_tv.setLongClickable(false);

        ((ViewPager) container).addView(view);

        return view;
    }



    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((CardView) object);
    }



}

