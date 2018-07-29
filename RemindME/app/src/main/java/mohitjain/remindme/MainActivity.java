package mohitjain.remindme;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listview);
        dbHelper = new DBHelper(this);

        loadTask();
    }

    //Add icon to menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //LOAD ALL TASKS
    private void loadTask(){
        ArrayList<String> taskList = dbHelper.getTaskList();

        if(mAdapter == null){
            mAdapter = new ArrayAdapter<String>(this, R.layout.lay, R.id.task_title, taskList);
            listView.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addTask:
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.addition, null);
                final EditText heading = alertLayout.findViewById(R.id.title1);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("ADD NEW TASK");
                dialog.setMessage("Whats your task");
                dialog.setView(alertLayout);
                dialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = heading.getText().toString();
                        dbHelper.insertNewTask(task);
                        loadTask();

                    }
                });
                dialog.setNegativeButton("CANCEL", null);
                AlertDialog alert = dialog.create();
                alert.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void deleteTask(View view){
        try{
            int index = listView.getPositionForView(view);
            String task = mAdapter.getItem(index++);
            dbHelper.deleteTask(task);
            loadTask();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }
    }


}
