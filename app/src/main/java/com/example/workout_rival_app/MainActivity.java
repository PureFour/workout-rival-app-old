package com.example.workout_rival_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String SIMPLE_GET_URL = "http://192.168.0.101:8080/workoutRival"; //hardcoded my IP
    private static final String POST_USER_URL = "http://192.168.0.101:8080/workoutRival/users";
    private String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button singInButton = findViewById(R.id.singIn_button);
        singInButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        final String login = ((EditText) findViewById(R.id.login_field)).getText().toString();
                        final String password = ((EditText) findViewById(R.id.password_field)).getText().toString();
                        authorizeUser(login, password);
                    }
                });

        Button singUpButton = findViewById(R.id.singUp_button);
        singUpButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        final String login = ((EditText) findViewById(R.id.login_field)).getText().toString();
                        final String password = ((EditText) findViewById(R.id.password_field)).getText().toString();
                        Log.d("login", ((EditText) findViewById(R.id.login_field)).getText().toString());
                        Log.d("password", ((EditText) findViewById(R.id.password_field)).getText().toString());
                        addNewUser(login, password);
                    }
                });
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
        if (id == R.id.Quit) {
            this.finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    private String getSimpleRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest getRequest = new StringRequest(Request.Method.GET, SIMPLE_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setMessage(response);
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);
        if (message.isEmpty()) {
            return "\"This will be coming from backend service !\"";
        }
        return this.message;
    }

    private void authorizeUser(final String login, final String password) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String AUTHORIZATION_USER_URL = "http://192.168.0.101:8080/workoutRival/users/login?login=" + login +"&password="+password;
        final StringRequest authRequest = new StringRequest(Request.Method.GET, AUTHORIZATION_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("\n\n\n\n "+ response + "\n\n\n\n");
                        checkResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("login", login);
                params.put("password", password);
                return params;
            }

        };
        queue.add(authRequest);
    }

    private void checkResponse(String response) {
        if (response.equals("true")) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


        private void addNewUser(final String login, final String password) {
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> data = new HashMap<String, String>();
        data.put("email", login);
        data.put("login", login);
        data.put("password", password);

        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, POST_USER_URL, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
        };
        queue.add(postRequest);
        }

    private void setMessage(String message) {
        this.message = message;
    }
}
