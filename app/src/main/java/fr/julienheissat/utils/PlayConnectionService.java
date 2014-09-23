package fr.julienheissat.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import fr.julienheissat.taskmanager.R;

/**
 * Created by juju on 20/09/2014.
 */
public class PlayConnectionService

{
   /*
    * Method to check if app is connected to Google Play services
    */

    public static final String SERVICECONNECTED = "PlayConnectionService";

    public static boolean servicesConnected(Context thisContext)
    {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(thisContext);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode)
        {
            // In debug mode, log the status
            Log.d(SERVICECONNECTED, thisContext.getString(R.string.play_services_available));

         return true;
        }

        else
        {
            Log.e(SERVICECONNECTED, thisContext.getString(R.string.play_services_unavailable));
        }
        return false;
    }

}
