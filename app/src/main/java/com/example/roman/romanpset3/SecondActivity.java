package com.example.roman.romanpset3;

import android.content.Intent;
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SecondActivity extends AppCompatActivity {

    public List<List<String>> order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final Button orderbutton = (Button) findViewById(R.id.button2);
        orderbutton.setOnClickListener(new OrderButtonClickListener());

        final Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new BackButtonClickListener());


        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("b");
        order = (List<List<String>>) bundle.getSerializable("order");

        if (order.size() > 0) {
            orderbutton.setAlpha(1);
        }

        String choice = intent.getStringExtra("choice");
        final ListView listView = (ListView) findViewById(R.id.itemList);
        final List<String> list = new ArrayList<String>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://resto.mprog.nl/menu?category=" + choice;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
                            list.add(itemname);

                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<String>(
                                            SecondActivity.this,
                                            android.R.layout.simple_list_item_1,
                                            list);
                            listView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                String value = (String)adapter.getItemAtPosition(i);

                boolean hasPut = FALSE;

                for(int j =0;j < order.size(); j++) {
                    if (order.get(j).get(0) == value) {
                        List<String> x = new ArrayList<String>();
                        x.add(value);
                        int orderamount = Integer.parseInt(order.get(j).get(1));
                        orderamount += 1;
                        x.add(Integer.toString(orderamount));
                        order.set(j, x);
                        hasPut = TRUE;
                        Log.d("CREATION", "onItemClick: added to order");
                    }
                }

                if (!hasPut) {
                    List<String> x = new ArrayList<String>();
                    x.add(value);
                    x.add("1");
                    order.add(x);
                    Log.d("CREATION", "onItemClick: made new order");
                }

                if (order.size() > 0) {
                    orderbutton.setAlpha(1);
                }
            }
        });
    }

    private class OrderButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (order.size() > 0) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("order", (Serializable) order);
                intent.putExtra("b", b);
                startActivity(intent);
                finish();
            }
        }

    }

    private class BackButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("order", (Serializable) order);
            intent.putExtra("b", b);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("order", (Serializable) order);
        intent.putExtra("b", b);
        startActivity(intent);
        finish();
    }

}
