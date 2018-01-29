package sssihms.hmis.mobilerx.AppUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import sssihms.hmis.mobilerx.R;

/**
 * This is created for patient list view.
 * It creates the view for the patientDetails.
 * Created by Murali krishna on 13/2/16.
 */
public class PatientListAdapter extends ArrayAdapter<PatientDetails> {
    private Context mContext= null;
    private int mResource;
    private ArrayList<PatientDetails> mPatientList= null;


    public PatientListAdapter(Context context, int patientViewResourceId, ArrayList<PatientDetails> patientObjects){
        super(context, patientViewResourceId, patientObjects);
        this.mContext= context;
        this.mResource= patientViewResourceId;
        this.mPatientList= patientObjects;
    }

    public PatientListAdapter(Context context, int patientViewResourceId) {
        this(context, patientViewResourceId, new ArrayList<PatientDetails>());
    }


    public void updateValue(int position, PatientDetails object){
        mPatientList.set(position,object);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(this.mPatientList != null)
            return this.mPatientList.size();
        return 0;
    }

    @Override
    public Context getContext() {
        return this.mContext;
    }

    @Override
    public PatientDetails getItem(int position) {
        return this.mPatientList.get(position);
    }

    protected  static class ViewHolder{
        TextView patient_Bed= null;
        TextView patient_Name= null;
        TextView patient_Age_Gender= null;
        TextView patient_EncounterID= null;
        TextView patient_Adm_Date= null;
        TextView patient_Doctor_Name= null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    public void add(PatientDetails patientDetails){
        if (mPatientList != null) {
            mPatientList.add(patientDetails);
            notifyDataSetChanged();
        }
    }

    public View createViewFromResource(final int position, View convertView, ViewGroup parent){
        View view = convertView;

        PatientDetails patientDetails = mPatientList.get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.patient_details, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.patient_Bed= (TextView) view.findViewById(R.id.pd_bed_num);
            viewHolder.patient_Name= (TextView) view.findViewById(R.id.pd_patient_name);
            viewHolder.patient_Age_Gender= (TextView) view.findViewById(R.id.pd_age_gender);
            viewHolder.patient_EncounterID= (TextView) view.findViewById(R.id.pd_encounter_id);
            viewHolder.patient_Adm_Date= (TextView) view.findViewById(R.id.pd_admission_date);
            viewHolder.patient_Doctor_Name= (TextView) view.findViewById(R.id.pd_doctor_name);
            view.setTag(viewHolder);
        }else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.patient_Bed.setText(patientDetails.getmBedNumber());
        holder.patient_Name.setText(patientDetails.getmPatientName());
        holder.patient_Age_Gender.setText(patientDetails.getmPatientAgeGender());
        holder.patient_EncounterID.setText(patientDetails.getmPatientEncounterID());
        holder.patient_Adm_Date.setText(patientDetails.getmPatientAdmissionDate());
        holder.patient_Doctor_Name.setText(patientDetails.getmConsultDoctor());
        return view;
    }
}

