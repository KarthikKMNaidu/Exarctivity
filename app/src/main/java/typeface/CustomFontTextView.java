package typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.exarcplus.exarctivity.R;


public class CustomFontTextView extends TextView {
    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);

        String fontName = attributeArray.getString(R.styleable.CustomFontTextView_style);
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, fontName, textStyle);
        setTypeface(customFont);

        attributeArray.recycle();
    }

    private Typeface selectTypeface(Context context, String fontName, int textStyle) {
        if (fontName.contentEquals(context.getString(R.string.boldfont))) {
            return FontCache.getTypeface("fonts/Poppins-Bold.ttf", context);
        } else if (fontName.contentEquals(context.getString(R.string.normalfont))) {
            return FontCache.getTypeface("fonts/Poppins-Medium.ttf", context);
        }
        else if(fontName.contentEquals(context.getString(R.string.italicfont))){
            return FontCache.getTypeface("fonts/Poppins-Regular.ttf", context);

        }else if(fontName.contentEquals(context.getString(R.string.lightfont))){
            return FontCache.getTypeface("fonts/Poppins-Light.ttf", context);
        }

        return null;

    }
}
