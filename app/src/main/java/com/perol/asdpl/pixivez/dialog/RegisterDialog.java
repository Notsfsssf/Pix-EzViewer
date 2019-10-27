/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.activity.HelloMActivity;
import com.perol.asdpl.pixivez.activity.LoginActivity;
import com.perol.asdpl.pixivez.networks.RestClient;
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices;
import com.perol.asdpl.pixivez.objects.Toasty;
import com.perol.asdpl.pixivez.responses.PixivAccountsEditResponse;
import com.perol.asdpl.pixivez.responses.PixivOAuthResponse;
import com.perol.asdpl.pixivez.services.AccountPixivService;
import com.perol.asdpl.pixivez.services.OAuthSecureService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RegisterDialog extends DialogFragment {
    private android.widget.EditText edittextemail;
    private android.widget.EditText edittextnewuseraccount;
    private android.widget.EditText edittextnewpassword;
    private android.widget.Button buttonregister;
    private AccountPixivService accountPixivService;
    private RestClient restClient;
    private Context context;
    private SharedPreferencesServices sharedPreferencesServices;
    List<Integer> list = new ArrayList<>();
    List<Integer> x = Collections.unmodifiableList(list);

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "ViewDialogFragment");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.context = getActivity().getApplicationContext();
        sharedPreferencesServices = new SharedPreferencesServices(context);
    }

//    public interface Callback {
//        void onClick();
//
//    }
//
//    private DetailDialog.Callback callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        restClient = new RestClient();
        accountPixivService = restClient.getRetrofit_Account().create(AccountPixivService.class);
        Bundle bundle = getArguments();
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_register, null);
        this.buttonregister = (Button) view.findViewById(R.id.button_register);
        this.edittextnewpassword = (EditText) view.findViewById(R.id.edittext_new_password);
        this.edittextnewuseraccount = (EditText) view.findViewById(R.id.edittext_new_user_account);

        this.edittextemail = (EditText) view.findViewById(R.id.edittext_email);
        builder.setView(view);
        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accountPixivService.editAccount(edittextemail.getText().toString(), edittextnewuseraccount.getText().toString(), sharedPreferencesServices.getString("password"), edittextnewpassword.getText().toString(), sharedPreferencesServices.getString("Authorization")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<PixivAccountsEditResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(PixivAccountsEditResponse pixivAccountsEditResponse) {
                                if (pixivAccountsEditResponse.getBody().isIs_succeed()) {

                                    RestClient restClient = new RestClient();
                                    OAuthSecureService oAuthSecureService = restClient.getretrofit_OAuthSecure().create(OAuthSecureService.class);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("client_id", "KzEZED7aC0vird8jWyHM38mXjNTY");
                                    map.put("client_secret", "W9JZoJe00qPvJsiyCGT3CCtC6ZUtdpKpzMbNlUGP");
                                    map.put("grant_type", "password");
                                    map.put("username", edittextnewuseraccount.getText().toString());
                                    map.put("password", edittextnewpassword);

                                    if (sharedPreferencesServices.getString("Device_token") != null) {
                                        map.put("device_token", sharedPreferencesServices.getString("Device_token"));


                                    }
                                    oAuthSecureService.postAuthToken(map).subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<PixivOAuthResponse>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(PixivOAuthResponse pixivOAuthResponse) {
                                            sharedPreferencesServices.setString("Device_token", pixivOAuthResponse.getResponse().getDevice_token());
                                            sharedPreferencesServices.setString("Refresh_token", pixivOAuthResponse.getResponse().getRefresh_token());
                                            sharedPreferencesServices.setString("Authorization", "Bearer " + pixivOAuthResponse.getResponse().getAccess_token());
                                            sharedPreferencesServices.setString("client_id", "KzEZED7aC0vird8jWyHM38mXjNTY");
                                            sharedPreferencesServices.setString("client_secret", "W9JZoJe00qPvJsiyCGT3CCtC6ZUtdpKpzMbNlUGP");
                                            sharedPreferencesServices.setString("userid", String.valueOf(pixivOAuthResponse.getResponse().getUser().getId()));
                                            sharedPreferencesServices.setBoolean("islogin", true);
                                            sharedPreferencesServices.setString("username", edittextnewuseraccount.getText().toString());
                                            sharedPreferencesServices.setString("password", edittextnewpassword.getText().toString());
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Toasty.Companion.error(context, "发生错误，请尝试用刚注册的PIXIVID作为用户名登录").show();
                                            Intent intent = new Intent(context, LoginActivity.class);

                                            startActivity(intent);
                                            getActivity().finish();
                                        }

                                        @Override
                                        public void onComplete() {
                                            Toasty.Companion.success(context, "完善验证信息成功", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(context, HelloMActivity.class);
                                            sharedPreferencesServices.setBoolean("isnone", false);
                                            getActivity().finish();
                                            startActivity(intent);

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toasty.Companion.error(context, "完善验证信息失败，请检查邮箱地址，PIXIVID是否有误").show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof DetailDialog.Callback) {
//            callback = (DetailDialog.Callback) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement Callback");
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        callback = null;
//    }
}
