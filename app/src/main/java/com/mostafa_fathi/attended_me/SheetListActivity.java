package com.mostafa_fathi.attended_me;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mostafa_fathi.attended_me.databinding.ActivitySheetListBinding;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {

    private ActivitySheetListBinding binding;
    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList<String> listItems=new ArrayList();
    private long cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sheet_list);


        cid=getIntent().getExtras().getLong("sheet_CID");
        loadListItems();

        sheetList=findViewById(R.id.sheetlist);
        adapter=new ArrayAdapter(this, R.layout.sheet_list, R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActivity(position));

        setToolBar();
    }

    private void openSheetActivity(int position) {
        long[] idArray=getIntent().getLongArrayExtra("idArray");
        int[] rollArray=getIntent().getIntArrayExtra("rollArray");
        String[] nameArray=getIntent().getStringArrayExtra("nameArray");

        Intent intent=new Intent(this,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("month",listItems.get(position));


        startActivity(intent);
    }

    private void loadListItems() {
        Cursor cursor=new DbHelper(this).getDistinctMonths(cid);

        while (cursor.moveToNext()){
            String date=cursor.getString(cursor.getColumnIndex(DbHelper.DATE_KEY));
            listItems.add(date.substring(3));
        }
    }
    private void setToolBar() {
        TextView title = binding.sheetListToolbar.findViewById(R.id.toolbar_title);
        TextView subTitle = binding.sheetListToolbar.findViewById(R.id.toolbar_subtitle);
        ImageButton back = binding.sheetListToolbar.findViewById(R.id.toolbar_back);
        ImageButton save = binding.sheetListToolbar.findViewById(R.id.toolbar_save);

        title.setText("Attendance Date");
        save.setVisibility(View.GONE);
        subTitle.setVisibility(View.GONE);
        back.setOnClickListener(v->finish());


    }
}