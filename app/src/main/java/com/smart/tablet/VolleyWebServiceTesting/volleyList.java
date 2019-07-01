package com.smart.tablet.VolleyWebServiceTesting;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.tablet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class volleyList extends AppCompatActivity {

    private TextView mTextviewResult;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley_list);

        mTextviewResult = findViewById(R.id.txt_view_result);
        Button btnParse = findViewById(R.id.btn_parse);


        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonParse();
            }
        });
    }

    private void JsonParse() {
        String url = "https://api.myjson.com/bins/kp9wz";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("employees");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject empl = jsonArray.getJSONObject(i);
                        String firstName = empl.getString("firstname");
                        int age = empl.getInt("age");
                        String mail = empl.getString("mail");

                        mTextviewResult.append(firstName + ", " + String.valueOf(age) + ", " + mail + "\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
