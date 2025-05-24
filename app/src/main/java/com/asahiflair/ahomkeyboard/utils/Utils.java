package com.asahiflair.ahomkeyboard.utils;

import static com.asahiflair.ahomkeyboard.utils.Constants.APP_LANGUAGE;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import com.asahiflair.ahomkeyboard.R;
import com.asahiflair.ahomkeyboard.EnableKeyboardActivity;
import com.asahiflair.ahomkeyboard.AhomKeyboardService;

public class Utils {
    private static boolean stopCopyDialog;
    private static boolean themeChange;
    private static boolean emojiKeyboard;
    private static boolean doubleTapOn, changingDoubleTap, updateSharedPreference;
    private static final int[] codesToBeReordered = {4155, 4156, 4157, 4158};


    public static boolean isEmojiKeyboard() {
        return emojiKeyboard;
    }

    public static void setEmojiKeyboard(boolean emojiKeyboard) {
        Utils.emojiKeyboard = emojiKeyboard;
    }

    public static boolean isStopCopyDialog() {
        return stopCopyDialog;
    }

    public static void setStopCopyDialog(boolean stopCopyDialog) {
        Utils.stopCopyDialog = stopCopyDialog;
    }


    public static void setThemeChanged(boolean bool) {
        themeChange = bool;
    }

    public static boolean isThemeChanged() {
        return themeChange;
    }


    public static boolean isEnable(Context context, String name) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(name, false);
    }

    public static boolean isMyServiceRunning(Context context, Class serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int getKeyboardTheme(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String theme = sharedPreferences.getString("chooseTheme", "1");
        return Integer.parseInt(theme);
    }


    public static boolean isChangingDoubleTap() {
        return changingDoubleTap;
    }

    public static void setChangingDoubleTap(boolean changingDoubleTap) {
        Utils.changingDoubleTap = changingDoubleTap;
    }

    public static boolean isDoubleTapOn(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("enableDoubleTap", false);
    }

    public static boolean isCodeToBeReordered(int code) {
        for (int codeToBeReordered : codesToBeReordered) {
            if (codeToBeReordered == code) {
                return true;
            }
        }
        return false;
    }


    public static void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void setAppLocale(Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

        public static ArrayList<Integer> initArrayList(int... ints) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i : ints) {
            list.add(i);
        }
        return list;
    }

    public static void initLanguage(Context context) {
        String langCode = PrefManager.getStringValue(context, APP_LANGUAGE);
        setAppLocale(context, langCode);
    }

}
