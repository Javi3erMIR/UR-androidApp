package com.example.brazomov;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brazomov.database.DBHelper;
import com.example.brazomov.database.models.Directiva;

public class WaitSettingd extends Fragment {

    private EditText txt_wait;
    private int index;
    private Button btn_ok, btn_can;
    private DBHelper dbHelper;
    private Cursor cursor, cursor1;
    private Directiva directiva;
    private TextView textView;

    public WaitSettingd() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                index = result.getInt("index");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_move_settings, container, false);
        dbHelper = new DBHelper(getContext());
        cursor = dbHelper.getAllDataFrom("Directiva");
        cursor.move(index);
        txt_wait = (EditText) rootView.findViewById(R.id.txt_secon);
        btn_ok = (Button) rootView.findViewById(R.id.btn_ok_wait);
        btn_can = (Button) rootView.findViewById(R.id.btn_cancel_wait);
        textView = (TextView) rootView.findViewById(R.id.txt_time);
        if(cursor.getCount() > 0) {
            try {
                textView.setText(cursor.getString(3));
            }catch (CursorIndexOutOfBoundsException w){
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "sleep(" + txt_wait.getText().toString() + ")";
                cursor.moveToFirst();
                cursor.move(index);
                directiva = new Directiva(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                directiva.setCodigo(text);
                dbHelper.updateInto("Directiva", directiva.getContentValues(), index);
                closefrag();
            }
        });

        btn_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    private void closefrag(){
        getActivity().onBackPressed();
    }
}