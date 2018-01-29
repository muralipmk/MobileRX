package sssihms.hmis.mobilerx.AppUtil;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;


/**
 * This class creates an DialogFragment Which is common to all the alert shown to the user.
 * Created by mca2 on 8/7/15.
 */
public class CommonDailog extends DialogFragment {

    private String mTitle= "Message";
    private String mMessage= "Some Thing Went Wrong";

    public void setmMessage(String title, String message){
        this.mTitle= title;
        this.mMessage= message;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(GlobalUtil.getDubugPref(getActivity(), "pref_app_debug") && GlobalUtil.mServerResponse != null) {
            Log.d("Server Response: ", GlobalUtil.mServerResponse);
        }

        builder.setTitle(mTitle).setMessage(mMessage).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Now show some msg to display that he chose to override future..
                dismiss();
                getActivity().onBackPressed();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}