package customWindows;

import com.hag.bucketlst.R;
import com.hag.bucketlst.R.id;
import com.hag.bucketlst.R.layout;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ListActivity {
	protected TextView title;
	protected ImageView icon;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        
        setContentView(R.layout.cus_win);
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        
        title = (TextView) findViewById(R.id.title);
	}
}