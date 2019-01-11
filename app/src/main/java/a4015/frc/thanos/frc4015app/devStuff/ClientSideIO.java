package a4015.frc.thanos.frc4015app.devStuff;

import java.security.SecureRandom;

public interface ClientSideIO {

    /**
     * This method should send data to a ServerSideIO.
     * @param data - A byte array representing a ScoutingDataPackage. Must be from calling .packageForSending()
     *             on a ScoutingDataPackage, and encrypted using a correct key.
     */
    void sendData(byte[] data);

    /**
     * May be called to verify that the client is in sync with the server that is, the CodeGenerators for
     * both the server and client are in sync. Verifies a users key.
     * @param key - Should be a key generated from the SyncedSecurityCodeGenerator
     */
    void verifySecurity(byte[] key);

}
