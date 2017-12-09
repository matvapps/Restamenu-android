package com.restamenu.util;

/**
 * @author Roodie
 */

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.URLSpan;

public final class TextUtils {

    private TextUtils() {
    }

    public static boolean isEmpty(@Nullable CharSequence text) {
        return text == null || text.length() == 0;
    }

    public static boolean equals(@Nullable CharSequence a, @Nullable CharSequence b) {
        if (a == b) {
            return true;
        }

        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static Spanned toSpanned(final Context context, final String toastString, @ColorRes final int toastColor) {
        final String string = "" + context.getResources().getColor(toastColor);
        final Spannable spannable = (Spannable) Html.fromHtml(colorize_a(colorize_em(toastString, string, true), string));
        for (final URLSpan urlSpan : (URLSpan[]) spannable.getSpans(0, spannable.length(), (Class) URLSpan.class)) {
            spannable.setSpan(urlSpan, spannable.getSpanStart(urlSpan), spannable.getSpanEnd(urlSpan), 0);
        }
        return spannable;
    }

    private static String colorize_a(final String s, final String s2) {
        return nullToEmpty(s).replaceAll("<a (.+?)>(.+?)</a>", String.format("<b><font color='%s'><a $1>$2</a></font></b>", s2));
    }

    private static String colorize_em(final String s, final String s2, final boolean b) {
        final String nullToEmpty = nullToEmpty(s);
        String string = "";
        if (b) {
            string += "<b>";
        }
        String s3 = string + "<font color='%s'>$1</font>";
        if (b) {
            s3 += "</b>";
        }
        return nullToEmpty.replaceAll("<em>(.+?)</em>", String.format(s3, s2));
    }

    public static String nullToEmpty(String s) {
        if (s == null) {
            s = "";
        }
        return s;
    }
}
