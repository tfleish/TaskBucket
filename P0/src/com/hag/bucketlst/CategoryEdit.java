package com.hag.bucketlst;

// test push from eclipse in testBranch?
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CategoryEdit extends CustomWindow {
    /** Called when the activity is first created. */
	
    private CDBAdapter db = new CDBAdapter(this);;
    EditText Category;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cedit);
		this.title.setText("Category Info");
		
    	// Create an anonymous implementation of OnClickListener
    	Button b2 = (Button) findViewById(R.id.b2);
    	b2.setOnClickListener(new OnClickListener()
    	{
    		public void onClick(View v)
    		{
		    	db.open();
		    	long id = 0;
		    	// do something when the button is clicked
		    	try
		    	{
		    		Category = (EditText)findViewById(R.id.catGet);
		    		db.insertCategory(Category.getText().toString());
		
		    		id = db.getAllEntriesCount();
		
		    		Context context = getApplicationContext();
		    		CharSequence text = "The quote '" + Category.getText() + "' was added successfully!\nQuotes Total = " + id;
		    		int duration = Toast.LENGTH_LONG;
		
		    		Toast toast = Toast.makeText(context, text, duration);
		    		toast.show();
		    		Category.setText("");
		    	}
		    	catch (Exception ex)
		    	{
		    		Context context = getApplicationContext();
		    		CharSequence text = ex.toString() + "ID = " + id;
		    		int duration = Toast.LENGTH_LONG;
		
		    		Toast toast = Toast.makeText(context, text, duration);
		    		toast.show();
		    	}
		    	db.close();
    		}
    	});
    }
}
