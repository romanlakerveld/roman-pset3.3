package com.example.roman.romanpset3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    public List<List<String>> order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("b");
        order =(List<List<String>>) bundle.getSerializable("order");

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new BackButtonClickListener());

        Button orderButton = (Button) findViewById(R.id.submitButton);
        orderButton.setOnClickListener(new OrderButtonClickListener());

        ListView listView = (ListView) findViewById(R.id.orderList);

        final CustomAdapter customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                SharedPreferences prefs = ThirdActivity.this.getSharedPreferences("settings", ThirdActivity.this.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove(order.get(i).get(0));
                editor.commit();

                order.remove(i);
                customAdapter.notifyDataSetChanged();

            }
        });


    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return order.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlayout, null);

            TextView textView_item = (TextView) view.findViewById(R.id.textView_name);
            TextView textView_amount = (TextView) view.findViewById(R.id.textView_amount);

            textView_item.setText(order.get(i).get(0));
            textView_amount.setText(order.get(i).get(1));

            return view;
        }
    }
    private class BackButtonClickListener implements View.OnClickListener {
        public void onClick(View view)  {
            Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("order", (Serializable) order);
            intent.putExtra("b", b);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("order", (Serializable) order);
        intent.putExtra("b", b);
        startActivity(intent);
        finish();
    }

    private class OrderButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            order.clear();
            SharedPreferences prefs = ThirdActivity.this.getSharedPreferences("settings", ThirdActivity.this.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
