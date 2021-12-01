
import java.io.IOException;
import java.net.ServerSocket;

public class BankServer {
    public static void main(String[] args) throws IOException {

        ServerSocket BankServerSocket = null;
        boolean listening = true;
        String BankServerName = "BankServer";
        int BankServerNumber = 4545;

        double Client1Account = 1000;
        double Client2Account = 1000;
        double Client3Account = 1000;

        double[] Account = {Client1Account, Client2Account, Client3Account};
        //Create the shared object in the global scope...

        SharedBankState ourSharedBankStateObject = new SharedBankState(Account);



        // Make the server socket

        try {
            BankServerSocket = new ServerSocket(BankServerNumber);
        } catch (IOException e) {
            System.err.println("Could not start " + BankServerName + " specified port.");
            System.exit(-1);
        }
        System.out.println(BankServerName + " started");

        //Got to do this in the correct order with only four clients!  Can automate this...

        while (listening){
            new BankServerThread(BankServerSocket.accept(), "BankingThread1", ourSharedBankStateObject).start();
            new BankServerThread(BankServerSocket.accept(), "BankingThread2", ourSharedBankStateObject).start();
            new BankServerThread(BankServerSocket.accept(), "BankingThread3", ourSharedBankStateObject).start();
            System.out.println("New " + BankServerName + " thread started.");
        }

        BankServerSocket.close();
    }
}