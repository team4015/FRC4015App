package a4015.frc.thanos.frc4015app.devStuff;

public interface ServerSideIO {

    /**
     * This method is to handle incoming byte data from a ClientSideIO, and assemble ScoutingDataPackages.
     * @param data - A byte array representing a ScoutingDataPackage.
     * @return - A ScoutingDataPackage which was represented by the data.
     */
    ScoutingDataPackage handleData(byte[] data);

    /**
     * Sends a boolean value to an ip.
     * @param valid - A boolean which is true if the ip had provided a valid key in the past minute.
     * @param ip - The ip to which this information is being sent (should be the ip of the user who sent the information
     */
    void validateSecurityKey(boolean valid, String ip);

}
