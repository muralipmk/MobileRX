package sssihms.hmis.mobilerx.Verification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sssihms.hmis.mobilerx.R;

/**
 *
 * Created by mca2 on 27/2/16.
 */
public class VerificationAdapater  extends ArrayAdapter<VerificationObject> {
    private Context mContext= null;
    private int mResource;
    private ArrayList<VerificationObject> mVerificationList= null;


    public VerificationAdapater(Context context, int drugViewResourceId, ArrayList<VerificationObject> prescriptionObjects){
        super(context, drugViewResourceId, prescriptionObjects);
        this.mContext= context;
        this.mResource= drugViewResourceId;
        this.mVerificationList= prescriptionObjects;
    }

    public VerificationAdapater(Context context, int drugViewResourceId) {
        this(context, drugViewResourceId, new ArrayList<VerificationObject>());
    }


    public void updateValue(int position, VerificationObject object){
        mVerificationList.set(position,object);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(this.mVerificationList != null)
            return this.mVerificationList.size();
        return 0;
    }

    @Override
    public VerificationObject getItem(int position) {
        return this.mVerificationList.get(position);
    }

    protected  static class ViewHolder{
        TextView drugName_info;
        TextView frequency;
        TextView brandName_info;
        TextView shift;
        ImageView checkBox;
        TextView mediumInfo;
        FrameLayout mediumFrameLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    public void add(VerificationObject verificationDetails){
        if (mVerificationList != null) {
            mVerificationList.add(verificationDetails);
            notifyDataSetChanged();
        }
    }

    public View createViewFromResource(final int position, View convertView, ViewGroup parent){
        View view = convertView;

        final VerificationObject verificationDetails = mVerificationList.get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.verification_detail, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.drugName_info= (TextView) view.findViewById(R.id.verf_drug_info);
            viewHolder.frequency= (TextView) view.findViewById(R.id.verf_quantity);
            viewHolder.brandName_info= (TextView) view.findViewById(R.id.verf_brand_info);
            viewHolder.shift= (TextView) view.findViewById(R.id.verf_shift);
            viewHolder.checkBox= (ImageView) view.findViewById(R.id.verf_checkbox);
            viewHolder.mediumInfo= (TextView) view.findViewById(R.id.verf_medium_info);
            viewHolder.mediumFrameLayout= (FrameLayout) view.findViewById(R.id.medium_layout);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerificationObject element = (VerificationObject) viewHolder.checkBox.getTag();

                    if (element.ismVerifyCheck())
                        v.setBackgroundResource(R.drawable.uncheck);
                    else
                        v.setBackgroundResource(R.drawable.check);

                    element.setmVerifyCheck(!element.ismVerifyCheck());
                }
            });
            view.setTag(viewHolder);
            viewHolder.checkBox.setTag(mVerificationList.get(position));
        }else{
            view = convertView;
            ((ViewHolder) view.getTag()).checkBox.setTag(mVerificationList.get(position));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        String drugInfo= verificationDetails.getmIGM_DRUG_NAME() + " " + verificationDetails.getmPPD_STRENGTH() + " "
                + verificationDetails.getmDRUG_UOM();
        String brandInfo= verificationDetails.getmBRAND_NAME() + " " + verificationDetails.getmDAD_QUANTITY() + " "
                + verificationDetails.getmUOM_NAME();

        if(verificationDetails.getmMEDIUM_BRAND_NAME() != null){
            holder.mediumFrameLayout.setVisibility(View.VISIBLE);
            holder.mediumInfo.setVisibility(View.VISIBLE);
            holder.mediumInfo.setText(verificationDetails.getmMEDIUM_BRAND_NAME() + "  " +
                    verificationDetails.getmPPD_MEDIUM_STRENGTH() + " " + verificationDetails.getmMEDIUM_UOM_NAME());
        }

        holder.drugName_info.setText(drugInfo);
        holder.brandName_info.setText(brandInfo);
        holder.frequency.setText(verificationDetails.getmFREQUENCY());
        holder.shift.setText(verificationDetails.getmDAD_DOSE_NUM());

        if(verificationDetails.getmDAD_STATUS().equals("ADMINISTERED"))
            verificationDetails.setmVerifyCheck(false);
        else
            verificationDetails.setmVerifyCheck(true);

        if(verificationDetails.ismVerifyCheck()) {
            holder.checkBox.setBackgroundResource(R.drawable.check);
            view.setBackgroundColor(getContext().getResources().getColor(R.color.verified));
        }
        else {
            holder.checkBox.setBackgroundResource(R.drawable.uncheck);
            view.setBackgroundColor(getContext().getResources().getColor(R.color.deverified));
        }

        return view;
    }

}
