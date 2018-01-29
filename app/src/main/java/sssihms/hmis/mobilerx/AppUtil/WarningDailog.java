package sssihms.hmis.mobilerx.AppUtil;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by mca2 on 11/3/16.
 */
public class WarningDailog extends DialogFragment {
    private String mTitle= "Message";
    private String mMessage= "Some Thing Went Wrong";


    public void setmMessage(String title, String message){
        this.mTitle= title;
        this.mMessage= message;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(mTitle).setMessage(mMessage).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Now show some msg to display that he chose to override future..
                dismiss();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
