package android.carmaludes.todo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class TodoActivity extends Activity {
    private ArrayList<String> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        arrayListToDo = new ArrayList<String>();
        arrayAdapterToDo = new  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListToDo);
        ListView todoList = (ListView)findViewById(R.id.todoList);
        todoList.setAdapter(arrayAdapterToDo);

        registerForContextMenu(todoList);

        try {
            Log.i("ON CREATE", "The onCreate has occurred.");

            Scanner scanner = new Scanner(openFileInput("ToDo.txt"));

            while(scanner.hasNextLine()) {
                String toDo = scanner.nextLine();
                arrayAdapterToDo.add(toDo);
            }

            scanner.close();
        }
        catch(Exception e) {
            Log.i("ON CREATE", e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() != R.id.todoList) {
            return;
        }

        menu.setHeaderTitle("What would you like to do?");

        String[] options = {"Delete Task", "Return"};

        for(String option : options) {
            menu.add(option);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int selectedIndex = info.position;

        if(item.getTitle().equals("Delete Task")) {
            arrayListToDo.remove(selectedIndex);
            arrayAdapterToDo.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        try {
            Log.i("ON BACK PRESSED", "The onBackPressed event has occurred");

            PrintWriter pw = new PrintWriter(openFileOutput("Todo.txt", Context.MODE_PRIVATE));

            for(String toDo : arrayListToDo) {
                pw.println();
            }

            pw.close();
        }
        catch(Exception e) {
            Log.i("ON BACK PRESSED", e.getMessage());
        }
    }

    public void addItem(View v) {
        EditText addItem = (EditText)findViewById(R.id.addItem);
        String toDo = addItem.getText().toString();

        if(toDo.isEmpty()) {
            return;
        }

        arrayAdapterToDo.add(toDo);
        addItem.setText("");
    }

}
