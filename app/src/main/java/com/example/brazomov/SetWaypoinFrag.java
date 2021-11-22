package com.example.brazomov;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.brazomov.database.DBHelper;
import com.example.brazomov.database.models.Directiva;

import java.text.DecimalFormat;

public class SetWaypoinFrag extends Fragment implements View.OnTouchListener{

    private Button btn_ok, btn_cancel;
    private ImageButton btnMoveFront, btnMoveRight, btnMoveBack, btnMoveLeft, btnGetUp, btnGetDown;
    private String showtcp = "desc", title;
    private double incremento = 0.05;
    private double aceleracion = 0.1;
    private double velocidad = 5;
    private RobotRealtimeReader realtimeReader;
    private ScriptSender scriptSender;
    private ScriptCommand scriptCommand;
    private DBHelper dbHelper;
    private Cursor cursor;
    private int index;
    private Directiva model;
    private String command_firtprefix="movej(p", command_postprefix = ", a = 10, v = 1, t = 0, r = 0)";


    public SetWaypoinFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("key1", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                index = result.getInt("index1");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_waypoin, container, false);
        dbHelper = new DBHelper(v.getContext());
        scriptCommand = new ScriptCommand();
        final Cursor cursor1 = dbHelper.getAllDataFrom("Configuracion");
        cursor = dbHelper.getAllDataFrom("Directiva");
        if(cursor1.getCount() > 0){
            cursor1.moveToFirst();
            scriptSender = new ScriptSender(cursor1.getString(1));
            realtimeReader = new RobotRealtimeReader(cursor1.getString(1));
        }
        btn_ok = (Button)v.findViewById(R.id.btn_ok_id);
        btn_cancel = (Button)v.findViewById(R.id.btn_cancel_id);
        btnMoveFront = (ImageButton)v.findViewById(R.id.btnMoveFront);
        btnMoveRight = (ImageButton)v.findViewById(R.id.btnMoveRight);
        btnMoveBack = (ImageButton)v.findViewById(R.id.btnMoveBack);
        btnMoveLeft = (ImageButton)v.findViewById(R.id.btnMoveLeft);
        btnGetUp = (ImageButton)v.findViewById(R.id.btnGetUp);
        btnGetDown = (ImageButton)v.findViewById(R.id.btnGetDown);
        sendToken();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closefrag();
            }
        });

        btnMoveFront.setOnTouchListener(this);
        btnMoveRight.setOnTouchListener(this);
        btnMoveBack.setOnTouchListener(this);
        btnMoveLeft.setOnTouchListener(this);
        btnGetUp.setOnTouchListener(this);
        btnGetDown.setOnTouchListener(this);
        return v;


    }

    private void sendToken(){
        Log.e("------", "Sending TOKEN...");
        scriptCommand.appendLine("pose = get_actual_tcp_pose()");
        scriptCommand.appendLine("movej(p[pose[0]+0.0001, pose[1], pose[2], pose[3], pose[4], pose[5]], a="+aceleracion+", v = "+velocidad+", t = 0, r = 0)");
        scriptSender.sendScriptCommand(scriptCommand);
        scriptCommand.clear();
        Log.e("------", "Robot is ready");
    }

    private void closefrag(){
        getActivity().onBackPressed();
    }

    public void updateData(){
        Log.e("-----", "Reading...");
        realtimeReader.readSocket();
        DecimalFormat df = new DecimalFormat("#.####");
        double[] tcp = new double[0];
        try {
            tcp = realtimeReader.getActualTcpPose();
            showtcp = "["+
                    df.format(tcp[0])+","+
                    df.format(tcp[1])+","+
                    df.format(tcp[2])+","+
                    df.format(tcp[3])+","+
                    df.format(tcp[4])+","+
                    df.format(tcp[5])+"]";
            cursor.moveToFirst();
            cursor.move(index);
            model = new Directiva(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            model.setCodigo(command_firtprefix + showtcp + command_postprefix);
            dbHelper.updateInto("Directiva", model.getContentValues(), index);
        } catch (InterruptedException | NullPointerException | ArrayIndexOutOfBoundsException e) {
           Toast.makeText(getContext(), "No guardado", Toast.LENGTH_SHORT).show();
        }

        closefrag();

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            if(view == btnMoveFront){
                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.click));
                Log.e(":-----:","adelante");
                scriptCommand.appendLine("pose = get_actual_tcp_pose()");
                scriptCommand.appendLine("aumento = "+incremento);
                scriptCommand.appendLine("movej(p[pose[0]+aumento, pose[1], pose[2], pose[3], pose[4], pose[5]], a="+aceleracion+", v = "+velocidad+", t = 0, r = 0)");
                scriptSender.sendScriptCommand(scriptCommand);
                scriptCommand.clear();
            }else if(view == btnMoveLeft){
                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.click));
                Log.e(":-----:","izquierda");
                scriptCommand.appendLine("pose = get_actual_tcp_pose()");
                scriptCommand.appendLine("aumento = "+incremento);
                scriptCommand.appendLine("movej(p[pose[0], pose[1]-aumento, pose[2], pose[3], pose[4], pose[5]], a="+aceleracion+", v = "+velocidad+", t = 0, r = 0)");
                scriptSender.sendScriptCommand(scriptCommand);
                scriptCommand.clear();
            }else if(view == btnMoveBack){
                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.click));
                Log.e(":-----:","atras");
                scriptCommand.appendLine("pose = get_actual_tcp_pose()");
                scriptCommand.appendLine("aumento = "+incremento);
                scriptCommand.appendLine("movej(p[pose[0]-aumento, pose[1], pose[2], pose[3], pose[4], pose[5]], a="+aceleracion+", v = "+velocidad+", t = 0, r = 0)");
                scriptSender.sendScriptCommand(scriptCommand);
                scriptCommand.clear();
            }else if(view == btnMoveRight){
                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.click));
                Log.e(":-----:","derecha");
                scriptCommand.appendLine("pose = get_actual_tcp_pose()");
                scriptCommand.appendLine("aumento = "+incremento);
                scriptCommand.appendLine("movej(p[pose[0], pose[1]+aumento, pose[2], pose[3], pose[4], pose[5]], a="+aceleracion+", v = "+velocidad+", t = 0, r = 0)");
                scriptSender.sendScriptCommand(scriptCommand);
                scriptCommand.clear();
            }else if(view == btnGetUp){
                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.click));
                Log.e(":-----:","arriba");
                scriptCommand.appendLine("pose = get_actual_tcp_pose()");
                scriptCommand.appendLine("aumento = "+incremento);
                scriptCommand.appendLine("movej(p[pose[0], pose[1], pose[2]+aumento, pose[3], pose[4], pose[5]], a="+aceleracion+", v = "+velocidad+", t = 0, r = 0)");
                scriptSender.sendScriptCommand(scriptCommand);
                scriptCommand.clear();
            }else if(view == btnGetDown){
                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.click));
                Log.e(":-----:","abajo");
                scriptCommand.appendLine("pose = get_actual_tcp_pose()");
                scriptCommand.appendLine("aumento = "+incremento);
                scriptCommand.appendLine("movej(p[pose[0], pose[1], pose[2]-aumento, pose[3], pose[4], pose[5]], a="+aceleracion+", v = "+velocidad+", t = 0, r = 0)");
                scriptSender.sendScriptCommand(scriptCommand);
                scriptCommand.clear();
            }
        }
        return true;
    }



}

