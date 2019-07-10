package com.perol.asdpl.pixivez.objects;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.perol.asdpl.pixivez.networks.RestClient;
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices;
import com.perol.asdpl.pixivez.responses.PixivOAuthResponse;
import com.perol.asdpl.pixivez.services.OAuthSecureService;
import com.perol.asdpl.pixivez.services.PxEZApp;
import com.perol.asdpl.pixivez.sql.AppDatabase;
import com.perol.asdpl.pixivez.sql.UserEntity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class ReFreshFunction implements io.reactivex.functions.Function<Observable<Throwable>, ObservableSource<?>> {
    private String client_id;
    private String client_secret;
    private SharedPreferencesServices sharedPreferencesServices;
    private Context context;
    private OAuthSecureService oAuthSecureService;
    private int i = 0;
    private int maxRetries = 3;
    private int retryDelayMillis = 1000;
    private int retryCount = 0;
    public ReFreshFunction(Context context) {
        super();
        sharedPreferencesServices = SharedPreferencesServices.getInstance();
        this.context = context;
        this.oAuthSecureService = new RestClient().getretrofit_OAuthSecure().create(OAuthSecureService.class);
        client_id = sharedPreferencesServices.getString("client_id");
        client_secret = sharedPreferencesServices.getString("client_secret");
    }
    private ReFreshFunction() {
        this.context=PxEZApp.instance;
        sharedPreferencesServices = SharedPreferencesServices.getInstance();
        this.oAuthSecureService = new RestClient().getretrofit_OAuthSecure().create(OAuthSecureService.class);
        client_id = sharedPreferencesServices.getString("client_id");
        client_secret = sharedPreferencesServices.getString("client_secret");
    }
    private static volatile ReFreshFunction instance;
    public static ReFreshFunction getInstance() {
        if (instance == null) {
            synchronized (ReFreshFunction.class) {

                if (instance == null) {

                    instance = new ReFreshFunction();

                }

            }

        }
        return instance;

    }
    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException
                        || throwable instanceof ConnectException) {

                    return Observable.just(throwable);
                } else if (throwable instanceof HttpException) {
                    if (((HttpException) throwable).response().code() == 400) {
                        if (++retryCount <= maxRetries) {

                            TToast.Companion.retoken(context);
                            return refreshtoken();
                        } else return Observable.error(throwable);


                    } else if (((HttpException) throwable).response().code() == 404) {
                        if (i == 0) {
                            Log.d("d",((HttpException) throwable).response().message());
                            Toasty.Companion.warning(context, "查找的id不存在"+((HttpException) throwable).response().message(), Toast.LENGTH_SHORT).show();
                            i++;
                        }

                        return Observable.error(throwable);
                    }


                }
                return Observable.error(throwable);


            }
        });

    }

    private ObservableSource<?> refreshtoken() {
        return oAuthSecureService.postRefreshAuthToken(client_id, client_secret, "refresh_token", sharedPreferencesServices.getString("Refresh_token"),
                sharedPreferencesServices.getString("Device_token"), true)
                .subscribeOn(Schedulers.io()).doOnNext(new Consumer<PixivOAuthResponse>() {
                    @Override
                    public void accept(PixivOAuthResponse pixivOAuthResponse) throws Exception {
                        sharedPreferencesServices.setString("Device_token", pixivOAuthResponse.getResponse().getDevice_token());
                        sharedPreferencesServices.setString("Refresh_token", pixivOAuthResponse.getResponse().getRefresh_token());
                        sharedPreferencesServices.setString("Authorization", "Bearer " + pixivOAuthResponse.getResponse().getAccess_token());
                        sharedPreferencesServices.setString("userid", String.valueOf(pixivOAuthResponse.getResponse().getUser().getId()));
                        sharedPreferencesServices.setBoolean("islogin", true);
                        final PixivOAuthResponse.ResponseBean.UserBean user = pixivOAuthResponse.getResponse().getUser();
                        final AppDatabase appdatabase = AppDatabase.Companion.getInstance(PxEZApp.instance);
                        appdatabase.userDao().getIllustHistory().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Subscriber<List<UserEntity>>() {
                            @Override
                            public void onSubscribe(Subscription s) {

                            }

                            @Override
                            public void onNext(List<UserEntity> userEntities) {
                                if (userEntities.size() == 0) {
                                    appdatabase.userDao().insert(new UserEntity(user.getProfile_image_urls().getPx_170x170(), Long.parseLong(user.getId()), user.getName(), user.getMail_address(), user.isIs_premium()));
                                }
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    }
                });
    }


}

