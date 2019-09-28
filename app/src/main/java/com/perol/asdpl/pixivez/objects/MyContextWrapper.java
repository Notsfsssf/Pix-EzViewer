package com.perol.asdpl.pixivez.objects;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class MyContextWrapper extends android.content.ContextWrapper {

    public MyContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrap(Context context, Locale newLocale) {

        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);

        } else {

            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);

        }

        return new ContextWrapper(context);
    }
}