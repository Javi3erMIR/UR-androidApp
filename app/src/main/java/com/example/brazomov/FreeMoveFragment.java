package com.example.brazomov;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.brazomov.database.DBHelper;
import com.example.brazomov.database.models.Configuracion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FreeMoveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FreeMoveFragment extends Fragment implements View.OnTouchListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean expanded = false;
    private ImageButton btnMoveFront, btnMoveRight, btnMoveBack, btnMoveLeft;
    private ImageButton btnGetUp, btnGetDown;
    private Handler handler;
    private String buttonName;
    private double incremento = 0.01;
    private double aceleracion = 2;
    private double velocidad = 1.5;
    private DBHelper dbHelper;

    private ScriptSender scriptSender;
    private ScriptCommand scriptCommand;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FreeMoveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FreeMoveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FreeMoveFragment newInstance(String param1, String param2) {
        FreeMoveFragment fragment = new FreeMoveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_free_move, container, false);
        dbHelper = new DBHelper(getContext());
        Cursor cursor = dbHelper.getAllDataFrom("Configuracion");
        cursor.moveToFirst();
        if(cursor.getCount() > 0 && cursor.moveToFirst()){
            scriptSender = new ScriptSender(cursor.getString(1));
        }
        scriptCommand = new ScriptCommand();

        btnMoveFront = (ImageButton)v.findViewById(R.id.btnMoveFront);
        btnMoveRight = (ImageButton)v.findViewById(R.id.btnMoveRight);
        btnMoveBack = (ImageButton)v.findViewById(R.id.btnMoveBack);
        btnMoveLeft = (ImageButton)v.findViewById(R.id.btnMoveLeft);

        btnGetUp = (ImageButton)v.findViewById(R.id.btnGetUp);
        btnGetDown = (ImageButton)v.findViewById(R.id.btnGetDown);

        btnMoveFront.setOnTouchListener(this);
        btnMoveRight.setOnTouchListener(this);
        btnMoveBack.setOnTouchListener(this);
        btnMoveLeft.setOnTouchListener(this);

        btnGetUp.setOnTouchListener(this);
        btnGetDown.setOnTouchListener(this);

        return v;
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