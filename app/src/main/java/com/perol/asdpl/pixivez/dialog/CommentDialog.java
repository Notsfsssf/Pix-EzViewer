package com.perol.asdpl.pixivez.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.perol.asdpl.pixivez.R;
import com.perol.asdpl.pixivez.activity.UserMActivity;
import com.perol.asdpl.pixivez.adapters.CommentAdapter;
import com.perol.asdpl.pixivez.networks.RestClient;
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices;
import com.perol.asdpl.pixivez.objects.ReFreshFunction;
import com.perol.asdpl.pixivez.objects.Toasty;
import com.perol.asdpl.pixivez.responses.IllustCommentsResponse;
import com.perol.asdpl.pixivez.services.AppApiPixivService;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class CommentDialog extends DialogFragment {

    RecyclerView recyclerviewPicture;


    EditText edittextComment;

    Button button;

    private ImageView textviewuserhead;
    private TextView textviewguanzhuzhe;
    private TextView textviewhaoPyou;
    private TextView textviewuserbody;
    private TextView textViewhold;
    private Context context;
    private String Authorization;
    private SharedPreferencesServices sharedPreferencesServices;
    private CommentAdapter commentAdapter;
    private Long id;
    private int Parent_comment_id = 1;
    private AppApiPixivService appApiPixivService;

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "ViewDialogFragment");
    }

    public static CommentDialog newInstance(Long id) {
        CommentDialog commentDialog = new CommentDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        commentDialog.setArguments(bundle);
        return commentDialog;
    }

    private void getData() {

        RestClient restClient = new RestClient();
        appApiPixivService = restClient.getRetrofit_AppAPI().create(AppApiPixivService.class);
        Observable.just(1).flatMap(new Function<Integer, ObservableSource<IllustCommentsResponse>>() {
            @Override
            public ObservableSource<IllustCommentsResponse> apply(Integer integer) throws Exception {
                Authorization = sharedPreferencesServices.getString("Authorization");
                return appApiPixivService.getIllustComments(Authorization, id);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ReFreshFunction(context))
                .subscribe(new Observer<IllustCommentsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final IllustCommentsResponse illustCommentsResponse) {
                        commentAdapter = new CommentAdapter(R.layout.view_comment_item, illustCommentsResponse.getComments(), context);
                        recyclerviewPicture.setNestedScrollingEnabled(false);
                        recyclerviewPicture.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                        recyclerviewPicture.setAdapter(commentAdapter);
                        recyclerviewPicture.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
                        commentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);

                                String comment = illustCommentsResponse.getComments().get(position).getComment();
                                builder.setMessage(comment);
                                androidx.appcompat.app.AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                        commentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                                if (view.getId() == R.id.commentuserimage) {
                                    Intent intent = new Intent(context, UserMActivity.class);

                                    intent.putExtra("data", illustCommentsResponse.getComments().get(position).getUser().getId());
                                    startActivity(intent);
                                }
                                if (view.getId() == R.id.repley_to_hit) {
                                    Parent_comment_id = illustCommentsResponse.getComments().get(position).getId();
                                    edittextComment.setHint("回复:" + illustCommentsResponse.getComments().get(position).getUser().getName());
                                }
                            }
                        });
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                commit();
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

    public void commit() {
        appApiPixivService.postIllustComment(Authorization, id, edittextComment.getText().toString(), Parent_comment_id == 1 ? null : Parent_comment_id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Observable.just(1).flatMap(new Function<Integer, ObservableSource<IllustCommentsResponse>>() {
                            @Override
                            public ObservableSource<IllustCommentsResponse> apply(Integer integer) throws Exception {
                                Authorization = sharedPreferencesServices.getString("Authorization");
                                return appApiPixivService.getIllustComments(Authorization, id);
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .retryWhen(new ReFreshFunction(context))
                                .subscribe(new Observer<IllustCommentsResponse>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(final IllustCommentsResponse illustCommentsResponse) {

                                        commentAdapter.setNewData(illustCommentsResponse.getComments());
                                        Toast.makeText(context, "评论成功！", Toast.LENGTH_SHORT).show();
                                        edittextComment.setText("");
                                        Parent_comment_id=1;
                                        edittextComment.setHint("");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (((HttpException) e).response().code() == 403) {

                                            Toasty.Companion.warning(context, "Rate Limit", Toast.LENGTH_SHORT).show();


                                        } else if (((HttpException) e).response().code() == 404) {


                                        }

                                    }

                                    @Override
                                    public void onComplete() {

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
    public void onStart() {
        super.onStart();



        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM; // 显示在底部
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度填充满屏
        window.setAttributes(params);

        // 这里用透明颜色替换掉系统自带背景
        int color = ContextCompat.getColor(getActivity(), android.R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(color));
    }



    public interface Callback {
        void onClick();

    }

    private Callback callback;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initbind();
        this.context = getActivity().getApplicationContext();
        sharedPreferencesServices = new SharedPreferencesServices(context);
    }

    private void initbind() {
getData();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        id = bundle.getLong("id");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_comment, null);
        recyclerviewPicture= view.findViewById(R.id.recyclerview_picture);
       edittextComment = view.findViewById(R.id.edittext_comment);
       button = view.findViewById(R.id.button);
        builder.setView(view);

        this.context = getActivity().getApplicationContext();
        sharedPreferencesServices = new SharedPreferencesServices(context);
        getData();
        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}
