package com.perol.asdpl.pixivez.objects

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.bumptech.glide.request.target.AppWidgetTarget
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PictureActivity
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.responses.IllustNext
import com.perol.asdpl.pixivez.services.AppApiPixivService
import com.perol.asdpl.pixivez.services.GlideApp
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.util.*

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [NewAppWidgetConfigureActivity]
 */
class NewAppWidget : AppWidgetProvider() {


    override fun onReceive(context: Context, intent: Intent) {


        super.onReceive(context, intent)

    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them

        for (appWidgetId in appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {
            val restClient = RestClient()

            val appApiPixivService = restClient.retrofit_AppAPI.create(AppApiPixivService::class.java)
            appApiPixivService.walkthroughIllusts().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(object : Observer<IllustNext> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(walkthroughResponse: IllustNext) {
                            // Construct the RemoteViews object
                            val rand = Random()
                            val randomnum = rand.nextInt(walkthroughResponse.illusts.size - 2)
                            val views = RemoteViews(context.packageName, R.layout.new_app_widget)
                            val bundle = Bundle()
                            bundle.putLong("illustid", walkthroughResponse.illusts[randomnum].id)
                            val illustlist = LongArray(1)
                            illustlist[0] = walkthroughResponse.illusts[randomnum].id
                            bundle.putLongArray("illustlist", illustlist)
                            val intent = Intent(context, PictureActivity::class.java)
                            intent.putExtras(bundle)
                            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                            views.setOnClickPendingIntent(R.id.widget_image, pendingIntent)
                            GlideApp.with(context.applicationContext).asBitmap().load(walkthroughResponse.illusts[randomnum].image_urls.medium)
                                    .transform(RoundedCornersTransformation(24, 0,
                                            RoundedCornersTransformation.CornerType.ALL)).into(object : AppWidgetTarget(context.applicationContext, R.id.widget_image, views, appWidgetId) {})
//                            appWidgetManager.updateAppWidget(appWidgetId, views)

                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })

        }
    }
}

