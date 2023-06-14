package com.unipi.boidanis.firebasetest1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomDialog extends DialogFragment {
    private EditText editText;
    private Button button;
    CalendarView calendarView;
    private String height,hint,condition,error_message;
    Date date, lastDate;
    private CustomDialogListener listener;

    public void setCustomDialogListener(CustomDialogListener listener) {
        this.listener = listener;
    }
    public CustomDialog() {
        // Empty constructor required for DialogFragment
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the custom layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        editText = dialogView.findViewById(R.id.editTextTextPersonName8);
        editText.setHint(hint);
        calendarView = dialogView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year,month,dayOfMonth);
                if(year==0||dayOfMonth==0||month==0){
                    c=Calendar.getInstance();
                }
                date=c.getTime();
            }
        });
        button = dialogView.findViewById(R.id.button12);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                    onPause();
                }else{
                if(!editText.getText().toString().matches("")) {
                    height = editText.getText().toString();

                    boolean check = validateinfo(String.valueOf(height));
                    if(check){
                        int difference = WeekCalculation(lastDate,date);
                        if(date.getTime()>lastDate.getTime()&&difference<=1){
                            listener.onDialogResult(date,height);
                            dismiss();
                        }else{
                            showMessage("Please enter a later date than your last recorded:",""+lastDate+"and no more than 7 days away from the last");
                            //Toast.makeText(getContext(), "Please enter a later date than your last recorded:"+lastDate + "and no more than 7 days away from the last", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Check your parameters or remove space", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(getContext(), "Please Enter "+hint, Toast.LENGTH_SHORT).show();

                }
            }
            }
        });
        builder.setView(dialogView);

        return builder.create();
    }
    public interface CustomDialogListener {
        void onDialogResult(Date selectedDate,String height);
    }
    public int WeekCalculation(Date date1,Date date) {
        long i = date1.getTime();
        long j = date.getTime();
        long daysDiff = TimeUnit.DAYS.convert(j - i, TimeUnit.MILLISECONDS);//604800//1 week
        long k = (long) Math.floor(daysDiff / 7.0);
        return (int) k;
    }
    private boolean validateinfo(String height) {
        if(!height.matches(condition)){
            editText.requestFocus();
            editText.setError(error_message);
            return false;
        }else{
            return true;
        }
    }
    void showMessage(String title, String message) {
        new androidx.appcompat.app.AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }

}
