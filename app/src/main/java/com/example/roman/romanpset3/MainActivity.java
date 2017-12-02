package com.example.roman.romanpset3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<List<String>> order;
    public List<String> itemlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue queue = Volley.newRequestQueue(this);

        final Button orderButton = (Button) findViewById(R.id.orderButton);
        orderButton.setOnClickListener(new OrderButtonClickListener());

        Intent intent = getIntent();
        if (intent.hasExtra("b")) {
            Bundle bundle = intent.getBundleExtra("b");
            order = (List<List<String>>) bundle.getSerializable("order");
        }
        else {
            order = new ArrayList<List<String>>();
        }

        itemlist = new ArrayList<String>();
        String url = "https://resto.mprog.nl/menu";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray jArray = null;
                        try {
                            jArray = jsonObject.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i = 0; i < jArray.length(); i++){
                            String itemname = null;
                            try {
                                itemname = jArray.getJSONObject(i).getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("ADD", "onResponse: " + itemname);
                            itemlist.add(itemname);
                        }
                        SharedPreferences prefs = MainActivity.this.getSharedPreferences("settings", MainActivity.MODE_PRIVATE);
                        for(int i = 0;i < itemlist.size(); i++) {
                            Log.d("ADD", "onCreate: " + itemlist.get(i));
                            if (prefs.contains(itemlist.get(i))) {
                                List<String> x = new ArrayList<String>();
                                x.add(itemlist.get(i));
                                x.add(prefs.getString(itemlist.get(i), "0"));
                                order.add(x);
                            }
                        }
                        if (order.size() > 0) {
                            orderButton.setAlpha(1);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);







        final List<String> list = new ArrayList<String>();
        final ListView listView = (ListView) findViewById(R.id.catList);
        String url1 = "https://resto.mprog.nl/categories";
        // Request a string response from the provided URL.
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray jArray = null;
                        try {
                            jArray = jsonObject.getJSONArray("categories");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i = 0; i < jArray.length(); i++){
                            String itemname = null;
                            try {
                                itemname = jArray.getString(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            list.add(itemname);

                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(
                                        MainActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        list);
                        listView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest1);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Intent intent =new Intent(MainActivity.this, SecondActivity.class);

                String value = (String)adapter.getItemAtPosition(position);
                intent.putExtra("choice", value);

                Bundle b = new Bundle();
                b.putSerializable("order", (Serializable)order);
                intent.putExtra("b", b);
                startActivity(intent);
                finish();
            }
        });
    }

    private class OrderButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (order.size() > 0) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("order", (Serializable) order);
                intent.putExtra("b", b);
                startActivity(intent);
                finish();
            }
        }
    }

    public void onBackPressed() {
        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        for(int i = 0; i < order.size(); i++) {
            editor.putString(order.get(i).get(0), order.get(i).get(1));
        }
        editor.commit();

        finish();
    }
}
