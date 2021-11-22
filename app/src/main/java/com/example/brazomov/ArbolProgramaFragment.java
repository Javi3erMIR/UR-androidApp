package com.example.brazomov;

import android.annotation.SuppressLint;
import android.app.StatusBarManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brazomov.database.DBHelper;
import com.example.brazomov.database.models.Directiva;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ArbolProgramaFragment extends Fragment{

    private Toolbar toolbar;
    private ListView listView;
    private MyAdapter adapter;
    private ArrayList<Directiva> model;
    private TextView txt_desc, txt_title;
    private DBHelper dbHelper;
    private String command_simple;
    private Cursor cursor;
    private ScriptCommand scriptCommand;
    private ScriptSender scriptSender;
    private Window window;
    private BottomNavigationView bottomNavigationView;

    public ArbolProgramaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_arbol_programa, container, false);
        listView = rootview.findViewById(R.id.List_view);
        toolbar = rootview.findViewById(R.id.toolbar_menu);
        toolbar.setNavigationIcon(R.mipmap.prog_icon_foreground);
        txt_desc = rootview.findViewById(R.id.txt_desc);
        toolbar.inflateMenu(R.menu.menu);
        model = new ArrayList<>();
        bottomNavigationView = rootview.findViewById(R.id.nav_bar_ur);
        statusBarChange();
        createDataBase(rootview);
        showData();
        fillRecyclerViewWihtDB();

        toolbar.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId()==R.id.Wait_node){
                createWaitNode();
            }
            else if(menuItem.getItemId()==R.id.Waypoint_node){
                createWaypointNode();
            }
            else if(menuItem.getItemId()==R.id.Set_node){
                createSetNode();
            }
            return false;
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Directiva directiva = model.get(i);
            if(directiva.getType().equals("Wait_node")){
                openWaitFrag(directiva);
            }
            if(directiva.getType().equals("Waypoint_node")){
                openWaypointFrag(directiva);
            }
            if(directiva.getType().equals("Set_node")){
                openSetFrag(directiva);
            }
        });

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            removeItem(i);
            return false;
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.item_play){
                sendSequence();
            }
            if(item.getItemId() == R.id.item_stop){
                stopSequence();
            }
            return false;
        });

        return rootview;
    }

    private void openWaitFrag(Directiva directiva) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", directiva.getId_directiva());
        bundle.putString("title", directiva.getTitle());
        getParentFragmentManager().setFragmentResult("key", bundle);
        Fragment fra = new WaitSettingd();
        FragmentManager fragman = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragman.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fra);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openWaypointFrag(Directiva directiva) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", directiva.getId_directiva());
        bundle.putString("title", directiva.getTitle());
        getParentFragmentManager().setFragmentResult("key", bundle);
        Fragment fra = new WaypointSettings();
        FragmentManager fragman = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragman.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fra);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openSetFrag(Directiva directiva) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", directiva.getId_directiva());
        bundle.putString("title", directiva.getTitle());
        getParentFragmentManager().setFragmentResult("key", bundle);
        Fragment fra = new SetNodeFragment();
        FragmentManager fragman = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragman.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fra);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void removeItem(int i){
        adapter.remove(model.get(i));
        dbHelper.deleteInto("Directiva", i);
        dbHelper.updateInto("Directiva", model.get(i).getContentValues(), i);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showData() {
        adapter = new MyAdapter(model, getContext());
        listView.setAdapter(adapter);
    }

    private void stopSequence() {
        scriptCommand.appendLine("stopl(1)");
        scriptSender.sendScriptCommand(scriptCommand);
        scriptCommand.clear();
    }

    private void createSetNode() {
        Directiva directiva = new Directiva(listView.getCount(), "SET NODE_" + (listView.getCount()+1), "Set_node");
        model.add(directiva);
        addDirectiva(directiva);
        adapter.notifyDataSetChanged();
    }

    private void createWaypointNode() {
        Directiva directiva = new Directiva(listView.getCount(), "WAYPOINT NODE_" + (listView.getCount()+1), "Waypoint_node");
        model.add(directiva);
        addDirectiva(directiva);
        adapter.notifyDataSetChanged();
    }

    private void createWaitNode() {
        Directiva directiva = new Directiva(listView.getCount(),"WAIT_" + (listView.getCount()+1), "Wait_node");
        model.add(directiva);
        addDirectiva(directiva);
        adapter.notifyDataSetChanged();
    }

    private void fillRecyclerViewWihtDB() {
        if(cursor.getCount() == 0) {
            Toast.makeText(getContext(), "La secuencia esta vacia", Toast.LENGTH_SHORT).show();
        }else if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Directiva directiva = new Directiva(listView.getCount(), cursor.getString(1), cursor.getString(2));
                model.add(directiva);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void createDataBase(View rootview) {
        dbHelper = new DBHelper(rootview.getContext());
        cursor = dbHelper.getAllDataFrom("Directiva");
        Cursor cursor1 = dbHelper.getAllDataFrom("Configuracion");
        if(cursor1.getCount() > 0){
            cursor1.moveToFirst();
            scriptSender = new ScriptSender(cursor1.getString(1));
            scriptCommand = new ScriptCommand();
        }
    }

    private void statusBarChange() {
        window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#333333"));
        window.getDecorView().setSystemUiVisibility(0);
    }

    private void sendSequence(){
        cursor = dbHelper.getAllDataFrom("Directiva");
        if(cursor.getCount() == 0) {
            Toast.makeText(getContext(), "La secuencia esta vacia", Toast.LENGTH_SHORT).show();
        }else if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getString(3).equals("desc")) {
                command_simple = "desc\n";
            } else if (!cursor.getString(3).equals("desc")) {
                command_simple = cursor.getString(3) + "\n";
            }
            while (cursor.moveToNext()) {
                if (cursor.getString(3).equals("desc")) {
                    command_simple += "desc\n";
                } else if (!cursor.getString(3).equals("desc")) {
                    command_simple += cursor.getString(3) + "\n";
                }
            }
            scriptCommand.appendLine(command_simple);
            scriptSender.sendScriptCommand(scriptCommand);
            scriptCommand.clear();
        }
        command_simple = "";
    }

    public void addDirectiva(Directiva directiva){
        boolean insert = dbHelper.insertInto("Directiva", directiva.getContentValues());
        dbHelper.updateInto("Directiva", directiva.getContentValues(), listView.getCount());
        if(insert==true){
            Toast.makeText(getContext(), "Secuencia añadida", Toast.LENGTH_SHORT).show();
        }else if(insert==false) {
            Toast.makeText(getContext(), "Secuencia no añadida", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FFFFFF"));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}
