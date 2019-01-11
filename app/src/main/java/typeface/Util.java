package typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.exarcplus.exarctivity.R;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sys on 7/8/2016.
 */
public class Util {
    public static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();

    public static void setTypeface(AttributeSet attrs, TextView textView) {
        if (textView.isInEditMode()) return;

        Context context = textView.getContext();

        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.AnyTextView);
        String typefaceName = values.getString(R.styleable.AnyTextView_typeface);

        if (typefaceCache.containsKey(typefaceName)) {
            textView.setTypeface(typefaceCache.get(typefaceName));
        } else {
            Typeface typeface;
            try {
                typeface = Typeface.createFromAsset(textView.getContext().getAssets(), context.getString(R.string.assets_fonts_folder) + typefaceName);
            } catch (Exception e) {
                
                return;
            }

            typefaceCache.put(typefaceName, typeface);
            textView.setTypeface(typeface);
        }

        values.recycle();
    }
}
