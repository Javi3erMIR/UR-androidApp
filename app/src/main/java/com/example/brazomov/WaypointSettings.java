package com.example.brazomov;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brazomov.database.DBHelper;
import com.example.brazomov.database.models.Directiva;

public class WaypointSettings extends Fragment {

    private Button btn_set;
    private TextView txt_tit;
    private int index;
    private String tcp = "desc";
    private DBHelper dbHelper;
    private Cursor cursor;

    public WaypointSettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                index = result.getInt("index");
                cursor.move(index);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getContext());
        cursor = dbHelper.getAllDataFrom("Directiva");
        View v = inflater.inflate(R.layout.fragment_waypoint_settings, container, false);
        btn_set = (Button) v.findViewById(R.id.btn_setTcp);
        txt_tit = (TextView)v.findViewById(R.id.txt_ti);

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("index1", index);
                getParentFragmentManager().setFragmentResult("key1", bundle);
                Fragment fragmento_way = new SetWaypoinFrag();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, fragmento_way);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return v;
    }
}