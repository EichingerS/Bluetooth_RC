package sebastian.bluetooth_rc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class einstellungen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einstellungen);
        String count="7";
        TextView tv = (TextView) findViewById(R.id.my_text_view);
        tv.setText(count);
    }
    public void ButtonOnClicked(View v)
    {
        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), steuerung.class
        ));
    }
    public void Button_plus_OnClicked(View v)
    {
        Button button = (Button) v;
        TextView tv = (TextView) findViewById(R.id.my_text_view);
        String text = tv.getText().toString();
        int text_int = Integer.parseInt(text);
        text_int++;
        tv.setText(""+text_int);

    }
    public void Button_minus_OnClicked(View v)
    {
        Button button = (Button) v;
        TextView tv = (TextView) findViewById(R.id.my_text_view);
        String text = tv.getText().toString();
        int text_int = Integer.parseInt(text);
        text_int--;
        tv.setText(""+text_int);

    }


}
