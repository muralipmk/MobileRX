package sssihms.hmis.mobilerx.AppUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import sssihms.hmis.mobilerx.R;
import java.util.ArrayList;

/**
 *
 * Created by mca2 on 19/12/15.
 */
public class DrugListAdapter extends ArrayAdapter<PrescriptionObject> {

    private Context mContext= null;
    private int mResource;
    private ArrayList<PrescriptionObject> mPrescriptionList= null;


    public DrugListAdapter(Context context, int drugViewResourceId, ArrayList<PrescriptionObject> prescriptionObjects){
        super(context, drugViewResourceId, prescriptionObjects);
        this.mContext= context;
        this.mResource= drugViewResourceId;
        this.mPrescriptionList= prescriptionObjects;
    }

    public DrugListAdapter(Context context, int drugViewResourceId) {
        this(context, drugViewResourceId, new ArrayList<PrescriptionObject>());
    }


    public void updateValue(int position, PrescriptionObject object){
        mPrescriptionList.set(position,object);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(this.mPrescriptionList != null)
            return this.mPrescriptionList.size();
        return 0;
    }

    @Override
    public PrescriptionObject getItem(int position) {
        return this.mPrescriptionList.get(position);
    }

    protected  static class ViewHolder{
        TextView form;
        TextView drugName;
        TextView unit;
        TextView route;
        TextView frequency_medium;
        TextView medium_strength_uom;
        TextView status;
        TextView date_time;
        TextView end_time;
        ImageView checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    public void add(PrescriptionObject prescriptionDetails){
        if (mPrescriptionList != null) {
            mPrescriptionList.add(prescriptionDetails);
            notifyDataSetChanged();
        }
    }

    public View createViewFromResource(final int position, View convertView, ViewGroup parent){
        View view = convertView;

        final PrescriptionObject prescriptionDetails = mPrescriptionList.get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drug_details, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.form= (TextView) view.findViewById(R.id.form_textView);
            viewHolder.drugName= (TextView) view.findViewById(R.id.drug_name_textView);
            viewHolder.unit= (TextView) view.findViewById(R.id.unit_textView);
            viewHolder.route= (TextView) view.findViewById(R.id.route_textView);
            viewHolder.frequency_medium= (TextView) view.findViewById(R.id.freq_medium_textview);
            viewHolder.medium_strength_uom= (TextView) view.findViewById(R.id.medium_uom);
            viewHolder.medium_strength_uom.setVisibility(View.INVISIBLE);
            viewHolder.date_time= (TextView) view.findViewById(R.id.start_date_time);
            viewHolder.status= (TextView) view.findViewById(R.id.dd_status);
            viewHolder.end_time= (TextView) view.findViewById(R.id.end_date_time);
            viewHolder.checkBox= (ImageView) view.findViewById(R.id.drug_CheckBox);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrescriptionObject element = (PrescriptionObject) viewHolder.checkBox.getTag();

                    if (element.isChecked())
                        v.setBackgroundResource(R.drawable.uncheck);
                    else
                        v.setBackgroundResource(R.drawable.check);

                    element.setChecked(!element.isChecked());
                }
            });
            view.setTag(viewHolder);
            viewHolder.checkBox.setTag(mPrescriptionList.get(position));
        }else{
            view = convertView;
            ((ViewHolder) view.getTag()).checkBox.setTag(mPrescriptionList.get(position));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.form.setText(prescriptionDetails.getmFormInfo().getmDescription());
        holder.drugName.setText(prescriptionDetails.getmDrugInfo().getmDescription());
        holder.unit.setText(prescriptionDetails.getmStrength() + " " + prescriptionDetails.getmUomInfo().getmDescription());
        holder.route.setText(prescriptionDetails.getmRouteInfo().getmDescription());
        if(prescriptionDetails.getmFrequencyInfo() != null)
            holder.frequency_medium.setText(prescriptionDetails.getmFrequencyInfo().getmDescription());
        else { //setting the medium details.
            holder.frequency_medium.setText(prescriptionDetails.getmMediumInfo().getmDescription());
            holder.medium_strength_uom.setVisibility(View.VISIBLE);
            holder.medium_strength_uom.setText(prescriptionDetails.getmMediumStrength()
                    + " " + prescriptionDetails.getmMediumUomInfo().getmDescription());
        }
        holder.date_time.setText(prescriptionDetails.getmStartDateTime());
        holder.end_time.setText(prescriptionDetails.getmEndDateTime());
        holder.status.setText(prescriptionDetails.getmStatus());
        if(prescriptionDetails.isChecked())
            holder.checkBox.setBackgroundResource(R.drawable.check);
        else
            holder.checkBox.setBackgroundResource(R.drawable.uncheck);

        return view;
    }
}
