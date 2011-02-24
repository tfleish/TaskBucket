package thayse.HelloWorldApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class goodbyeworld extends Activity{
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goodbye);
        
        // Add a Buttons
    	
        Button goodbyeButton = (Button)findViewById(R.id.goodbye_button);
        goodbyeButton.setOnClickListener(new View.OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent intent = new Intent(goodbyeworld.this, helloworld.class);
        		startActivity(intent);
        	}

        });
    }
    
}
