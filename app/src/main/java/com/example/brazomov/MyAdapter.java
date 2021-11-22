package com.example.brazomov;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.brazomov.database.DBHelper;
import com.example.brazomov.database.models.Directiva;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Directiva> {

    private ArrayList<Directiva> nodes_des;
    private Context context;
    private DBHelper dbHelper;
    private Cursor cursor;

    MyAdapter(ArrayList<Directiva> nodes, Context c){
        super(c, R.layout.row, nodes);
        this.context = c;
        this.nodes_des = nodes;
    }

    private static class ViewHolder {
        TextView title;
        TextView code;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Directiva model = getItem(position);
        dbHelper = new DBHelper(getContext());
        cursor = dbHelper.getAllDataFrom("Directiva");
        cursor.moveToFirst();
        cursor.move(position);
        ViewHolder viewHolder = new ViewHolder();
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        viewHolder.title = row.findViewById(R.id.node_title);
        viewHolder.code = row.findViewById(R.id.txt_desc);
        viewHolder.title.setText(model.getTitle());
        if(cursor.getString(3) != null){
            viewHolder.code.setText(cursor.getString(3));
        }else{
            viewHolder.code.setText(model.getCodigo());
        }
        return row;
    }


}
