package sssihms.hmis.mobilerx.AppUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sssihms.hmis.mobilerx.R;

/**
 * It holds the listview adapter for the object which has the id and the description.
 * Example: chemical_ID and the Chemical Name etc..,
 * This is used by the listviews which has the object type id and the description.
 * Created by Murali Krishna on 13/2/16.
 */
public class DescriptionAndIdAdapter extends ArrayAdapter<DescriptionAndIdHolder> implements Filterable {

    private ArrayList<DescriptionAndIdHolder> mList= null;
    private ArrayList<DescriptionAndIdHolder> mOriginalList= null;
    private Filter mFilter= null;
    private int mResource= 0;

    public DescriptionAndIdAdapter(Context context, int resource) {
        this(context, resource, new ArrayList<DescriptionAndIdHolder>());
    }

    public DescriptionAndIdAdapter(Context context, int resource, ArrayList<DescriptionAndIdHolder> mList) {
        super(context, resource);
        this.mList = mList;
        this.mResource= resource;
    }

    public int getPositionByDescription(String description){
        for(int i= 0;i < mList.size();i++){
            if(mList.get(i).getmDescription().equals(description))
                return i;
        }
        return -1;
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position,convertView,parent);
    }


    @Override
    public DescriptionAndIdHolder getItem(int position) {
        return mList.get(position);
    }


    /**
     * Remove all elements from the list.
     */
    public void clear() {
        if (mOriginalList != null) {
            mOriginalList.clear();
        } else {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    public void add(DescriptionAndIdHolder chemicalDetails){
        if (mOriginalList != null) {
            mOriginalList.add(chemicalDetails);
        } else {
            mList.add(chemicalDetails);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(mResource, null);
        }

        DescriptionAndIdHolder drugDetails = mList.get(position);

        if (drugDetails != null) {
            TextView drugName= (TextView) v;
            if (drugName != null){
                drugName.setText(drugDetails.getmDescription());
            }
        }
        return v;
    }



    /**
     * {@inheritDoc}
     */
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalList == null)
                mOriginalList = new ArrayList<>(mList);

            if (prefix == null || prefix.length() == 0) {
                ArrayList<DescriptionAndIdHolder> list = new ArrayList<>(mOriginalList);
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<DescriptionAndIdHolder> values = new ArrayList<>(mOriginalList);

                final int count = values.size();
                final ArrayList<DescriptionAndIdHolder> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final DescriptionAndIdHolder value = values.get(i);
                    final String valueText = value.getmDescription().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.contains(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");

                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.contains(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mList = (ArrayList<DescriptionAndIdHolder>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}
