package fragmentTest;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.joe.activitytest.R;

/**
 * Created by JOE on 2016/1/25.
 */
public class FragmentActivity extends Activity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.button:
                        AnotherRightFragment fragment = new AnotherRightFragment();
                        FragmentManager manager = getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.right_layout, fragment);
                        transaction.commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
