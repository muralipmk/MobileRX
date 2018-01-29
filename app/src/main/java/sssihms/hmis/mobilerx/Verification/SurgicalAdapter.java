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
 * Created by mca2 on 4/3/16.
 */
public class SurgicalAdapter extends ArrayAdapter<SurgicalObject> {
    private Context mContext= null;
    private int mResource;
    private ArrayList<SurgicalObject> mVerificationList= null;


    public SurgicalAdapter(Context context, int drugViewResourceId, ArrayList<SurgicalObject> surgicalObjects){
        super(context, drugViewResourceId, surgicalObjects);
        this.mContext= context;
        this.mResource= drugViewResourceId;
        this.mVerificationList= surgicalObjects;
    }

    public SurgicalAdapter(Context context, int drugViewResourceId) {
        this(context, drugViewResourceId, new ArrayList<SurgicalObject>());
    }


    @Override
    public int getCount() {
        if(this.mVerificationList != null)
            return this.mVerificationList.size();
        return 0;
    }

    @Override
    public SurgicalObject getItem(int position) {
        return this.mVerificationList.get(position);
    }

    protected  static class ViewHolder{
        TextView drugName_info;
        TextView quantity;
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

    public void add(SurgicalObject surgicalObject){
        if (mVerificationList != null) {
            mVerificationList.add(surgicalObject);
            notifyDataSetChanged();
        }
    }

    public View createViewFromResource(final int position, View convertView, ViewGroup parent){
        View view = convertView;

        final SurgicalObject verificationDetails = mVerificationList.get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.verification_detail, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.drugName_info= (TextView) view.findViewById(R.id.verf_drug_info);
            viewHolder.quantity= (TextView) view.findViewById(R.id.verf_quantity);
            viewHolder.brandName_info= (TextView) view.findViewById(R.id.verf_brand_info);
            viewHolder.shift= (TextView) view.findViewById(R.id.verf_shift);
            viewHolder.checkBox= (ImageView) view.findViewById(R.id.verf_checkbox);
            viewHolder.mediumInfo= (TextView) view.findViewById(R.id.verf_medium_info);
            viewHolder.mediumFrameLayout= (FrameLayout) view.findViewById(R.id.medium_layout);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SurgicalObject element = (SurgicalObject) viewHolder.checkBox.getTag();

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

        String quantity= verificationDetails.getmQUANTITY() + " " + verificationDetails.getmUOM_NAME();

        holder.drugName_info.setText(verificationDetails.getmDRUG_NAME());
        holder.brandName_info.setText(verificationDetails.getmBRAND_NAME());
        holder.quantity.setText(quantity);
        holder.shift.setText(verificationDetails.getmDOSE_NUM());

        if(verificationDetails.getmSTATUS().equals("SURG_OUTSTANDING"))
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
