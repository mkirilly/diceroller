package com.mkirilly.diceroller;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PatternMatcher;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final Pattern rollPat = Pattern.compile("(\\d*)d(\\d+)([+-]\\d+)?");
    private Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rand = new Random();
    }
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickClear(View view) {
        TextView logListing = (TextView)findViewById(R.id.LogListing);
        logListing.setText("");
    }

    public void onClickMarker(View view) {
        TextView logListing = (TextView)findViewById(R.id.LogListing);
        logListing.append("-------\n");
    }


    public void onClickRoll(View view) {
        Button button = (Button)view;
        CharSequence buttonText = button.getText();

        TextView logListing = (TextView) findViewById(R.id.LogListing);

        final Integer roll = getNextRoll(buttonText, rand);
        logListing.append(buttonText.toString() + ": "
                + ((roll != null) ? roll : "failure") + "\n");
    }

    public void onClickAddNew(View view) {
        EditText rollTypeText = (EditText) findViewById(R.id.NewRollType);
        CharSequence newRollType = rollTypeText.getText();
        Matcher m = rollPat.matcher(newRollType);
        if (m.find()){
            // correct pattern, add new button with the pattern as text if unique
            LinearLayout customRolls = (LinearLayout)findViewById(R.id.CustomRollTypes);
            boolean uniqueRollType = true;
            for(int i = 0; i < customRolls.getChildCount(); i++) {
                if (((Button)customRolls.getChildAt(i)).getText().toString().equals(newRollType.toString())) {
                    uniqueRollType = false;
                }
            }
            LinearLayout basicRollTypes = (LinearLayout)findViewById(R.id.BasicRollTypes);
            for(int i = 0; i < basicRollTypes.getChildCount(); i++) {
                if (((Button)basicRollTypes.getChildAt(i)).getText().toString().equals(newRollType.toString())) {
                    uniqueRollType = false;
                }
            }

            if (uniqueRollType) {
                Button newBut = new Button(customRolls.getContext());
                newBut.setText(newRollType);
                newBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        onClickRoll(view2);
                    }
                });
                customRolls.addView(newBut);
            }
        } else {
            // something went wrong
        }
        rollTypeText.setText("");
    }


    public static Integer getNextRoll(CharSequence buttonText, Random r) {

        Matcher m = rollPat.matcher(buttonText);
        if (m.find()) {
            String multString = m.group(1);
            String dieString = m.group(2);
            String modString = m.group(3);
            if (multString == null || multString.equals("")) { multString = "1"; }
            if (dieString == null) { dieString = "1"; }
            if (modString == null) { modString = "0"; }
            final int multiplier = Integer.parseInt(multString);
            final int dieType = Integer.parseInt(dieString);
            final int modifier = Integer.parseInt(modString);

            int total = 0;

            for (int i = 0; i < multiplier; i++) {
                total += r.nextInt(dieType)+1;
            }

            total += modifier;
            return total;
        }
        else
        {
            return null;
        }
    }
}