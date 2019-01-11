package typeface;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sys on 7/8/2016.
 */
public class AnyTextView extends TextView {

    public AnyTextView(Context context) {
        super(context);
    }

    public AnyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Util.setTypeface(attrs, this);
    }

    public AnyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Util.setTypeface(attrs, this);
    }
}