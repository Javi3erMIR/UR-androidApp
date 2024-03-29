package com.example.brazomov;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brazomov.database.DBHelper;
import com.example.brazomov.database.models.Configuracion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfiguracionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfiguracionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnBorrarSecuencias;
    private Button btnRegresar;
    private Button btnGuardarConfiguracion;
    private EditText txtIp;
    private Configuracion configuracion;
    private DBHelper dbHelper;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfiguracionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfiguracionFragment newInstance(String param1, String param2) {
        ConfiguracionFragment fragment = new ConfiguracionFragment();
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
        View v = inflater.inflate(R.layout.fragment_configuracion, container, false);

        btnBorrarSecuencias = (Button) v.findViewById(R.id.btnBorrarSecuencias);
        btnGuardarConfiguracion = (Button) v.findViewById(R.id.btnGuardarConfiguracion);
        btnRegresar = (Button) v.findViewById(R.id.btnRegresar);
        txtIp = (EditText) v.findViewById(R.id.txtIP);

        dbHelper = new DBHelper(v.getContext());
        configuracion = new Configuracion();

        Cursor cursor = dbHelper.getAllDataFrom("Configuracion");

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            configuracion.getIpRobot(cursor.getString(1));
            this.txtIp.setText(cursor.getString(1));
        }

        btnBorrarSecuencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionPorBoton(view);
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionPorBoton(view);
            }
        });

        btnGuardarConfiguracion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                accionPorBoton(view);
            }
        });

        return v;
    }

    public void accionPorBoton(View v){
        if(v.getId() == R.id.btnBorrarSecuencias){
            dbHelper.deleteAllFrom("Directiva");
            dbHelper.deleteAllFrom("Secuencia");
        }else if(v.getId() == R.id.btnRegresar){
            Fragment fragmento = new MainFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragmento);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if(v.getId() == R.id.btnGuardarConfiguracion){
            configuracion.setIpRobot(txtIp.getText().toString());
            Cursor cursor = dbHelper.getAllDataFrom("Configuracion");
            if (cursor.getCount() > 0){
                if(dbHelper.updateInto("Configuracion",configuracion.getContentValues(),1)){
                    Toast.makeText(this.getActivity(),"Actualización Exitosa",Toast.LENGTH_LONG);
                }else{
                    Toast.makeText(this.getActivity(),"Actualización Fallida",Toast.LENGTH_LONG);
                }
            }else{
                if(dbHelper.insertInto("Configuracion",configuracion.getContentValues())){
                    Toast.makeText(this.getActivity(),"Actualización Exitosa",Toast.LENGTH_LONG);
                }else{
                    Toast.makeText(this.getActivity(),"Actualización Fallida",Toast.LENGTH_LONG);
                }
            }

        }
    }

}