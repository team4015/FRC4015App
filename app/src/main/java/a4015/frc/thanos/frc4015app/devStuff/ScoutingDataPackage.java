package a4015.frc.thanos.frc4015app.devStuff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains and validates all data to be sent to servers for processing. Will self validate and ensure all data
 * inputted is admissible, and will throw exceptions when invalid data is provided.
 * @Author Gerald Lee
 */
public class ScoutingDataPackage implements Serializable {

    public static class InvalidDataException extends Exception {

        private InvalidDataException(String reason) {
            super(reason);
        }

    }

    private final int TEAM_NUMBER;
    private final String SCOUT_IP;
    private String teamName = "";
    private String robotType = "";
    private List<String> comments = new ArrayList<>();
    private int[] hatchData = {0, 0, 0}; // Level 1 hatches, 2, and 3.
    private int[] cargoData = {0, 0, 0}; // Level 1 cargo, 2, and 3.
    private int climbLevel = 1; // The highest level which the team DID climb to (at the end of the match)
    private boolean sandstormCapable = false; // Can the team effectively maneuver during the sandstorm (NOT FOR MATCHES)
    private int blockScore = 0; // Points the defender prevented opponents from scoring directly
    private int disruptTime = 0; // Time in seconds the defender wasted of the opponents time
    private int penaltyPoints = 0; // Points this team gave to the opponents from penalties
    private boolean wasDisabled = false; // Weather or not the robot was disabled
    private int disabledTime = 0; // Time when the robot was disabled
    private int[] teamMates = new int[2]; // Team numbers of other robots in the same alliance
    private int[] opponentNumbers = new int[3]; // Team numbers of opponents alliance
    private int matchNumber = -1; // The number of the match this tournament
    private int maxClimb = 1; // The highest level the team claims to be able to climb to
    private int maxHatch = 0; // The highest hatch level the team can place
    private int maxCargo = 0; // The highest cargo level the team can place
    private boolean matchDataReady = false;
    private boolean[] modified = {
            false, // [0] - hatchData
            false, // [1] - cargoData
            false, // [2] - climbLevel
            false, // [3] - blockScore
            false, // [4] - penaltyPoints
            false, // [5] - disabledTime
            false, // [6] - maxClimb
            false, // [7] - maxHatch
            false, // [8] - maxCargo
            false // [9] - sandstormCapable
    };

    /**
     * Initializes a new data package. At a minimum the two parameters must be submitted.
     * @param teamNumber - The team number of the teams who's robot is being submitted
     * @param scoutIP - The IP of the scout submitting data. Not to be provided from the scout themselves.
     */
    public ScoutingDataPackage(int teamNumber, String scoutIP) {
        TEAM_NUMBER = teamNumber;
        SCOUT_IP = scoutIP;
    }

    /**
     * Configures the data package for a match. Several parameters are required for this. Some data will be refused
     * if this method has not been successfully called. This method may only be successfully called once per package.
     * @param teamMates - An array containing the team numbers of the other two robots on the same alliance.
     * @param opponentNumbers - An array containing the team numbers of the opposing alliance.
     * @param matchNumber - An integer N representing the Nth match of the tournament.
     * @throws InvalidDataException Thrown under the following conditions:
     *
     * - The method has been successfully called previously.
     * - The match number is less than 1.
     * - Any of the submitted team numbers are less than 0.
     * - The array of team mates contains some number of elements not equal to 2.
     * - The array of opponents contains some number of elements not equal to 3.
     */
    public void configureMatch(int[] teamMates, int[] opponentNumbers, int matchNumber) throws InvalidDataException {
        if (matchDataReady) {
            throw new InvalidDataException("Match is already configured!");
        }
        int last = -1;
        for (int test : teamMates) {
            if (test < 0) {
                throw new InvalidDataException("Team number cannot be negative!");
            }
            else if (test == last) {
                throw new InvalidDataException("Team numbers must be distinct!");
            }
            last = test;
        }
        int lastB = -1;
        boolean first = true;
        for (int test : opponentNumbers) {
            if (test < 0) {
                throw new InvalidDataException("Opponent team number cannot be negative!");
            }
            else if (test == last || test == lastB) {
                throw new InvalidDataException("Opponent team numbers must be distinct!");
            }
            if (first) {
                last = test;
            }
            else {
                lastB = test;
            }
        }
        if (matchNumber <= 0) {
            throw new InvalidDataException("Match number must be > 0!");
        }
        this.teamMates = teamMates;
        this.opponentNumbers = opponentNumbers;
        this.matchNumber = matchNumber;
        matchDataReady = true;
    }

