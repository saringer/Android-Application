package de.fu_berlin.agdb.notifications.notification_fragments;

/**
 * Created by Riva on 15.05.2016.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.mapbox.geocoder.service.models.GeocoderFeature;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import de.fu_berlin.agdb.server_requests.LocationService;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.adapters.GeocoderAdapter;
import de.fu_berlin.agdb.data.Constants;
import de.fu_berlin.agdb.data.LocationData;

public class MapFragment extends Fragment implements View.OnClickListener {
    private MapView mapView;
    public MapboxMap mapboxMap;
    private AutoCompleteTextView autocomplete;
    private Button continueButton;
    private LocationSelectedListener mCallback;
    private GeocoderFeature result;
    private LocationData locationData;
    private LocationService locationService;
    private Bundle savedInstanceState;
    private int alertTypeFlag;

    public int getAlertTypeFlag() {
        return alertTypeFlag;
    }

    public void setAlertTypeFlag(int alertTypeFlag) {
        this.alertTypeFlag = alertTypeFlag;
    }

    // Parent Activity must implement this interface
    // in order comnunicate between parent activity and Fragment
    public interface LocationSelectedListener {
        public void onLocationSelected(LocationData result, int alertTypeFlag);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActionBar().setTitle(" Pick Location");
        getActionBar().setIcon(
                new IconDrawable(getActivity(), FontAwesomeIcons.fa_map)
                        .colorRes(R.color.actionBarColor)
                        .actionBarSize());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        this.savedInstanceState = savedInstanceState;
        // Set Up Custom Fragment Toolbar
        setHasOptionsMenu(true);


        // Custom adapter
        final GeocoderAdapter adapter = new GeocoderAdapter(getContext());
        autocomplete = (AutoCompleteTextView) v.findViewById(R.id.query);
        autocomplete.setLines(1);
        autocomplete.setAdapter(adapter);
        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                result = adapter.getItem(position);
                autocomplete.setText(result.getPlaceName());
                updateMap(mapboxMap, result.getLatitude(), result.getLongitude());
                // pass lat & long to locationData
                if (locationData == null) {
                    locationData = new LocationData(result.getPlaceName(), result.getLatitude(), result.getLongitude());
                } else {
                    locationData.setLatitude(result.getLatitude());
                    locationData.setLongitude(result.getLongitude());
                    locationData.setPlaceDescription(result.getPlaceName());
                }
            }
        });

        // Add clear button to autocomplete
        final Drawable imgClearButton = getResources().getDrawable(R.drawable.my_location_stale);
        autocomplete.setCompoundDrawablesWithIntrinsicBounds(null, null, imgClearButton, null);
        autocomplete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AutoCompleteTextView et = (AutoCompleteTextView) v;
                if (et.getCompoundDrawables()[2] == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgClearButton.getIntrinsicWidth()) {
                    autocomplete.setText("");
                }
                return false;
            }
        });


        //Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;


        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.setAccessToken(Constants.MAPBOX_ACCESS_TOKEN);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                getMapView(mapboxMap);
            }
        });


        locationService = new LocationService(
                getActivity());

        continueButton = (Button) v.findViewById(R.id.bContinue);
        continueButton.setOnClickListener(this);


        return v;
    }

    private void initMapView() {
        if (mapboxMap == null) {

            mapView.setAccessToken(Constants.MAPBOX_ACCESS_TOKEN);
            mapView.setStyleUrl(Style.MAPBOX_STREETS);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    getMapView(mapboxMap);
                }
            });

        }
    }

    private void updateMap(MapboxMap mapboxMap, double latitude, double longitude) {

        // Marker
        mapboxMap.removeAnnotations();
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Geocoder result"));

        // Animate map
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(13)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);
    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled or currently not available! Do you want to go to the settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_current_location:
                Location nwLocation = locationService
                        .getLocation(LocationManager.NETWORK_PROVIDER, getContext());

                if (nwLocation != null) {
                    double latitude = nwLocation.getLatitude();
                    double longitude = nwLocation.getLongitude();

                    // check if the map is already initialized
                    if (mapboxMap != null) {
                        updateMap(mapboxMap, latitude, longitude);
                        // set text
                        autocomplete.setText("My Current Location");
                        // pass lat & long to locationData
                        if (locationData == null) {
                            locationData = new LocationData("My Current Location", latitude, longitude);
                        } else {
                            locationData.setLatitude(latitude);
                            locationData.setLongitude(longitude);
                            locationData.setPlaceDescription("My Current Location");
                        }
                    }


                } else {
                    showSettingsAlert("NETWORK");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Add the mapView lifecycle to the fragment's lifecycle methods


    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }

        getActionBar().setTitle(" Pick Location");
        getActionBar().setIcon(
                new IconDrawable(getActivity(), FontAwesomeIcons.fa_map)
                        .colorRes(R.color.actionBarColor)
                        .actionBarSize());
        //super.onResume();
        //mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
        //super.onPause();
        //mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
        //super.onLowMemory();
        //mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
        //super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
        //super.onSaveInstanceState(outState);
        //mapView.onSaveInstanceState(outState);
    }

    protected void getMapView(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bContinue:


                if (locationData != null) {


                    // Send selected location towards parent activity
                    mCallback.onLocationSelected(locationData, getAlertTypeFlag());
                    break;
                } else {
                    Toast.makeText(getActivity(), "You need to select a Location before you continue", Toast.LENGTH_SHORT).show();
                }

            default:

                break;


        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (LocationSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LocationSelectedListener");
        }
    }

}
