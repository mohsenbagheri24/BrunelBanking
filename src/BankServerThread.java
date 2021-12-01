
import java.net.*;
import java.io.*;


public class BankServerThread extends Thread {


    private Socket BankingActionSocket = null;
    private SharedBankState mySharedActionStateObject;
    private String myActionServerThreadName;
    
    //Setup the thread
    public BankServerThread(Socket BankingActionSocket, String ActionServerThreadName, SharedBankState SharedObject) {

	    //super(ActionServerThreadName);
        this.BankingActionSocket = BankingActionSocket;
        mySharedActionStateObject = SharedObject;
        myActionServerThreadName = ActionServerThreadName;
    }

    public void run() {
        try {
            System.out.println(myActionServerThreadName + "initialising.");
            PrintWriter out = new PrintWriter(BankingActionSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(BankingActionSocket.getInputStream()));
            String inputLine, outputLine;

            while ((inputLine = in.readLine()) != null) {
                // Get a lock first
                try {
                    mySharedActionStateObject.acquireLock();
                    outputLine = mySharedActionStateObject.processInput(myActionServerThreadName, inputLine);
                    System.out.println(outputLine);
                    out.println(outputLine);
                    mySharedActionStateObject.releaseLock();
                }
                catch(InterruptedException e) {
                    System.err.println("Failed to get lock when reading:"+e);
                }
            }

            out.close();
            in.close();
            BankingActionSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
