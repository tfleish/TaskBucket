package thayse.HelloWorldApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class helloworld extends Activity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Add a Button
        Button helloButton = (Button)findViewById(R.id.hello_button);
        helloButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
        	Intent intent = new Intent(helloworld.this, goodbyeworld.class);
        	startActivity(intent);
        }

        });
    }
    
}
