package com.sample.to_do;

import Data.DatabaseHandler;
import Modal.TaskItems;
import UI.RecyclerAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class taskDetails extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DatabaseHandler db;
    private EditText tskIp;
    private Button saveBtn;
    private CardView card;
    private String d;
    private StringBuilder sb;
    private FloatingActionButton addBtn;
    private List<TaskItems> tasksList1,tasksList2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        db=new DatabaseHandler(this);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar2);
        card=(CardView) findViewById(R.id.cardviewDisp);
        tskIp=(EditText)findViewById(R.id.IpTask);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d");
        Date date = new Date();
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        Calendar cal = Calendar.getInstance();
        String month = monthName[cal.get(Calendar.MONTH)];
        d=formatter.format(date)+" "+month+"";
        toolbar.setTitle(formatter.format(date)+" "+month+"");
        byPassActivity();
        addBtn=(FloatingActionButton)findViewById(R.id.editBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tskIp.getText().toString().isEmpty()) {
                    saveToDb();
                }
            }
        });
    }

    private void saveToDb() {
        TaskItems taskItems=new TaskItems();
        String newTask=tskIp.getText().toString();
        taskItems.setTask(newTask);
        db.addTask(taskItems);
        startActivity(new Intent(taskDetails.this, taskDetails.class));
        finish();
    }

    private void byPassActivity() {
        if(db.getCount()>0){
            recyclerView = (RecyclerView) findViewById(R.id.recyclerViewDisp);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            tasksList1 = new ArrayList<>();
            tasksList2 = new ArrayList<>();
            sb=new StringBuilder();
            sb.append("Tasks for ->"+d+"\n");
            tasksList1=db.getAll();
            int i=0;
            for(TaskItems x:tasksList1){
                TaskItems items=new TaskItems();
                items.setTask(x.getTask());
                sb.append((i+1)+". "+x.getTask()).append("\n");
                tasksList2.add(items);
                i++;
            }
            adapter = new RecyclerAdapter(this, tasksList2);
            recyclerView.setAdapter(adapter);
        }else{
            recyclerView = (RecyclerView) findViewById(R.id.recyclerViewDisp);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            tasksList1 = new ArrayList<>();
            adapter = new RecyclerAdapter(this, tasksList1);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.share_menu){
            Intent shareText=new Intent();
            shareText.setAction(Intent.ACTION_SEND);
            shareText.setType("text/plain");
            shareText.putExtra(Intent.EXTRA_TEXT, sb.toString());
            Intent share=Intent.createChooser(shareText,null);
            startActivity(share);
        }
        return true;
    }

}