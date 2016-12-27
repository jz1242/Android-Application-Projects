package com.example.jz.pathhero;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.app.ActionBar;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class searchForReal extends AppCompatActivity {
    // private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView courseList;
    ArrayList<String> listItems;
    String[] holder;
    ArrayAdapter<String> adapter;
    ListView show;
    EditText editText;
    String[] items=new String[5];
    ArrayList<String[]> courses = new ArrayList<String[]>();
    private static final String API_KEY = "tDxBrZz15UY2cJ258US4qYEuLONUBS4S";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchreal);
        course();
        show = (ListView)findViewById(R.id.listview);

        editText=(EditText)findViewById(R.id.txtsearch);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                initList();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String searchTerm=s.toString();
                    ArrayList<String> results = new ArrayList<String>(5);

                    for (int i = 0; i < 5; i++) {
                        results.add("                                                 " + searchTerm);
                    }

                    for (int i = 0; i < courses.size(); i++) {
                        for (int j = 0; j < 3; j++) {
                            int score = courses.get(i)[j].toLowerCase().indexOf(searchTerm.toLowerCase());
                            if (score >= 0) {
                                for (int k = 0; k < results.size(); k++) {
                                    if (score < results.get(k).indexOf(searchTerm) && !results.contains(courses.get(i)[j])) {
                                        results.set(k, courses.get(i)[j]);
                                    }
                                }
                            }
                        }
                    }

                    // System.out.println(results);

                    for (int i = 0; i < 5; i++) {
                        // System.out.println(i + 1 + ": " + results.get(i));
                        items[i]=results.get(i);

                    }
                    initList();


                }


            @Override
            public void afterTextChanged(Editable s) {
                //searchItem(s.toString());

            }
        });


       // courseList = (ListView) findViewById(R.id.courseList);
        //items = new ArrayList<String>();
       // itemsAdapter =  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
       // courseList.setAdapter(itemsAdapter);
        // items.add("First Item");
        // items.add("Second Item");
    }

    public void course(){
        String schoolList = "Krieger School of Arts and Sciences,Whiting School of Engineering";
        String[] schoolListArr = schoolList.replaceAll(" ", "%20").split(",");
        try {
            for (int i = 0; i < schoolListArr.length; i++) {
                // http://stackoverflow.com/questions/1485708/how-do-i-do-a-http-get-in-java
                StringBuilder result = new StringBuilder();
                URL url = new URL("https://isis.jhu.edu/api/classes/" + schoolListArr[i] + "/current?key=" + API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                JSONParser parser = new JSONParser();
                JSONArray coursesJSON = (JSONArray) parser.parse(result.toString());
                Iterator<JSONObject> classIterator = coursesJSON.iterator();
                JSONObject ic;
                while (classIterator.hasNext()) {
                    ic = classIterator.next();
                    courses.add(new String[]{(String) ic.get("Title"), (String) ic.get("InstructorsFullName"),
                            (String) ic.get("OfferingName") + " (" + ic.get("SectionName") + ")"});
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void initList(){

        ListAdapter a=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        ListView b=(ListView) findViewById(R.id.listview);
        b.setAdapter(a);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
