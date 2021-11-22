package com.example.brazomov;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ScriptSender {
    // IP of the robot
    private final String TCP_IP;
    // Port for secondary client
    private final int TCP_port = 30003;

    public ScriptSender() {
        this.TCP_IP = "127.0.0.1";
    }

    public ScriptSender(String IP) {
        this.TCP_IP = IP;
    }

    public void sendScriptCommand(com.example.brazomov.ScriptCommand command) {
        sendToSecondary(command.toString());
    }

    // Internal method that sends script to client
    private void sendToSecondary(final String command){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    // Create a new Socket Client
                    Socket sc = new Socket(TCP_IP, TCP_port);
                    if (sc.isConnected()){
                        // Create stream for data
                        PrintWriter out;
                        out = new PrintWriter(sc.getOutputStream());

                        // Send command
                        out.write(command);
                        out.flush();

                        // Perform housekeeping
                        out.close();
                    }
                    sc.close();
                }
                catch (IOException e){
                    System.out.println(e);
                }
            }
        }).start();

    }
}
