package sssihms.hmis.mobilerx.AppUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import sssihms.hmis.mobilerx.R;

import java.util.ArrayList;

public class OptionGridViewAdapter extends ArrayAdapter<String>{

    private ArrayList<String> mOptionsList;
    private LayoutInflater mInflater;


    public OptionGridViewAdapter(Context context, int resource) {
        this(context,resource,new ArrayList<String>());

    }

    public OptionGridViewAdapter(Context context, int resource, ArrayList<String> mOptionsList) {
        super(context, resource, mOptionsList);
        this.mOptionsList = mOptionsList;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mOptionsList.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return mOptionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return mOptionsList == null ? 0 : mOptionsList.get(position).hashCode();
    }

    @Override
    public void add(String object) {
        this.mOptionsList.add(object);
        notifyDataSetChanged();
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Button optionButton;

        if (convertView == null)
            view = mInflater.inflate(R.layout.single_gridoption, parent, false);
        else
            view = convertView;

        optionButton= (Button) view.findViewById(R.id.option_button);
        optionButton.setText(mOptionsList.get(position));
        return view;
    }

}
