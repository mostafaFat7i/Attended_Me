package com.mostafa_fathi.attended_me;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG="Add Class";
    public static final String CLASS_UPDATE_DIALOG="Update Class";
    public static final String STUDENT_ADD_DIALOG="Add Student";
    public static final String STUDENT_UPDATE_DIALOG = "Update Student";


    private OnClickLiscener liscener;
    private int roll;
    private String name;

    public MyDialog(int roll, String name) {

        this.roll = roll;
    }

    public MyDialog(int roll) {
        this.roll = roll;
    }

    public MyDialog() {

    }

    public interface OnClickLiscener{
        void onClick(String txt1,String txt2);
    }

    public void setLiscener(OnClickLiscener liscener) {
        this.liscener = liscener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog=null;

        if (getTag().equals(CLASS_ADD_DIALOG))
            dialog=getAddClassDialog();

        if (getTag().equals(STUDENT_ADD_DIALOG))
            dialog=getAddStudentDialog();

        if (getTag().equals(CLASS_UPDATE_DIALOG))
            dialog=getUpdateClassDialog();

        if (getTag().equals(STUDENT_UPDATE_DIALOG))
            dialog=getUpdateStudentDialog();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title=view.findViewById(R.id.title_dialog);
        title.setText("Update Student");

        EditText roll_edit=view.findViewById(R.id.edt01);
        EditText name_edit=view.findViewById(R.id.edt02);



        roll_edit.setHint("New Roll");
        name_edit.setHint("New Name");

        Button add=view.findViewById(R.id.add_btn);
        add.setText("Update");
        Button cancel=view.findViewById(R.id.cancel_btn);
        roll_edit.setText(roll+"");
        roll_edit.setEnabled(false);
        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v->{
            String roll=roll_edit.getText().toString();
            String name=name_edit.getText().toString();


            if (!name.equals(""))
                liscener.onClick(roll,name);
            else
                Toast.makeText(getActivity(), "Make sure you are enter new student name", Toast.LENGTH_SHORT).show();

            dismiss();

        });


        return builder.create();
    }

    private Dialog getUpdateClassDialog() {
        EditText class_name,subject_name;
        Button add,cancel;

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title=view.findViewById(R.id.title_dialog);
        title.setText("Update Class");

        class_name=view.findViewById(R.id.edt01);
        subject_name=view.findViewById(R.id.edt02);

        class_name.setHint("Class New Name");
        subject_name.setHint("Subject New Name");

        add=view.findViewById(R.id.add_btn);
        add.setText("Update");
        cancel=view.findViewById(R.id.cancel_btn);

        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v->{
            String className=class_name.getText().toString();
            String subjectName=subject_name.getText().toString();

            if (!className.equals("") && !subjectName.equals(""))
                liscener.onClick(className,subjectName);
            else
                Toast.makeText(getActivity(), "Make sure you are enter class name and subject name", Toast.LENGTH_SHORT).show();


            dismiss();
        });


        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title=view.findViewById(R.id.title_dialog);
        title.setText("New Student");

        EditText roll_edit=view.findViewById(R.id.edt01);
        EditText name_edit=view.findViewById(R.id.edt02);

//        roll_edit.setHint("Roll");

        roll_edit.setText(String.valueOf(roll+1));
        roll_edit.setEnabled(false);
        name_edit.setHint("Name");

        Button add=view.findViewById(R.id.add_btn);
        Button cancel=view.findViewById(R.id.cancel_btn);

        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v->{
            String roll=roll_edit.getText().toString();
            String name=name_edit.getText().toString();
            roll_edit.setText(String.valueOf(Integer.parseInt(roll)+1));
            name_edit.setText("");

            if (!roll_edit.equals("") && !name_edit.equals(""))
                liscener.onClick(roll,name);
            else
                Toast.makeText(getActivity(), "Make sure you are enter class name and subject name", Toast.LENGTH_SHORT).show();
        });


        return builder.create();
    }

    private Dialog getAddClassDialog() {

        EditText class_name,subject_name;
        Button add,cancel;

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title=view.findViewById(R.id.title_dialog);
        title.setText("New Class");

        class_name=view.findViewById(R.id.edt01);
        subject_name=view.findViewById(R.id.edt02);

        class_name.setHint("Class Name");
        subject_name.setHint("Subject Name");

        add=view.findViewById(R.id.add_btn);
        cancel=view.findViewById(R.id.cancel_btn);

        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v->{
            String className=class_name.getText().toString();
            String subjectName=subject_name.getText().toString();

            if (!className.equals("") && !subjectName.equals(""))
                liscener.onClick(className,subjectName);
            else
                Toast.makeText(getActivity(), "Make sure you are enter class name and subject name", Toast.LENGTH_SHORT).show();


            dismiss();
        });


        return builder.create();
    }
}
