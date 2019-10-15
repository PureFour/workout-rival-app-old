package com.example.workout_rival_app;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {
	private static final String SIMPLE_GET_URL = "http://192.168.0.101:8080/workoutRival"; //hardcoded my IP
	private String message = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button loginButton = findViewById(R.id.login_button);
		loginButton.setOnClickListener(
				new View.OnClickListener()
				{
					public void onClick(View view)
					{
						Log.d("login", ((EditText) findViewById(R.id.login_field)).getText().toString());
						Log.d("password", ((EditText) findViewById(R.id.login_field)).getText().toString());
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

	private void setMessage(String message) {
		this.message = message;
	}
}
