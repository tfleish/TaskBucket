package com.breadcrumbz.android.hello;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class HelloAndroid extends Activity {
	
	EditText helloName;
	TextView helloClass;
	Button buttonToggle;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Capture our button from layout
        Button buttonToast = (Button)findViewById(R.id.go);
        // Register the onClick listener with the implementation above
        buttonToast.setOnClickListener(mAddListener);
        // Capture our button from layout
        buttonToggle = (Button)findViewById(R.id.toggle);
        // Register the onClick listener with the implementation above
        buttonToggle.setOnClickListener(cAddListener);
    }
    
    // Create an anonymous implementation of OnClickListener
    private OnClickListener mAddListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		long id = 0;
    		// do something when button is clicked
    		try
    		{
    			helloName = (EditText)findViewById(R.id.helloName);
    			Context context = getApplicationContext();
    			CharSequence text = "Hello " + helloName.getText() + "!";
    			int duration = Toast.LENGTH_LONG;
    			Toast toast = Toast.makeText(context, text, duration);
    			toast.show();
    		}
    		catch (Exception ex)
    		{
    			Context context = getApplicationContext();
    			CharSequence text = ex.toString() + "ID = " + id;
    			int duration = Toast.LENGTH_LONG;
    			Toast toast = Toast.makeText(context, text, duration);
    			toast.show();
    		}
    	}
    };
    
    // Create an anonymous implementation of OnClickListener
    private OnClickListener cAddListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		// do something when button is clicked
			helloClass = (TextView)findViewById(R.id.helloClass);
			String toggleWord = (String) buttonToggle.getText();
			if (toggleWord.equals("Sleep"))
			{
				helloClass.setTextColor(Color.BLACK);
				helloClass.setText("Good Bye 21w789");
				buttonToggle.setText("Wake");
				findViewById(R.id.window).setBackgroundColor(Color.WHITE);
			}
			else
			{
				helloClass.setTextColor(Color.WHITE);
				helloClass.setText("Hello 21w789");
				buttonToggle.setText("Sleep");
				findViewById(R.id.window).setBackgroundColor(Color.BLACK);
			}

    	}
    };
}