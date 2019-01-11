package typeface;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by sys on 7/8/2016.
 */
public class AnyButton extends Button {

    public AnyButton(Context context) {
        super(context);
    }

    public AnyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Util.setTypeface(attrs, this);
    }

    public AnyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Util.setTypeface(attrs, this);
    }
}
