package com.perol.asdpl.pixivez.objects;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.activity.PictureActivity;
import com.perol.asdpl.pixivez.networks.RestClient;
import com.perol.asdpl.pixivez.responses.IllustNext;
import com.perol.asdpl.pixivez.services.AppApiPixivService;
import com.perol.asdpl.pixivez.services.GlideApp;

import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link NewAppWidgetConfigureActivity NewAppWidgetConfigureActivity}
 */
public class NewAppWidget extends AppWidgetProvider {


    @Override
    public void onReceive(Context context, Intent intent) {


        super.onReceive(context, intent);

    }

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {
        RestClient restClient = new RestClient();

        AppApiPixivService appApiPixivService = restClient.getRetrofit_AppAPI().create(AppApiPixivService.class);
        appApiPixivService.walkthroughIllusts().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<IllustNext>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(IllustNext walkthroughResponse) {
                // Construct the RemoteViews object
                Random rand = new Random();
                 int  randomnum = rand.nextInt(walkthroughResponse.getIllusts().size() - 2);
                final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
                Bundle bundle = new Bundle();
                bundle.putLong("illustid", walkthroughResponse.getIllusts().get(randomnum).getId());
                long[] illustlist= new long[1];
                illustlist[0]=walkthroughResponse.getIllusts().get(randomnum).getId();
                bundle.putLongArray("illustlist", illustlist);
                Intent intent=new Intent(context, PictureActivity.class);
                intent.putExtras(bundle);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.widget_image, pendingIntent);
                GlideApp.with(context.getApplicationContext()).asBitmap().load(walkthroughResponse.getIllusts().get(randomnum).getImage_urls().getMedium())
                        .transform(new RoundedCornersTransformation(24, 0,
                                RoundedCornersTransformation.CornerType.ALL)).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        views.setImageViewBitmap(R.id.widget_image, resource);
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            NewAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

