package com.mostafa_fathi.attended_me;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mostafa_fathi.attended_me.databinding.ActivityStudentBinding;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    private ActivityStudentBinding binding;
    private String cName, sName;
    private long cid;
    private int position;
    private RecyclerView.LayoutManager layoutManager;
    private StudentAdapter adapter;
    private ArrayList<StudentItem> studentList = new ArrayList<>();
    private DbHelper dbHelper;
    private MyCalender calender;
    private TextView subTitle,emptyMessage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student);
        subTitle = binding.studentToolbar.findViewById(R.id.toolbar_subtitle);
        dbHelper=new DbHelper(this);
        calender=new MyCalender();
        cName = getIntent().getExtras().getString("ClassName");
        sName = getIntent().getExtras().getString("SubjectName");
        position = getIntent().getExtras().getInt("Position");
        cid=getIntent().getExtras().getLong("CID");
        setToolBar(cName,sName);

        binding.studentRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.studentRecycler.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(studentList, this,binding.emptyStudentList);
        binding.studentRecycler.setAdapter(adapter);

        adapter.setLiscener(position -> changeStatus(position));


        binding.fabStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStudentDialog();

            }
        });
        loadData();
        loadStatusDate();


    }
    private void loadData() {
        Cursor cursor=dbHelper.getStudentTable(cid);

        studentList.clear();
        while (cursor.moveToNext()){
            long sid=cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            int roll=cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            String name=cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));

            studentList.add(new StudentItem(sid,roll,name));
        }

    }

    private void changeStatus(int position) {
        String status=studentList.get(position).getStatus();
        if (status.equals("P"))
            status="A";
        else
            status="P";

        studentList.get(position).setStatus(status);

        adapter.notifyItemChanged(position);
    }

    private void setToolBar(String classtitle,String classsubtitle) {
        TextView title = binding.studentToolbar.findViewById(R.id.toolbar_title);
        ImageButton back = binding.studentToolbar.findViewById(R.id.toolbar_back);
        ImageButton save = binding.studentToolbar.findViewById(R.id.toolbar_save);

        save.setOnClickListener(v->saveStatus());

        Toolbar toolbar=findViewById(R.id.the_tool_bar);
        title.setText(classtitle);
//        subTitle.setText(classsubtitle);
        subTitle.setText(classsubtitle+" | "+calender.getDate());
        back.setOnClickListener(v->finish());

        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));


    }

    private void saveStatus() {
        for (StudentItem studentItem: studentList){
            String status=studentItem.getStatus();
            if (!status.equals("P"))
                status="A";
            long value = dbHelper.addStatus(studentItem.getSid(),cid,calender.getDate(),status);

            if (value==-1)
                dbHelper.updateStatus(studentItem.getSid(),calender.getDate(),status);
        }
        Toast.makeText(this, "Attendance was taken and saved", Toast.LENGTH_LONG).show();
    }

    private void loadStatusDate(){
        for (StudentItem studentItem: studentList){
            String status=dbHelper.getStatus(studentItem.getSid(),calender.getDate());
            if (status!=null)
                studentItem.setStatus(status);
            else
                studentItem.setStatus("");

        }
        adapter.notifyDataSetChanged();
    }



    private boolean onMenuItemClick(MenuItem menuItem) {
//        if (menuItem.getItemId()==R.id.add_student){
//            showAddStudentDialog();
//        }

        if (menuItem.getItemId()== R.id.show_caleder){
            showCalender();
        }
        else if (menuItem.getItemId()== R.id.show_attendance_sheet){
            openAttendanceSheet();
        }
        return true;
    }


    private void openAttendanceSheet() {
        long[] idArray=new long[studentList.size()];
        for (int i=0;i<idArray.length;i++)
            idArray[i]=studentList.get(i).getSid();

        int[] rollArray=new int[studentList.size()];
        for (int i=0;i<rollArray.length;i++)
            rollArray[i]=studentList.get(i).getRoll();

        String[] nameArray=new String[studentList.size()];
        for (int i=0;i<nameArray.length;i++)
            nameArray[i]=studentList.get(i).getName();

        Intent intent=new Intent(this,SheetListActivity.class);
        intent.putExtra("sheet_CID",cid);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        startActivity(intent);
    }

    private void showCalender() {
        calender.show(getSupportFragmentManager(),"");
        calender.setListener((this::onCalenderOkClicked));
    }

    private void onCalenderOkClicked(int year, int month, int day) {
        calender.setDate(year,month,day);
        subTitle.setText(sName+" | "+calender.getDate());
        loadStatusDate();
    }

    private void showAddStudentDialog() {
        MyDialog dialog=new MyDialog(studentList.size());


        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setLiscener((roll,name)->addStudent(roll,name));
    }

    private void addStudent(String roll_string, String name) {
        int roll=Integer.parseInt(roll_string);
        long sid=dbHelper.addStudent(cid,roll,name);

        studentList.add(new StudentItem(sid,roll,name));
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog=new MyDialog(studentList.get(position).getRoll(),studentList.get(position).getName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setLiscener((roll_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position, String name) {
        dbHelper.UpdateStudent(studentList.get(position).getSid(),name);
        studentList.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }


    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentList.get(position).getSid());
        studentList.remove(position);
        adapter.notifyItemRemoved(position);
    }
}