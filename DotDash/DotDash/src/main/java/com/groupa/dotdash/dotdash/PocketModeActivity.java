package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PocketModeActivity extends Activity {


    private ImageButton playButton;
    private ImageButton composeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket_mode);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        playButton = (ImageButton)findViewById(R.id.playButton);
        composeButton = (ImageButton)findViewById(R.id.composeButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message unheardMessage = DataManager.getInstance().getNextMessage();
                if (unheardMessage != null) {
                    Translator.outputMessage(view.getContext(), Translator.convertTextToMorse(unheardMessage.getContact().getName()+ " ", DotDash.wpm));
                    Translator.outputMessage(view.getContext(), Translator.convertTextToMorse(unheardMessage.getText(), DotDash.wpm));
                }
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.pocket_mode, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
