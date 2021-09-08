package com.mostafa_fathi.attended_me;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mostafa_fathi.attended_me.databinding.ActivitySheetBinding;

import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {

    private ActivitySheetBinding binding;

    long[] idArray;
    int[] rollArray;
    String[] nameArray;
    String month;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sheet);

        dbHelper=new DbHelper(this);
        idArray = getIntent().getLongArrayExtra("idArray");
        rollArray = getIntent().getIntArrayExtra("rollArray");
        nameArray = getIntent().getStringArrayExtra("nameArray");
        month = getIntent().getExtras().getString("month");

        showTable();

        setToolBar();

    }

    private void showTable() {
        TableLayout tableLayout = findViewById(R.id.table_layout);

        int DAY_IN_MONTH = getDayInMonth(month);

        //raw setup
        int row_size = idArray.length + 1;
        TableRow[] rows = new TableRow[row_size];
        TextView[] roll_tvs = new TextView[row_size];
        TextView[] name_tvs = new TextView[row_size];
        TextView[][] status_tvs = new TextView[row_size][DAY_IN_MONTH + 1];
        for (int i = 0; i < row_size; i++) {
            roll_tvs[i]=new TextView(this);
            name_tvs[i]=new TextView(this);


            for (int j = 1; j <= DAY_IN_MONTH; j++) {
                status_tvs[i][j]=new TextView(this);
            }
        }
        roll_tvs[0].setText("Roll");
        roll_tvs[0].setTypeface(roll_tvs[0].getTypeface(), Typeface.BOLD);
        name_tvs[0].setText("Name");
        name_tvs[0].setTypeface(name_tvs[0].getTypeface(), Typeface.BOLD);


        for (int i = 1; i <= DAY_IN_MONTH; i++) {
            status_tvs[0][i].setText(String.valueOf(i));
            status_tvs[0][i].setTypeface(status_tvs[0][i].getTypeface(), Typeface.BOLD);

            status_tvs[0][i].setTextColor(Color.parseColor("#FFFFFFFF"));

        }
        for (int i = 1; i < row_size; i++) {

            roll_tvs[i].setText(String.valueOf(rollArray[i-1]));
            name_tvs[i].setText(nameArray[i-1]);

            for (int j = 1; j <=DAY_IN_MONTH ; j++) {



                String day=String.valueOf(j);
                if (day.length()==1)
                    day="0"+day;

                String date=day+"."+month;
                String status=dbHelper.getStatus(idArray[i-1],date);

                status_tvs[i][j].setText(status);
            }
        }

        for (int i = 0; i < row_size; i++) {
            rows[i]=new TableRow(this);

            if (i==0){
                rows[i].setBackgroundColor(Color.parseColor("#EEEC608F"));
            }

            if (i==0){
                roll_tvs[i].setTextColor(Color.parseColor("#FFFFFFFF"));
                name_tvs[i].setTextColor(Color.parseColor("#FFFFFFFF"));
            }


            if (i%2!=0)
                rows[i].setBackgroundColor(Color.parseColor("#33FF0000"));

//                rows[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
//            else
//                rows[i].setBackgroundColor(Color.parseColor("#ffff"));



            roll_tvs[i].setPadding(16,16,16,16);
            name_tvs[i].setPadding(16,16,16,16);

            rows[i].addView(roll_tvs[i]);
            rows[i].addView(name_tvs[i]);

            for (int j = 1; j <= DAY_IN_MONTH; j++) {
                status_tvs[i][j].setPadding(16,16,16,16);
                rows[i].addView(status_tvs[i][j]);
            }
            tableLayout.addView(rows[i]);

        }
        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);

    }

    private int getDayInMonth(String month) {
        int monthIndex = Integer.parseInt(month.substring(0, 1));
        int year = Integer.parseInt(month.substring(4));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.YEAR, year);


        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void setToolBar() {
        TextView title = binding.sheetToolbar.findViewById(R.id.toolbar_title);
        TextView subTitle = binding.sheetToolbar.findViewById(R.id.toolbar_subtitle);
        ImageButton back = binding.sheetToolbar.findViewById(R.id.toolbar_back);
        ImageButton save = binding.sheetToolbar.findViewById(R.id.toolbar_save);

        title.setText("Attendance");
        save.setVisibility(View.GONE);
        subTitle.setText(month);
//        subTitle.setVisibility(View.GONE);
        back.setOnClickListener(v->finish());


    }
}