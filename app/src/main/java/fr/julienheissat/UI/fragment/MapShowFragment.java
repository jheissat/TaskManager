package fr.julienheissat.ui.fragment;


import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.julienheissat.application.TaskManagerApplication;
import fr.julienheissat.modelController.LocationController;
import fr.julienheissat.taskmanager.R;
import fr.julienheissat.utils.PlayConnectionService;


/**
 * Fragment displaying the map after connection to google play services, zooming to current location
 * and displaying list of tasks on the current Map
 * Using support library
 */

public class MapShowFragment extends Fragment implements LocationController.LocationControllerListener
{


    public static final String MAPSHOWFRAGMENTLIFECYCLE = "Map fragment lifecycle";
    public static final String MAPSHOWFRAGMENTLOCATIONCALLBACKS = "Map fragment location callbacks";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProgressBar mProgressBar;
    private TextView mAddress;
    private MapView mapView;
    private Location currentLocation;
    private GoogleMap mMap;
    private TaskManagerApplication app;
    private boolean locationRetrieved;
    private boolean addressRetrieved;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnMapFragmentInteractionListener mListener;

    //null constructor required by the fragmentManager
    public MapShowFragment()
    {
    }


    /**
     * Fragment lifecycle methods
     */


    // TODO: Rename and change types and number of parameters
    public static MapShowFragment newInstance(String param1, String param2)
    {
        Log.d(MAPSHOWFRAGMENTLIFECYCLE, "new instance");

        MapShowFragment fragment = new MapShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onAttach(Activity activity)
    {

        Log.d(MAPSHOWFRAGMENTLIFECYCLE, "OnAttach");
        super.onAttach(activity);

        // locationController.queryLatestAddress();
        try
        {
            mListener = (OnMapFragmentInteractionListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(MAPSHOWFRAGMENTLIFECYCLE, "OnCreate");
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        app = (TaskManagerApplication) this.getActivity().getApplication();
        app.getLocationController().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        Log.d(MAPSHOWFRAGMENTLIFECYCLE, "OnCreateView");
        View fragmentView = inflater.inflate(R.layout.fragment_map, container, false);




        if (PlayConnectionService.servicesConnected(getActivity()))
        {
            MapsInitializer.initialize(getActivity());
        }


        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()))
        {
            case ConnectionResult.SUCCESS:
                Log.i(MAPSHOWFRAGMENTLIFECYCLE, "OnCreateView connection success");
                mapView = (MapView) fragmentView.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                // Gets to GoogleMap from the MapView and does initialization stuff
                if (mapView != null)
                {
                    mMap = mapView.getMap();
                 // Direct method to retrieve current location from GoogleMap.
                 //   mMap.getUiSettings().setMyLocationButtonEnabled(false);
                 //   mMap.setMyLocationEnabled(true);


                        if (getCurrentLocation())
                        {
                            Log.i(MAPSHOWFRAGMENTLIFECYCLE, "OnCreateView - Location retrieved");

                            LatLng latlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latlng)
                                    .zoom(10)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mMap.addMarker(new MarkerOptions()
                                    .position(latlng));
                        }

                        else
                        {
                            app.getLocationController().connectLocationClient();
                        }

                }

                break;
            case ConnectionResult.SERVICE_MISSING:
                Log.d(MAPSHOWFRAGMENTLIFECYCLE, "OnCreateView - Service missing");
                Toast.makeText(getActivity(), "Service missing", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Log.d(MAPSHOWFRAGMENTLIFECYCLE, "OnCreateView- Update required");
                Toast.makeText(getActivity(), "Update required", Toast.LENGTH_SHORT).show();
                break;

            default:
                Log.d(MAPSHOWFRAGMENTLIFECYCLE, "OnCreateView Issue connecting Google map");
        }

        // Get progress bar and address objects from layout
        mAddress = (TextView) fragmentView.findViewById(R.id.address);
        mProgressBar = (ProgressBar) fragmentView.findViewById(R.id.address_progress);

        if (!addressRetrieved)
        {
            app.getLocationController().queryLatestAddress();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        return fragmentView;
    }


    @Override
    public void onDetach()
    {
        super.onDetach();
        // locationController.unregister(this);
        mListener = null;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    /**
     * LocationController listener callback methods
     */

    // if location updates have been requested and there are location changes
    @Override
    public void locationChanged(LocationController locationController)
    {
        Log.i(MAPSHOWFRAGMENTLOCATIONCALLBACKS, "locationChanged");

    }

    // callback when location controller is connected
    @Override
    public void locationControllerConnected(LocationController locationController)
    {
        Log.i(MAPSHOWFRAGMENTLOCATIONCALLBACKS, "locationControllerConnected");

        if (!locationRetrieved && getCurrentLocation())
        {
            Log.i(MAPSHOWFRAGMENTLOCATIONCALLBACKS, "LocationControllerConnected - Location retrieved");
            LatLng latlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latlng)
                    .zoom(10)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions()
                    .position(latlng));
        }


        if (locationController.getLatestAddress() == null)
        {
            locationController.queryLatestAddress();
            mProgressBar.setVisibility(View.VISIBLE);

        }

    }


    // callback when location controller is disconnected
    @Override
    public void locationControllerDisConnected(LocationController locationController)
    {
        Log.i(MAPSHOWFRAGMENTLOCATIONCALLBACKS, "locationControllerDisConnected");
    }

    // callback when address has been requested and new address has been received
    @Override
    public void addressChanged(LocationController locationController)
    {
        Log.i(MAPSHOWFRAGMENTLOCATIONCALLBACKS, "addressChanged");
        locationController.getLatestAddress();
        mProgressBar.setVisibility(View.GONE);

        if (locationController.getLatestAddress() != null)
        {
            mAddress.setText(locationController.getLatestAddress());
            mProgressBar.setVisibility(View.GONE);
            addressRetrieved=true;
        }


    }

    //Method to get location from Location controller
    private boolean getCurrentLocation()
    {
        if (!locationRetrieved)
        {
            currentLocation = app.getLocationController().getLatestLocation();
            if (currentLocation != null)
            {
                locationRetrieved=true;
                return true;

            } else
            {
                app.getLocationController().connectLocationClient();
                return false;
            }
        } else
        {
            return true;
        }

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnMapFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
