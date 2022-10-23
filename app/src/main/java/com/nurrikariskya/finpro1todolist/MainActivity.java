 package com.nurrikariskya.finpro1todolist;

 import androidx.appcompat.app.AlertDialog;
 import androidx.appcompat.app.AppCompatActivity;

 import android.os.Bundle;

 import android.content.ContentValues;
 import android.content.DialogInterface;
 import android.database.Cursor;
 import android.database.sqlite.SQLiteDatabase;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.ArrayAdapter;
 import android.widget.EditText;
 import android.widget.ListView;
 import android.widget.TextView;

 import java.util.ArrayList;

 import data.DB1;
 import data.DB2;

 public class MainActivity extends AppCompatActivity {
     private static final String TAG = "MainActivity";
     private DB2 taskHelper;
     private ListView TaskList;
     private ArrayAdapter<String> arrAdapter;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         taskHelper = new DB2(this);
         TaskList = (ListView) findViewById(R.id.list_todo);

         updateUI();
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menus, menu);
         return super.onCreateOptionsMenu(menu);
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case R.id.add_to_do:
                 final EditText taskEdit = new EditText(this);
                 AlertDialog dialog = new AlertDialog.Builder(this)
                         .setTitle("Add a new task").setMessage("What do you want to do next?").setView(taskEdit)
                         .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 String task = String.valueOf(taskEdit.getText());
                                 SQLiteDatabase db = taskHelper.getWritableDatabase();
                                 ContentValues values = new ContentValues();
                                 values.put(DB1.TaskEntry.COL_TASK_TITLE, task);
                                 db.insertWithOnConflict(DB1.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                                 db.close();
                                 updateUI();
                             }
                         })
                         .setNegativeButton("Cancel", null).create();
                 dialog.show();
                 return true;

             default:
                 return super.onOptionsItemSelected(item);
         }
     }

     public void deleteTask(View view) {
         View parent = (View) view.getParent();
         TextView taskTextView = (TextView) parent.findViewById(R.id.title_task);
         String task = String.valueOf(taskTextView.getText());
         SQLiteDatabase db = taskHelper.getWritableDatabase();
         db.delete(DB1.TaskEntry.TABLE, DB1.TaskEntry.COL_TASK_TITLE + " = ?", new String[]{task});
         db.close();
         updateUI();
     }

     private void updateUI() {
         ArrayList<String> taskList = new ArrayList<>();
         SQLiteDatabase db = taskHelper.getReadableDatabase();
         Cursor cursor = db.query(DB1.TaskEntry.TABLE,
                 new String[]{DB1.TaskEntry._ID, DB1.TaskEntry.COL_TASK_TITLE},
                 null, null, null, null, null);
         while (cursor.moveToNext()) {
             int idx = cursor.getColumnIndex(DB1.TaskEntry.COL_TASK_TITLE);
             taskList.add(cursor.getString(idx));
         }

         if (arrAdapter == null) {
             arrAdapter = new ArrayAdapter<>(this, R.layout.todo, R.id.title_task, taskList);
             TaskList.setAdapter(arrAdapter);
         } else {
             arrAdapter.clear();
             arrAdapter.addAll(taskList);
             arrAdapter.notifyDataSetChanged();
         }

         cursor.close();
         db.close();
     }
 }