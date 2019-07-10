package com.perol.asdpl.pixivez.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;

import com.perol.asdpl.pixivez.R;

import java.text.SimpleDateFormat;

/**
 * Created by Notsfsssf on 2018/3/17.
 */

public class DateDialog extends DialogFragment {
    private CalendarView calendarView;
    private int year, month, day;

    public CalendarView getCalendarView() {
        return calendarView;
    }

    private String finaldata;

    public void setCalendarView(CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getFinaldata() {
        return finaldata;
    }

    public void setFinaldata(String finaldata) {
        this.finaldata = finaldata;
    }

    public interface Callback {
        void onClick(String data);

    }

    public Callback callback;

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "ViewDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_data, null);
        builder.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    CalendarView calendarView = view.findViewById(R.id.calendarview_);
                    Log.d("data", String.valueOf(calendarView.getDate()));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    callback.onClick(year + "-" + month + "-" + day);
//                    callback.onClick(String.valueOf(sdf.format(calendarView.getDate())));
                }
            }

        });
        CalendarView calendarView = view.findViewById(R.id.calendarview_);
        calendarView.setMaxDate(System.currentTimeMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {


            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                System.out.println(year+":"+month+":"+dayOfMonth);
                setDay(dayOfMonth);
                setYear(year);
                setMonth(month+1);
            }
        });


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
