package com.restamenu.util;

import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * Created by Alexandr.
 */
public class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String url) {
        super(url);
    }
    @Override public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}