    /**
     * Sets the name of the team. Not a mandatory field.
     * @param teamName - A string representing the name of the team.
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * Sets the number of penalty points given to the opposing alliance. Requires the match to have been configured.
     * @param penaltyPoints - Points given to the opposing alliance as a result of this teams actions.
     * @throws InvalidDataException - Thrown when penalty points are  a negative value.
     */
    public void setPenaltyPoints(int penaltyPoints) throws InvalidDataException {
        if (!matchDataReady) {
            throw new InvalidDataException("Match data must be configured first!");
        }
        if (penaltyPoints < 0) {
            throw new InvalidDataException("Penalty points must be >= 0!");
        }
        this.penaltyPoints = penaltyPoints;
        modified[4] = true;
    }

    /**
     * Sets the number of hatches scored in a given match at each level. Requires the match to have been configured.
     * @param hatchData - An array of integers in the form A, B, C. A represents the number of hatches scored at level 1.
     *                  B represents level 2, and C level 3.
     * @throws InvalidDataException - If either the array given does not contain exactly 3 elements, or any elements which
     * are less than 0.
     */
    public void setHatchData(int[] hatchData) throws InvalidDataException {
        if (!matchDataReady) {
            throw new InvalidDataException("Match must be configured first!");
        }
        if (hatchData.length != 3) {
            throw new InvalidDataException("Hatch data must have exactly 3 elements!");
        }
        for (int test : hatchData) {
            if (test < 0) {
                throw new InvalidDataException("No element of hatch data may be < 0!");
            }
        }
        this.hatchData = hatchData;
        modified[0] = true;
    }

    /**
     * Sets the number of cargo balls scored in a given match at each level. Requires the match to have been configured.
     * @param cargoData - An array of integers in the form A, B, C. A represents the amount of cargo scored at level 1.
     *                  B represents level 2, and C level 3.
     * @throws InvalidDataException - If either the array given does not contain exactly 3 elements, or any elements which
     * are less than 0.
     */
    public void setCargoData(int[] cargoData) throws InvalidDataException {
        if (!matchDataReady) {
            throw new InvalidDataException("Match must be configured first!");
        }
        if (cargoData.length != 3) {
            throw new InvalidDataException("Hatch data must have exactly 3 elements!");
        }
        for (int test : cargoData) {
            if (test < 0) {
                throw new InvalidDataException("No element of hatch data may be < 0!");
            }
        }
        this.cargoData = cargoData;
        modified[1] = true;
    }

    /**
     * Sets the level to which the robot being scouted climbed to in a match (not a theoretical highest).
     * @param climbLevel - An integer value representing the level to which the robot climbed to.
     * @throws InvalidDataException - If the level given is not exactly equal to 1, 2, or 3.
     */
    public void setClimbLevel(int climbLevel) throws InvalidDataException {
        if (!matchDataReady) {
            throw new InvalidDataException("Match must be configured first!");
        }
        if (climbLevel == 1 || climbLevel == 2 || climbLevel == 3) {
            this.climbLevel = climbLevel;
            modified[2] = true;
        }
        else {
            throw new InvalidDataException("Climb Level must be either 1, 2, or 3!");
        }
    }

    /**
     * Sets the theoretical capability of  a robot to operate either autonomously or blindly during the sandstorm period.
     * @param sandstormCapable - A boolean value representing the ability of a robot to operate in the sandstorm period.
     */
    public void setSandstormCapable(boolean sandstormCapable) {
        this.sandstormCapable = sandstormCapable;
        modified[9] = true;
    }

    /**
     * Sets a theoretical maximum level which the robot can place cargo balls.
     * @param maxCargo - An integer representing the theoretical highest level which the robot can place cargo balls.
     * @throws InvalidDataException - Thrown when a value which is not exactly 0, 1, 2, or 3 is provided.
     */
    public void setMaxCargo(int maxCargo) throws InvalidDataException {
        if (maxCargo == 0 || maxCargo == 1 || maxCargo == 2 || maxCargo == 3) {
            this.maxCargo = maxCargo;
            modified[8] = true;
        }
        else {
            throw new InvalidDataException("Cargo max Level must be either 0, 1, 2, or 3!");
        }
    }

    /**
     * Sets a theoretical maximum level which the robot can place hatches.
     * @param maxHatch - An integer representing the theoretical highest level which the robot can place hatches.
     * @throws InvalidDataException - Thrown when a value which is not exactly 0, 1, 2, or 3 is provided.
     */
    public void setMaxHatch(int maxHatch) throws InvalidDataException {
        if (maxHatch == 0 || maxHatch == 1 || maxHatch == 2 || maxHatch == 3) {
            this.maxHatch = maxHatch;
            modified[7] = true;
        }
        else {
            throw new InvalidDataException("Hatch max Level must be either 0, 1, 2, or 3!");
        }
    }

