package edu.gatech.hci.foodnavigator.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.widget.TextView;
import edu.gatech.hci.foodnavigator.R;

public class CustomTextView extends TextView {

	private final static int NORMAL = 0;
	private final static int BOLD = 1;
	private final static int ITALIC = 2;
	private final static int BOLD_ITALIC = 3;
	private static final String DEFAULT_FONT_TYPE = "Aller.ttf";

	private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(2);

	private Typeface getTypeface(String typefaceName, Context context) {
		Typeface mTypeface = sTypefaceCache.get(typefaceName);
		if (mTypeface == null) {
			mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), typefaceName);
			sTypefaceCache.put(typefaceName, mTypeface);
		}
		return mTypeface;
	}

	public CustomTextView(Context context) {
		super(context);
		setCustomFont(context, DEFAULT_FONT_TYPE, NORMAL);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// Typeface.createFromAsset doesn't work in the layout editor
		if (isInEditMode()) {
			return;
		}
		TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
		String fontName = attr.getString(R.styleable.CustomTextView_font);
		int textStyle = attr.getInt(R.styleable.CustomTextView_textStyle, 0);
		attr.recycle();
		setCustomFont(context, fontName, textStyle);
	}

	private void setCustomFont(Context context, String fontName, int textStyle) {
		if (fontName == null) {
			fontName = DEFAULT_FONT_TYPE;
		}
		Typeface typeface = getTypeface(fontName, context);
		int style = 0;
		switch (textStyle) {
		case NORMAL:
			style = Typeface.NORMAL;
			break;
		case BOLD:
			style = Typeface.BOLD;
			break;
		case ITALIC:
			style = Typeface.ITALIC;
			break;
		case BOLD_ITALIC:
			style = Typeface.BOLD_ITALIC;
			break;
		}
		setTypeface(typeface, style);
	}
}
