package com.mostafa_fathi.attended_me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mostafa_fathi.attended_me.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    RecyclerView.LayoutManager layoutManager;
    ClassAdapter adapter;
    ArrayList<ClassItem> classList = new ArrayList<>();
    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        dbHelper=new DbHelper(this);

        binding.fabMain.setOnClickListener(v -> showDialog());


        binding.recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(layoutManager);
        adapter = new ClassAdapter(classList, this,binding.emptyClassesList);
        binding.recyclerview.setAdapter(adapter);

        adapter.setLiscener(position -> goToItemActivity(position));

        setToolBar();
        loadData();

    }

    private void loadData() {
        Cursor cursor=dbHelper.getClassTable();

        classList.clear();
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(DbHelper.C_ID));
            String className=cursor.getString(cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY));
            String subjectName=cursor.getString(cursor.getColumnIndex(DbHelper.SUBJECT_NAME_KEY));

            classList.add(new ClassItem(id,className,subjectName));

        }
    }

    private void setToolBar() {
        TextView title = binding.mainToolbar.findViewById(R.id.toolbar_title);
        TextView subTitle = binding.mainToolbar.findViewById(R.id.toolbar_subtitle);
        ImageButton back = binding.mainToolbar.findViewById(R.id.toolbar_back);
        ImageButton save = binding.mainToolbar.findViewById(R.id.toolbar_save);


        title.setText("Classes");
        subTitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void goToItemActivity(int position) {
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra("ClassName", classList.get(position).getClassName());
        intent.putExtra("SubjectName", classList.get(position).getSubjectName());
        intent.putExtra("Position", position);
        intent.putExtra("CID", classList.get(position).getCid());

        startActivity(intent);
    }

    private void showDialog() {

        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.CLASS_ADD_DIALOG);
        dialog.setLiscener((className, subjectName) -> addClass(className, subjectName));
    }

    private void addClass(String className, String subjectName) {

        long cid=dbHelper.addClass(className,subjectName);

        ClassItem classItem=new ClassItem(cid,className,subjectName);

        classList.add(classItem);

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());

        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog=new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_UPDATE_DIALOG);
        dialog.setLiscener((className,subjectName)->updateClass(position,className,subjectName));

    }

    private void updateClass(int position, String className, String subjectName) {
        dbHelper.UpdateClass(classList.get(position).getCid(),className,subjectName);
        classList.get(position).setClassName(className);
        classList.get(position).setSubjectName(subjectName);

        adapter.notifyItemChanged(position);

    }

    private void deleteClass(int position) {
        dbHelper.deleteClass(classList.get(position).getCid());
        classList.remove(position);
        adapter.notifyItemRemoved(position);
    }
}