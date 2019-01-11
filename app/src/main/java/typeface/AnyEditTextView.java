package typeface;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by sys on 7/8/2016.
 */
public class AnyEditTextView extends EditText {

    public AnyEditTextView(Context context) {
        super(context);
    }

    public AnyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Util.setTypeface(attrs, this);
    }

    public AnyEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Util.setTypeface(attrs, this);
    }
}
