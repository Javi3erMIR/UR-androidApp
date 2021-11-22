package com.example.brazomov;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.brazomov.database.DBHelper;
import com.example.brazomov.database.models.Directiva;

public class SetNodeFragment extends Fragment {

    private Spinner spinner_1, spinner_2;
    private String command_prefix = "set_digital_out(", command_postfix, command_middle;
    private int index;
    private Directiva model;
    private Cursor cursor;
    private DBHelper dbHelper;
    private Button btnOK;
    private RadioButton rb1, rb2;

    public SetNodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                index = result.getInt("index");
                //title = result.getString("title");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_set_node, container, false);
        spinner_1 = (Spinner)root.findViewById(R.id.spin_io);
        spinner_2 = (Spinner)root.findViewById(R.id.spin_status);
        btnOK = (Button)root.findViewById(R.id.btn_ok_set);
        rb1 = (RadioButton)root.findViewById(R.id.radio_none_action);
        rb2 = (RadioButton)root.findViewById(R.id.radio_set_digital);
        dbHelper = new DBHelper(root.getContext());
        cursor = dbHelper.getAllDataFrom("Directiva");
        if(cursor.getCount() > 0){
            try {

            }catch (CursorIndexOutOfBoundsException e){
                Log.e("----", "no data");
            }
        }
        String[] io_ops = new String[8];
        for(int i = 0; i < 8; i++){
            io_ops[i] = "digital_out["+i+"]";
        }
        String[] status = {"Low", "High"};
        ArrayAdapter<String> arrayAdapter_1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, status);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, io_ops);
        spinner_1.setAdapter(arrayAdapter);
        spinner_2.setAdapter(arrayAdapter_1);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setItemIndex();
                setCommand();
                getActivity().onBackPressed();
            }
        });

        return root;
    }

    public void setItemIndex(){
        command_middle = String.valueOf(spinner_1.getSelectedItemPosition()) + ",";
    }

    public void setCommand(){
        try {
            if(spinner_2.getSelectedItem().toString().equals("Low") && rb2.isChecked()){
                command_postfix = "False)";
                String command = command_prefix + command_middle + command_postfix;
                cursor.moveToFirst();
                cursor.move(index);
                model = new Directiva(index, cursor.getString(1), "Set_node");
                model.setCodigo(command);
                dbHelper.updateInto("Directiva", model.getContentValues(), index);
            }
            else if(spinner_2.getSelectedItem().toString().equals("High") && rb2.isChecked()){
                command_postfix = "True)";
                String command = command_prefix + command_middle + command_postfix;
                cursor.moveToFirst();
                cursor.move(index);
                model = new Directiva(index, cursor.getString(1), "Set_node");
                model.setCodigo(command);
                dbHelper.updateInto("Directiva", model.getContentValues(), index);
            }
        }catch (CursorIndexOutOfBoundsException | SQLiteConstraintException e){
            Toast.makeText(getContext(), "Dato no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}