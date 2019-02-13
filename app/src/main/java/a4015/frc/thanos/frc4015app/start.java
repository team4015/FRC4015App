package a4015.frc.thanos.frc4015app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Start page that validates and stores the team number and scout IP in memory, before moving onto the
 * MainActivity page, where the ScoutingDataPackage class is to be used to instantiated using these variables.
 * @Author Daniel Ye
 */
public class start extends AppCompatActivity {
    EditText teamNumber;
    EditText scoutIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        teamNumber = findViewById(R.id.teamNumber);
        scoutIP = findViewById(R.id.scoutIP);

    }
    /**
     * This method runs when the submit button is clicked, and checks if the the team number and
     * scout IP is valid and saves them in memory, then it starts the MainActivity class.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void submitInfo(View view) {
        if (teamNumber.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"You must enter a team number!",Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(teamNumber.getText().toString())<0) {
            Toast.makeText(getApplicationContext(),"Team number must be >= 0!",Toast.LENGTH_SHORT).show();
        } else if (scoutIP.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"You must enter a scout ID!",Toast.LENGTH_SHORT).show();
        } else {
            //Save Team Number
            SharedPreferences settings = getSharedPreferences("Team Number", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("Team Number", Integer.parseInt(teamNumber.getText().toString()));
            editor.commit();
            //Save Scout IP
            settings = getSharedPreferences("Scout IP", Context.MODE_PRIVATE);
            editor = settings.edit();
            editor.putString("Scout IP", scoutIP.getText().toString());
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
