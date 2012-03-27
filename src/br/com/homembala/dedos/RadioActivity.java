package br.com.homembala.dedos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class RadioActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.start);
        (findViewById(R.id.button1)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(RadioActivity.this, Choice.class);
			     startActivity(intent);
			}});
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.exit(0);
		}
		return false;

	}
}