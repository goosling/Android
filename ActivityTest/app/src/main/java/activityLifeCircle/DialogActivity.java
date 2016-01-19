package activityLifeCircle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.joe.activitytest.R;

/**
 * Created by joe on 2016/1/19.
 */
public class DialogActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout);
    }
}
