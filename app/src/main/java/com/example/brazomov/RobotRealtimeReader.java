package com.example.brazomov;

import android.media.SoundPool;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class RobotRealtimeReader{

    private final String TCP_IP;

    private final int TCP_port;

    public RobotRealtimeReader() {
        this.TCP_IP = "127.0.0.1";
        this.TCP_port = 30003;
    }

    public RobotRealtimeReader(String IP) {
        this.TCP_IP = IP;
        this.TCP_port = 30003;
    }

    private double[] RealtimeMessage;

    public void readSocket(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket rt = new Socket(TCP_IP, TCP_port);
                    if (rt.isConnected()) {
                      Log.e("------", "Connected to UR Realtime Client");
                    }

                    DataInputStream in;
                    in = new DataInputStream(rt.getInputStream());

                    int length = in.readInt();
                    Log.e("------","Length is " + length);

                    RealtimeMessage = new double[length];

                    RealtimeMessage[0] = length;

                    int data_available = (length-4)/8;
                    Log.e("------","There are "+data_available+" doubles available");

                    int i = 1;
                    while (i <= data_available){
                        RealtimeMessage[i] = in.readDouble();
                        //Log.e("------","Index "+i+" is "+RealtimeMessage[i]);
                        i++;
                    }

                    in.close();
                    rt.close();
                    Log.e("------","Disconnected from UR Realtime Client");
                    Log.e("------", "Read!");
                }
                catch (IOException e){
                    Log.e("-----", String.valueOf(e));
                }
            }
        }).start();
    }

    private enum RTinfo {
        TCP_actual		(56, 6);
        private final int index;
        private final int count;
        RTinfo(int index, int count){
            this.index = index;
            this.count = count;
        }
        private int index() {return index;}
        private int count() {return count;}
    }

    public double[] getActualTcpPose() throws NullPointerException, InterruptedException{
        Thread.sleep(1000);
        final double[] val = new double[RTinfo.TCP_actual.count()];
        int i = 0;
        while (i < RTinfo.TCP_actual.count()){

            val[i] = RealtimeMessage[RTinfo.TCP_actual.index()+i];
            ++i;
        }
        return val;
    }

}
