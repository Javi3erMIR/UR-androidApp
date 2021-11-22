package com.example.brazomov;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnArbol,btnLibre,btnSalir, btnConfig;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        final View v = inflater.inflate(R.layout.fragment_main, container, false);

        btnArbol = (Button) v.findViewById(R.id.btnArbolPrograma);
        btnLibre = (Button) v.findViewById(R.id.btnLibre);
        btnConfig = (Button) v.findViewById(R.id.btnConfiguracion);
        btnSalir = (Button) v.findViewById(R.id.btnSalir);

        btnArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionPorBoton(view);
            }
        });

        btnLibre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionPorBoton(view);
            }
        });

        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionPorBoton(view);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionPorBoton(view);
            }
        });

        return v;
    }

    public void accionPorBoton(View view){

        if(view.getId() == R.id.btnLibre){
            Fragment fragmento = new FreeMoveFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();         
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragmento);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if(view.getId() == R.id.btnArbolPrograma){
            Fragment fra = new ArbolProgramaFragment();
            FragmentManager fragman = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragman.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fra);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(view.getId() == R.id.btnConfiguracion){
            Fragment fragmento_confg = new ConfiguracionFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragmento_confg);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(view.getId() == R.id.btnSalir){
            System.exit(0);
        }

    }

}