    /**
     * Sets a theoretical maximum level which the robot can climb.
     * @param maxClimb - An integer representing the theoretical highest level which the robot can climb.
     * @throws InvalidDataException - Thrown when a value which is not exactly 1, 2, or 3 is provided.
     */
    public void setMaxClimb(int maxClimb) throws InvalidDataException {
        if (maxClimb == 1 || maxClimb == 2 || maxClimb == 3) {
            this.maxClimb = maxClimb;
            modified[6] = true;
        }
        else {
            throw new InvalidDataException("Hatch max Level must be either 1, 2, or 3!");
        }
    }

    /**
     * Sets an estimate of the number of points a bot prevented the opposing to from scoring.
     * @param blockScore - An integer representing the estimated number of points a robot prevented opponents from scoring.
     * @throws InvalidDataException - When a values less than 0 is provided.
     */
    public void setBlockScore(int blockScore) throws InvalidDataException {
        if (!matchDataReady) {
            throw new InvalidDataException("Match data must be configured first!");
        }
        if (blockScore < 0) {
            throw new InvalidDataException("Block score must be >= 0!");
        }
        this.blockScore = blockScore;
        modified[3] = true;
    }

    /**
     * Sets the time at which a robot was disabled. If set to 0, assumes that the bot was not disabled.
     * @param disabledTime - An integer representing time in seconds when the robot was disabled.
     * @throws InvalidDataException - Thrown when a negative input is provided.
     */
    public void setDisabledTime(int disabledTime) throws InvalidDataException {
        if (!matchDataReady) {
            throw new InvalidDataException("Match data must be configured first!");
        }
        if (disabledTime < 0) {
            throw new InvalidDataException("Disabled time must be greater than or equal to 0");
        }
        if (disabledTime > 0) {
            wasDisabled = true;
        }
        else {
            wasDisabled = false;
        }
        this.disabledTime = disabledTime;
        modified[5] = true;
    }

    /**
     * Sets the time in seconds during which the robot disrupted the motion or pinned other opposing robots.
     * @param disruptTime - An integer representing the duration is seconds for which opposing robots were blocked.
     * @throws InvalidDataException - Thrown when a negative input is provided.
     */
    public void setDisruptTime(int disruptTime) throws InvalidDataException {
        if (!matchDataReady) {
            throw new InvalidDataException("Match data must be configured first!");
        }
        if (disruptTime < 0) {
            throw new InvalidDataException("Disruption time must be greater than or equal to 0");
        }
        this.disruptTime = disruptTime;
    }

    /**
     * Adds a comment or note about the specific match or robot.
     * @param comment - A string comprised of the comment.
     */
    public void addComment(String comment) {
        this.comments.add(comment);
    }

    /**
     * Sets a string description of the robot type.
     * @param type - A string representing the type of robot.
     */
    public void setRobotType(String type) {
        this.robotType = type;
    }

    public int getTeamNumber() { return TEAM_NUMBER; }

    public String getScoutIP() { return SCOUT_IP; }

    public boolean isSandstormCapable() {
        return sandstormCapable;
    }

    public boolean isWasDisabled() {
        return wasDisabled;
    }

    public int getBlockScore() {
        return blockScore;
    }

    public int getClimbLevel() {
        return climbLevel;
    }

    public int getDisabledTime() {
        return disabledTime;
    }

    public int getDisruptTime() {
        return disruptTime;
    }

    public int getPenaltyPoints() {
        return penaltyPoints;
    }

    public int[] getCargoData() {
        return cargoData;
    }

    public int[] getHatchData() {
        return hatchData;
    }

    public String getRobotType() {
        return robotType;
    }

    public List<String> getComments() {
        return comments;
    }

    public int[] getOpponentNumbers() {
        return opponentNumbers;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public int[] getTeamMates() {
        return teamMates;
    }

    public String getTeamName() {
        return teamName;
    }

    public boolean[] getModified() {
        return modified;
    }

    public int getMaxClimb() {
        return maxClimb;
    }

    public int getMaxCargo() {
        return maxCargo;
    }

    public int getMaxHatch() {
        return maxHatch;
    }

    /**
     * To be used to package this object into a byte array to be sent to the server/laptop for processing
     * @return - A byte array representing this object.
     * @throws IOException - Thrown when this object fails to serialize.
     */
    public byte[] packageForSending() throws IOException {
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(outByteStream);
        outStream.writeObject(this);
        return outByteStream.toByteArray();
    }

}
