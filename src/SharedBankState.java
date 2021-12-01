public class SharedBankState {

    private double Client1Account, Client2Account, Client3Account;
    private boolean accessing = false; 						// true a thread has a lock, false otherwise
    private String[] outputs = new String[4];

   
    
// Constructor
    SharedBankState(double[] transaction) {
        Client1Account = transaction[0];
        Client2Account = transaction[1];
        Client3Account = transaction[2];
    }

    
    
//Attempt to aquire a lock
    public synchronized void acquireLock() throws InterruptedException {
        Thread me = Thread.currentThread(); // get a ref to the current thread
        System.out.println(me.getName() + " is attempting to acquire a lock!");
        while (accessing) {  // while someone else is accessing or threadsWaiting > 0
            System.out.println(me.getName() + " waiting to get a lock as someone else is accessing...");
            //wait for the lock to be released - see releaseLock() below
            wait();
        }
        accessing = true;
        System.out.println(me.getName() + " got a lock!");
    }
    

    // Releases a lock to when a thread is finished
    public synchronized void releaseLock() {
        //release the lock and tell everyone
        accessing = false;
        notifyAll();
    }


    
    /* The processInput method */
    public synchronized String processInput(String myThreadName, String theInput) {
        System.out.println("Thread " + myThreadName + " wants to do action " + theInput);
        String theOutput = "Something went wrong";
        int transfer = 50;
        
        
        //Deposit 
        if (theInput.equals("1")) {
            //Correct request
            if (myThreadName.equals("BankingThread1")) {
            	
                Client1Account = Client1Account + 130;
                System.out.println("Client " + myThreadName + " has added 130 units ");
                outputs[0] = "Client " + myThreadName + "'s account balance is updated = " + Client1Account;
                theOutput = "Client " + myThreadName + "'s account balance is updated = " + Client1Account;
            } else if (myThreadName.equals("BankingThread2")) {

                Client2Account = Client2Account + 230;
                System.out.println("Client " + myThreadName + " has added 230 units " + Client2Account);
                outputs[0] = "Client " + myThreadName + "'s account balance is updated = " + Client2Account;

            } else if (myThreadName.equals("BankingThread3")) {

                Client3Account = Client3Account + 130;
                System.out.println("Client " + myThreadName + " has added 130 units " + Client3Account);
                outputs[0] = "Client " + myThreadName + "'s account balance is updated = " + Client3Account;

            }  else {
                System.out.println("Error - thread call not recognised.");
            }
        } else { //incorrect request
            outputs[0] = myThreadName + " received incorrect request - only understand \"Do my action!\"";

        }
        
        
//Withdraw 
        if (theInput.equals("2")) {
            if (myThreadName.equals("BankingThread1")) {

                Client1Account = Client1Account - 30;
                System.out.println("Client " + myThreadName + " has withdrew 30 units " + Client1Account);
                outputs[1] = "Client " + myThreadName + "'s account balance is updated = " + Client1Account;

            } else if (myThreadName.equals("BankingThread2")) {

                Client2Account = Client2Account - 30;
                System.out.println("Client " + myThreadName + " has withdrew 30 units " + Client2Account);
                outputs[1] = "Client " + myThreadName + "'s account balance is updated = " + Client2Account;

            } else if (myThreadName.equals("BankingThread3")) {

                Client3Account = Client3Account - 60;
                System.out.println("Client" + myThreadName + " has withdrew 60 units " + Client3Account);
                outputs[1] = "Client " + myThreadName + "'s account balance is updated = " + Client3Account;

            }  else {
                System.out.println("Error - thread call not recognised.");
            }
        }
        
        
//Transfer 
        if (theInput.equals("3")) {
            if (myThreadName.equals("BankingThread1")) {
                    System.out.println("client 1 initial balance -> "+ Client1Account);
                    System.out.println("client 3 initial balance -> "+ Client3Account);
                    Client1Account -= transfer;
                    Client3Account += transfer;
                    System.out.println("client 1 final balance after transfer -> "+ Client1Account);
                    System.out.println("client 3 final balance after transfer -> "+ Client3Account);
                    outputs[2]= "";

                    outputs[0] = "Client " + myThreadName + "'s account balance is updated = " + Client1Account;
                    outputs[1] = "Client 3 now has " + Client3Account;
            }

            if (myThreadName.equals("BankingThread2")) {
                Client3Account = Client3Account - transfer;
                Client1Account = Client1Account + transfer;
                outputs[1] = "Client " + myThreadName + "'s account balance is updated = " + Client3Account;
                outputs[0] = "Client 1 now has " + Client1Account;
                theOutput="i transferred";

            }
            if (myThreadName.equals("BankingThread3")) {

                    Client3Account = Client3Account - transfer;
                    Client1Account = Client1Account + transfer;
                    outputs[2] = "Client " + myThreadName + "'s account balance is updated = " + Client3Account;
                    outputs[0] = "Client 1 now has " + Client1Account;
        }
        }
        
        
//Check Balance
        if(theInput.equals("4")){
            if(myThreadName.equals("BankingThread1")){
                System.out.println("Client " + myThreadName + "'s account balance is  = " + Client1Account);
                outputs[3] = myThreadName + ": Your balance is " + Client1Account;
            }
            else if(myThreadName.equals("BankingThread2")){
                outputs[3] = myThreadName + ": Your balance is " + Client2Account;
            }
            else if(myThreadName.equals("BankingThread3")){
                outputs[3] = myThreadName + ": Your balance is " + Client3Account;
            }
                  }

 //Exit       
        if(theInput.equals("5")){
            System.exit(0);
        }
        //Return the output message to the BankingActionServer

        if(theInput.equals("1")){ return outputs[0];}
        if(theInput.equals("2")){ return outputs[1];}
        if(theInput.equals("3")){ return outputs[2];}
        if(theInput.equals("4")){ return outputs[3];}

        return theOutput;
    }
}