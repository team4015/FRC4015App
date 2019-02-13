package a4015.frc.thanos.frc4015app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;

import a4015.frc.thanos.frc4015app.devStuff.ScoutingDataPackage;
/**
 * Uses the ScoutingDataPackage class to validate and store all data that the user inputs through the text boxes
 * so that they can be sent to servers for processing.
 * @Author Daniel Ye
 */
public class MainActivity extends AppCompatActivity {
    private LinearLayout configureLayout;
    private LinearLayout restartLayout;
    private ConstraintLayout inputLayout;
    private ScoutingDataPackage dataPackage;
    private ScrollView scrollLayout;
    int[] teamMates = new int[2];
    int[] opponentNumbers = new int[3];
    int matchNumber;
    private EditText teamMate1;
    private EditText teamMate2;
    private EditText opponent1;
    private EditText opponent2;
    private EditText opponent3;
    private EditText matchNumberInput;
    private EditText levelOneHatchesInput;
    private int levelOneHatches = 0;
    private EditText levelTwoHatchesInput;
    private int levelTwoHatches = 0;
    private EditText levelThreeHatchesInput;
    private int levelThreeHatches = 0;
    private EditText levelOneCargoInput;
    private int levelOneCargo = 0;
    private EditText levelTwoCargoInput;
    private int levelTwoCargo = 0;
    private EditText levelThreeCargoInput;
    private int levelThreeCargo = 0;
    private EditText teamName;
    private EditText penaltyPoints;
    private EditText blockScore;
    private EditText disabledTime;
    private EditText disruptTime;
    private EditText comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollLayout = findViewById(R.id.scrollLayout);
        configureLayout = findViewById(R.id.configureLayout);
        configureLayout.setVisibility(View.VISIBLE);
        inputLayout = findViewById(R.id.inputLayout);
        inputLayout.setVisibility(View.INVISIBLE);
        restartLayout = findViewById(R.id.restartLayout);
        restartLayout.setVisibility(View.INVISIBLE);
        teamMate1 = findViewById(R.id.teamMate1);
        teamMate2 = findViewById(R.id.teamMate2);
        opponent1 = findViewById(R.id.opponent1);
        opponent2 = findViewById(R.id.opponent2);
        opponent3 = findViewById(R.id.opponent3);
        matchNumberInput = findViewById(R.id.matchNumber);
        levelOneHatchesInput = findViewById(R.id.levelOneHatches);
        levelTwoHatchesInput = findViewById(R.id.levelTwoHatches);
        levelThreeHatchesInput = findViewById(R.id.levelThreeHatches);
        levelOneCargoInput = findViewById(R.id.levelOneCargo);
        levelTwoCargoInput = findViewById(R.id.levelTwoCargo);
        levelThreeCargoInput = findViewById(R.id.levelThreeCargo);
        teamName = findViewById(R.id.teamName);
        penaltyPoints = findViewById(R.id.penaltyPoints);
        blockScore = findViewById(R.id.blockScore);
        disabledTime = findViewById(R.id.disabledTime);
        disruptTime = findViewById(R.id.disruptTime);
        comments = findViewById(R.id.comments);
        //get Team Number and Scout IP and create ScoutingDataPackage object
        SharedPreferences settings = getSharedPreferences("Team Number", Context.MODE_PRIVATE);
        int teamNumber = settings.getInt("Team Number", 0);
        settings = getSharedPreferences("Scout IP", Context.MODE_PRIVATE);
        String scoutIP = settings.getString("Scout IP", "defaultIP");
        setTitle("Team: "+teamNumber+" \t IP: "+scoutIP);
        dataPackage = new ScoutingDataPackage(teamNumber,scoutIP);
    }
    /**
     * This method runs when the submit button is clicked, and submits the teammate numbers,
     * opponent numbers and match number into the ScoutingDataPackage object if they are valid.
     * @param View - a View object that needs to be passed for the onClick method to work when the button is pressed
     */
    public void configureMatch(View view) {
        if (teamMate1.getText().toString().equals("") || teamMate2.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"You must have 2 teammates!",Toast.LENGTH_SHORT).show();
        } else {
            teamMates[0] = Integer.parseInt(teamMate1.getText().toString());
            teamMates[1] = Integer.parseInt(teamMate2.getText().toString());
        }
        if (opponent1.getText().toString().equals("") || opponent2.getText().toString().equals("") || opponent3.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"You must have 3 opponents!",Toast.LENGTH_SHORT).show();
        } else {
            opponentNumbers[0] = Integer.parseInt(opponent1.getText().toString());
            opponentNumbers[1] = Integer.parseInt(opponent2.getText().toString());
            opponentNumbers[2] = Integer.parseInt(opponent3.getText().toString());
        }
        if (matchNumberInput.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"You must enter a match number!",Toast.LENGTH_SHORT).show();
        } else {
            matchNumber = Integer.parseInt(matchNumberInput.getText().toString());
        }
        try {
            dataPackage.configureMatch(teamMates, opponentNumbers, matchNumber);
            configureLayout.setVisibility(View.INVISIBLE);
            inputLayout.setVisibility(View.VISIBLE);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            configureLayout.setVisibility(View.VISIBLE);
            inputLayout.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * Increases the number of Penalty Points by 5 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void increasePenaltyPoints(View view) {
        try {
            if (penaltyPoints.getText().toString().equals("")) {
                dataPackage.setPenaltyPoints(5);
            } else {
                dataPackage.setPenaltyPoints(Integer.parseInt(penaltyPoints.getText().toString())+5);
            }
            penaltyPoints.setText(dataPackage.getPenaltyPoints()+ "");
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Decreases the number of Penalty Points by 5 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void decreasePenaltyPoints(View view) {
        try {
            if (penaltyPoints.getText().toString().equals("")) {
                dataPackage.setPenaltyPoints(0);
            } else {
                dataPackage.setPenaltyPoints(Integer.parseInt(penaltyPoints.getText().toString()));
                if (dataPackage.getPenaltyPoints() < 5) {
                    dataPackage.setPenaltyPoints(0);
                } else {
                    dataPackage.setPenaltyPoints(dataPackage.getPenaltyPoints()-5);
                }
            }
            penaltyPoints.setText(dataPackage.getPenaltyPoints()+ "");
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Increases the number of Level One Hatches Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void increaseLevelOneHatches(View view) {
        if (levelOneHatchesInput.getText().toString().equals("")) {
            levelOneHatches = 1;
        } else {
            levelOneHatches = Integer.parseInt(levelOneHatchesInput.getText().toString());
            levelOneHatches++;
        }
        levelOneHatchesInput.setText(levelOneHatches+"");
    }
    /**
     * Decreases the number of Level One Hatches Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void decreaseLevelOneHatches(View view) {
        if (levelOneHatchesInput.getText().toString().equals("")) {
            levelOneHatches = 0;
        } else {
            levelOneHatches = Integer.parseInt(levelOneHatchesInput.getText().toString());
            if (levelOneHatches <= 1) {
                levelOneHatches = 0;
            } else {
                levelOneHatches--;
            }
        }
        levelOneHatchesInput.setText(levelOneHatches+"");
    }
    /**
     * Increases the number of Level Two Hatches Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void increaseLevelTwoHatches(View view) {
        if (levelTwoHatchesInput.getText().toString().equals("")) {
            levelTwoHatches = 1;
        } else {
            levelTwoHatches = Integer.parseInt(levelTwoHatchesInput.getText().toString());
            levelTwoHatches++;
        }
        levelTwoHatchesInput.setText(levelTwoHatches+"");
    }
    /**
     * Decreases the number of Level Two Hatches Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void decreaseLevelTwoHatches(View view) {
        if (levelTwoHatchesInput.getText().toString().equals("")) {
            levelTwoHatches = 0;
        } else {
            levelTwoHatches = Integer.parseInt(levelTwoHatchesInput.getText().toString());
            if (levelTwoHatches <= 1) {
                levelTwoHatches = 0;
            } else {
                levelTwoHatches--;
            }
        }
        levelTwoHatchesInput.setText(levelTwoHatches+"");
    }
    /**
     * Increases the number of Level Three Hatches Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void increaseLevelThreeHatches(View view) {
        if (levelThreeHatchesInput.getText().toString().equals("")) {
            levelThreeHatches = 1;
        } else {
            levelThreeHatches = Integer.parseInt(levelThreeHatchesInput.getText().toString());
            levelThreeHatches++;
        }
        levelThreeHatchesInput.setText(levelThreeHatches+"");
    }
    /**
     * Decreases the number of Level Three Hatches Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void decreaseLevelThreeHatches(View view) {
        if (levelThreeHatchesInput.getText().toString().equals("")) {
            levelThreeHatches = 0;
        } else {
            levelThreeHatches = Integer.parseInt(levelThreeHatchesInput.getText().toString());
            if (levelThreeHatches <= 1) {
                levelThreeHatches = 0;
            } else {
                levelThreeHatches--;
            }
        }
        levelThreeHatchesInput.setText(levelThreeHatches+"");
    }
    /**
     * Changes max hatch level to 0 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxHatchLevelZero(View view) {
        try {
            dataPackage.setMaxHatch(0);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes max hatch level to 1 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxHatchLevelOne(View view) {
        try {
            dataPackage.setMaxHatch(1);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes max hatch level to 2 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxHatchLevelTwo(View view) {
        try {
            dataPackage.setMaxHatch(2);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes max hatch level to 3 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxHatchLevelThree(View view) {
        try {
            dataPackage.setMaxHatch(3);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Increases the number of Level One Cargo Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void increaseLevelOneCargo(View view) {
        if (levelOneCargoInput.getText().toString().equals("")) {
            levelOneCargo = 1;
        } else {
            levelOneCargo = Integer.parseInt(levelOneCargoInput.getText().toString());
            levelOneCargo++;
        }
        levelOneCargoInput.setText(levelOneCargo+"");
    }
    /**
     * Decreases the number of Level One Cargo Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void decreaseLevelOneCargo(View view) {
        if (levelOneCargoInput.getText().toString().equals("")) {
            levelOneCargo = 0;
        } else {
            levelOneCargo = Integer.parseInt(levelOneCargoInput.getText().toString());
            if (levelOneCargo <= 1) {
                levelOneCargo = 0;
            } else {
                levelOneCargo--;
            }
        }
        levelOneCargoInput.setText(levelOneCargo+"");
    }
    /**
     * Increases the number of Level Two Cargo Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void increaseLevelTwoCargo(View view) {
        if (levelTwoCargoInput.getText().toString().equals("")) {
            levelTwoCargo = 1;
        } else {
            levelTwoCargo = Integer.parseInt(levelTwoCargoInput.getText().toString());
            levelTwoCargo++;
        }
        levelTwoCargoInput.setText(levelTwoCargo+"");
    }
    /**
     * Decreases the number of Level Two Cargo Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void decreaseLevelTwoCargo(View view) {
        if (levelTwoCargoInput.getText().toString().equals("")) {
            levelTwoCargo = 0;
        } else {
            levelTwoCargo = Integer.parseInt(levelTwoCargoInput.getText().toString());
            if (levelTwoCargo <= 1) {
                levelTwoCargo = 0;
            } else {
                levelTwoCargo--;
            }
        }
        levelTwoCargoInput.setText(levelTwoCargo+"");
    }
    /**
     * Increases the number of Level Three Cargo Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void increaseLevelThreeCargo(View view) {
        if (levelThreeCargoInput.getText().toString().equals("")) {
            levelThreeCargo = 1;
        } else {
            levelThreeCargo = Integer.parseInt(levelThreeCargoInput.getText().toString());
            levelThreeCargo++;
        }
        levelThreeCargoInput.setText(levelThreeCargo+"");
    }
    /**
     * Decreases the number of Level Three Cargo Complete by 1 when the associated button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void decreaseLevelThreeCargo(View view) {
        if (levelThreeCargoInput.getText().toString().equals("")) {
            levelThreeCargo = 0;
        } else {
            levelThreeCargo = Integer.parseInt(levelThreeCargoInput.getText().toString());
            if (levelThreeCargo <= 1) {
                levelThreeCargo = 0;
            } else {
                levelThreeCargo--;
            }
        }
        levelThreeCargoInput.setText(levelThreeCargo+"");
    }
    /**
     * Changes max cargo level to 0 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxCargoLevelZero(View view) {
        try {
            dataPackage.setMaxCargo(0);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes max cargo level to 1 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxCargoLevelOne(View view) {
        try {
            dataPackage.setMaxCargo(1);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes max cargo level to 2 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxCargoLevelTwo(View view) {
        try {
            dataPackage.setMaxCargo(2);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes max cargo level to 3 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxCargoLevelThree(View view) {
        try {
            dataPackage.setMaxCargo(3);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes climb level to 1 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void climbLevelOne(View view) {
        try {
            dataPackage.setClimbLevel(1);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes climb level to 2 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void climbLevelTwo(View view) {
        try {
            dataPackage.setClimbLevel(2);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes climb level to 3 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void climbLevelThree(View view) {
        try {
            dataPackage.setClimbLevel(3);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes max climb level to 1 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxClimbLevelOne(View view) {
        try {
            dataPackage.setMaxClimb(1);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes max climb level to 2 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxClimbLevelTwo(View view) {
        try {
            dataPackage.setMaxClimb(2);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Changes max climb level to 3 when the button is clicked.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void maxClimbLevelThree(View view) {
        try {
            dataPackage.setMaxClimb(3);
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Switch that toggles sandstorm capable variable.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void sandstormToggle(View view) {
        dataPackage.setSandstormCapable(!dataPackage.isSandstormCapable());
    }
    /**
     * Submits the data, storing any unstored variables(hatches, cargo, and manual inputs) into the ScoutingDataPackage object.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void submit(View view) {
        try {
            if (levelOneHatchesInput.getText().toString().equals("")) {
                levelOneHatches = 0;
            } else {
                levelOneHatches = Integer.parseInt(levelOneHatchesInput.getText().toString());
            }
            if (levelTwoHatchesInput.getText().toString().equals("")) {
                levelTwoHatches = 0;
            } else {
                levelTwoHatches = Integer.parseInt(levelTwoHatchesInput.getText().toString());
            }
            if (levelThreeHatchesInput.getText().toString().equals("")) {
                levelThreeHatches = 0;
            } else {
                levelThreeHatches = Integer.parseInt(levelThreeHatchesInput.getText().toString());
            }
            int[] hatches = {levelOneHatches,levelTwoHatches,levelThreeHatches};
            dataPackage.setHatchData(hatches);
            if (levelOneCargoInput.getText().toString().equals("")) {
                levelOneCargo = 0;
            } else {
                levelOneCargo = Integer.parseInt(levelOneCargoInput.getText().toString());
            }
            if (levelTwoCargoInput.getText().toString().equals("")) {
                levelTwoCargo = 0;
            } else {
                levelTwoCargo = Integer.parseInt(levelTwoCargoInput.getText().toString());
            }
            if (levelThreeCargoInput.getText().toString().equals("")) {
                levelThreeCargo = 0;
            } else {
                levelThreeCargo = Integer.parseInt(levelThreeCargoInput.getText().toString());
            }
            int[] cargo = {levelOneCargo,levelTwoCargo,levelThreeCargo};
            dataPackage.setCargoData(cargo);
            dataPackage.setTeamName(teamName.getText().toString());
            if (penaltyPoints.getText().toString().equals("")) {
                dataPackage.setPenaltyPoints(0);
            } else {
                dataPackage.setPenaltyPoints(Integer.parseInt(penaltyPoints.getText().toString()));
            }
            if (blockScore.getText().toString().equals("")) {
                dataPackage.setBlockScore(0);
            } else {
                dataPackage.setBlockScore(Integer.parseInt(blockScore.getText().toString()));
            }
            if (disruptTime.getText().toString().equals("")) {
                dataPackage.setDisruptTime(0);
            } else {
                dataPackage.setDisruptTime(Integer.parseInt(disruptTime.getText().toString()));
            }
            if (disabledTime.getText().toString().equals("")) {
                dataPackage.setDisruptTime(0);
            } else {
                dataPackage.setDisabledTime(Integer.parseInt(disabledTime.getText().toString()));
            }
            dataPackage.addComment(comments.getText().toString());
            try {
                dataPackage.packageForSending();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        } catch (ScoutingDataPackage.InvalidDataException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        inputLayout.setVisibility(View.INVISIBLE);
        restartLayout.setVisibility(View.VISIBLE);
        scrollLayout.fullScroll(ScrollView.FOCUS_UP);
    }
    /**
     * Returns to the starting page of the scouting app to scout another team.
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void restart(View view) {
        startActivity(new Intent(getApplicationContext(), start.class));
    }
    /**
     * Disables the user from using the return button in the navigation bar to return to the start screen.
     * @param KeyEvent - a KeyEvent object that reads the actions of the user.
     */    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
