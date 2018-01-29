package sssihms.hmis.mobilerx.Prescription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sssihms.hmis.mobilerx.R;

/**
 * Created by mca2 on 2/3/16.
 */
public class AuthorizeAdapter   extends ArrayAdapter<AuthorizationObject> {
    private Context mContext = null;
    private int mResource;
    private ArrayList<AuthorizationObject> mAuthorizeList = null;


    public AuthorizeAdapter(Context context, int drugViewResourceId, ArrayList<AuthorizationObject> prescriptionObjects) {
        super(context, drugViewResourceId, prescriptionObjects);
        this.mContext = context;
        this.mResource = drugViewResourceId;
        this.mAuthorizeList = prescriptionObjects;
    }

    public AuthorizeAdapter(Context context, int drugViewResourceId) {
        this(context, drugViewResourceId, new ArrayList<AuthorizationObject>());
    }


    public void updateValue(int position, AuthorizationObject object) {
        mAuthorizeList.set(position, object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (this.mAuthorizeList != null)
            return this.mAuthorizeList.size();
        return 0;
    }

    @Override
    public AuthorizationObject getItem(int position) {
        return this.mAuthorizeList.get(position);
    }

    protected static class ViewHolder {
        TextView drugName_info;
        TextView drug_admin_date;
        TextView brandName_info;
        TextView shift;
        ImageView checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    public void add(AuthorizationObject verificationDetails) {
        if (mAuthorizeList != null) {
            mAuthorizeList.add(verificationDetails);
            notifyDataSetChanged();
        }
    }

    public View createViewFromResource(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final AuthorizationObject authorizationDetails = mAuthorizeList.get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.authorization_details, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.drugName_info = (TextView) view.findViewById(R.id.auth_drug_info);
            viewHolder.brandName_info = (TextView) view.findViewById(R.id.auth_brand_info);
            viewHolder.shift = (TextView) view.findViewById(R.id.auth_shift);
            viewHolder.drug_admin_date= (TextView) view.findViewById(R.id.auth_admin_date);
            viewHolder.checkBox = (ImageView) view.findViewById(R.id.auth_checkbox);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthorizationObject element = (AuthorizationObject) viewHolder.checkBox.getTag();

                    if (element.ismAuthorizeCheck())
                        v.setBackgroundResource(R.drawable.uncheck);
                    else
                        v.setBackgroundResource(R.drawable.check);

                    element.setmAuthorizeCheck(!element.ismAuthorizeCheck());
                }
            });
            view.setTag(viewHolder);
            viewHolder.checkBox.setTag(mAuthorizeList.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkBox.setTag(mAuthorizeList.get(position));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        String drugInfo = authorizationDetails.getmDRUG_NAME()+ " " + authorizationDetails.getmDRUG_STRENGTH() + " "
                + authorizationDetails.getmDRUG_UOM_NAME();
        String brandInfo = authorizationDetails.getmBRAND_NAME() + " " + authorizationDetails.getmBRAND_QUANTITY() + " "
                + authorizationDetails.getmBRAND_UOM();

        holder.drugName_info.setText(drugInfo);
        holder.drug_admin_date.setText(authorizationDetails.getmDAD_ADMIN_DATE());
        holder.brandName_info.setText(brandInfo);
        holder.shift.setText(authorizationDetails.getmSTATUS());

        if (authorizationDetails.ismAuthorizeCheck())
            holder.checkBox.setBackgroundResource(R.drawable.check);
        else
            holder.checkBox.setBackgroundResource(R.drawable.uncheck);

        return view;
    }
}

