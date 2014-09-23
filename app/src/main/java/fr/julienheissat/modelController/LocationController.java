package fr.julienheissat.modelController;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.julienheissat.application.TaskManagerApplication;
import fr.julienheissat.taskmanager.R;
import fr.julienheissat.utils.LocationUtils;
import fr.julienheissat.utils.PlayConnectionService;

/**
 * Class to connect to google play services and propose several location services to listeners :
 * - Get latest location
 * - Get location updates on a regular interval
 * - Retrieve the address of current location (Geocoder)
 * - To be instantiated during application startup ?
 */
public class LocationController implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener
     //   ,LocationListener
{
    // Context of the location controller

    private final TaskManagerApplication app;

    //List of LocationController listeners
    private ArrayList<LocationControllerListener> listOfListener;

    //Objects to connect to Google services
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;


    //Location results
    private Location mLatestLocation;
    private String mLatestAddress;


    public LocationController(TaskManagerApplication app)
    {

        this.app = app;
        listOfListener = new ArrayList<LocationControllerListener>();
        connectLocationClient();
        Log.d("LocationController - new instance", "Constructor");
    }


    /**
     * Overriding methods from  GooglePlayServicesClient.ConnectionCallbacks
     */
    @Override
    public void onConnected(Bundle bundle)
    {
        if (null != mLocationClient && mLocationClient.isConnected())
        {
            mLatestLocation = mLocationClient.getLastLocation();
        }
        Log.i("LocationController - onConnected", "Bundle:" + bundle);

        updateAllListenersOnConnection();
        disconnectLocationClient();
    }


    @Override
    public void onDisconnected()
    {
        Log.i("LocationController - onDisconnected", "Disconnection");
        updateAllListenersOnDisconnection();
    }


    /**
     * Overriding method from GooglePlayServicesClient.OnConnectionFailedListener
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (connectionResult.hasResolution())
        {
            Log.i("LocationController - onConnectionFailed", "connectionResultHasResolution=" + connectionResult);
        } else
        {
            Log.i("LocationController - onConnectionFailed", "connectionResultNotHasResolution=" + connectionResult);
        }
    }


    /**
     * Overriding method from LocationListener
     */
//    @Override
//    public void onLocationChanged(Location location)
//    {
//        Log.i("LocationController - onLocationChanged", "Location:"+location);
//        mLatestLocation = mLocationClient.getLastLocation();
//        updateAllListeners();
//
//    }


    /**
     * Methods to connect/disconnect to location client. Methods to request and stop location updates to google play services
     */
    public void connectLocationClient()
    {
        if (mLocationClient == null)
        {
            mLocationClient = new LocationClient(app, this, this);
        }
        if (!mLocationClient.isConnected())
        {
            mLocationClient.connect();
        }
    }

    public void disconnectLocationClient()
    {
        if (null != mLocationClient)
        {
            mLocationClient.disconnect();
            Log.i("LocationController - disconnectionLocationClient", "location client disconnected");
        }
    }

//    public void requestLocationUpdates()
//    {
//
//        if (PlayConnectionService.servicesConnected(app) && mLocationClient.isConnected())
//        {
//            if (mLocationRequest == null)
//            {
//                mLocationRequest = LocationRequest.create().setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS)
//                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                        .setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
//            }
//
//            mLocationClient.requestLocationUpdates(mLocationRequest, this);
//        } else
//        {
//            Log.i("LocationController - requestLocationUpdates not working", "Connection to google play" + PlayConnectionService.servicesConnected(app));
//        }
//
//    }

//    public void stopLocationUpdates()
//    {
//        if (null != mLocationClient)
//        {
//            mLocationClient.removeLocationUpdates(this);
//        }
//    }

    /**
     * Methods to retrieve location and address
     */


    public Location getLatestLocation()
    {
        return mLatestLocation;
    }


    public String getLatestAddress()
    {
        return mLatestAddress;
    }


    /**
     * Method to ask for address based on current location using geocoder
     */


    public boolean queryLatestAddress()
    {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent())
        {
            Log.e("LocationController - queryLatestAddress", "Geocoder is present:" + Geocoder.isPresent());
            return false;
        }

        else if (PlayConnectionService.servicesConnected(app))
        {



            // Start the background task
           if (mLatestLocation !=null){

                    new GetAddressTask(app).execute(mLatestLocation);
                    return true;
             }
            else
           {
               return false;
           }
        }

        else
        {
            Log.e("LocationController - queryLatestAddress", "Connection services connection:" + PlayConnectionService.servicesConnected(app));
            return false;
        }



    }



    protected class GetAddressTask extends AsyncTask<Location, Void, String>
    {

        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;

        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context)
        {

            // Required by the semantics of AsyncTask
            super();

            // Set a Context for the background task
            localContext = context;
        }

        /**
         * Get a geocoding service instance, pass latitude and longitude to it, format the returned
         * address, and return the address to the UI thread.
         */
        @Override
        protected String doInBackground(Location... params)
        {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

            // Get the current location from the input parameter list

            Location location = params[0];

            // Create a list to contain the result address
            List<Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try
            {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
                addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                Log.i("LocationController - GetAddressTask", "Number of addresses:" + addresses.size());

                // Catch network or other I/O problems.
            } catch (IOException exception1)
            {

                // Log an error and return an error message
                Log.e(LocationUtils.GEOCODERTAG, app.getString(R.string.IO_Exception_getFromLocation));

                // print the stack trace
                exception1.printStackTrace();

                // Return an error message
                return (app.getString(R.string.IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
            } catch (IllegalArgumentException exception2)
            {

                // Construct a message containing the invalid arguments
                String errorString = app.getString(
                        R.string.illegal_argument_exception,
                        location.getLatitude(),
                        location.getLongitude()
                );
                // Log the error and print the stack trace
                Log.e(LocationUtils.GEOCODERTAG, errorString);
                exception2.printStackTrace();

                //
                return errorString;
            }


            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0)
            {

                // Get the first address
                Address address = addresses.get(0);

                // Format the first line of address
                String addressText = app.getString(R.string.address_output_string,

                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",

                        // Locality is usually a city
                        address.getLocality(),

                        // The country of the address
                        address.getCountryName()
                );


                // Return the text
                return addressText;

                // If there aren't any addresses, post a message
            } else
            {
                return app.getString(R.string.no_address_found);
            }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(String addressFound)
        {
            mLatestAddress = addressFound;
            updateAllListenersOnAddressChanged();
        }
    }


    /**
     * Listener Interface with registration methods
     */
    public static interface LocationControllerListener
    {
        public void locationChanged(LocationController locationController);

        public void locationControllerConnected(LocationController locationController);

        public void locationControllerDisConnected(LocationController locationController);

        public void addressChanged(LocationController locationController);
    }
    /**
     * Registration methods
     */
    public void register(LocationControllerListener listener)

    {

        listOfListener.add(listener);
        Log.i("LocationControllerListener", "registered" + listener);
    }

    public void unregister(LocationControllerListener listener)
    {
        listOfListener.remove(listener);
        Log.i("LocationControllerListener", "unregistered" + listener);
    }

    public void unregisterAll()
    {
        listOfListener.removeAll(listOfListener);
        Log.i("LocationControllerListener", "unregister all" + listOfListener);
    }

    /**
     * Update methods to listeners
     */
    public void updateAllListeners()
    {
        for (LocationControllerListener listener : listOfListener)
        {
            listener.locationChanged(this);
        }
    }

    public void updateAllListenersOnConnection()
    {
        for (LocationControllerListener listener : listOfListener)
        {
            listener.locationControllerConnected(this);
        }
    }

    public void updateAllListenersOnDisconnection()
    {
        for (LocationControllerListener listener : listOfListener)
        {
            listener.locationControllerDisConnected(this);
        }
    }

    public void updateAllListenersOnAddressChanged()
    {
        for (LocationControllerListener listener : listOfListener)
        {
            listener.locationControllerDisConnected(this);
        }
    }

}

