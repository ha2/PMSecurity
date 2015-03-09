package com.example.arpad.pmsecurity;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by arpad on 2015.02.23..
 */
public class PMListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "TAG:PMListFragment: ";
    private final static int PM_ALL_FLAGS = PackageManager.GET_ACTIVITIES + PackageManager.GET_GIDS
            + PackageManager.GET_CONFIGURATIONS + PackageManager.GET_INSTRUMENTATION
            + PackageManager.GET_PERMISSIONS + PackageManager.GET_PROVIDERS
            + PackageManager.GET_RECEIVERS + PackageManager.GET_SERVICES
            + PackageManager.GET_SIGNATURES + PackageManager.GET_UNINSTALLED_PACKAGES;

    private View mRootView;
    private Context mContext;
    private Adapter mAdapter;
    private List<Item> mItemList;


    private String mRegexFilter;

    private String[] mListPkgName;
    private String[] mListPkgDescription;

    void setItemList( List<Item> itemList ){
        mItemList = itemList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //View rootView;
        mRootView = inflater.inflate( R.layout.list_fragment, null, false );

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mItemList = new ArrayList<Item>();

        init();

        Collections.sort(mItemList);
        mAdapter = new Adapter(getActivity(), mItemList );
        setListAdapter( mAdapter );
        getListView().setOnItemClickListener( this );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, mItemList.get( position ).getPackageName(),  Toast.LENGTH_SHORT).show();
    }

    public void init(){
        mContext = getActivity().getApplicationContext();
        PackageManager pm = mContext.getPackageManager();

        //this.setRegexFilter( new String( mContext.getString( R.string.all_regex ) ) );
        MainActivity mainActivity = (MainActivity) getActivity();
        this.setRegexFilter( mainActivity.getRegexFilter() );

        //String regex = (MainActivity) getActivity().getRegexFilter();
        List<PackageInfo> installedPkg = pm.getInstalledPackages( PM_ALL_FLAGS );

        for( PackageInfo pkgInfo : installedPkg ){
            String pN = (pkgInfo.packageName == null ) ? new String("NULL") : pkgInfo.packageName;
            Log.d(TAG, "init(): pkgInfo: " + pkgInfo.toString() );
            Item item = new Item( mContext, pN );

//            boolean addItem = false;
//            if ( pkgInfo.permissions != null ){
//                Log.d(TAG, "init(): permissions: " + String.valueOf( pkgInfo.permissions.length ) );
//                PermissionInfo[] permissionInfos = pkgInfo.permissions;
//                Log.d(TAG, "init(): permissionInfos: " + permissionInfos.length );
//                //for( PermissionInfo permissionInfo : permissionInfos ){
//                for( int i = 0; i < permissionInfos.length; i++ ){
//                    String group = permissionInfos[i].group;
//                    if ( group != null ){
//                        if ( group.matches( mRegexFilter ) ){
//                            Log.d( TAG, "init(): group.matches( mRegexFilter ): " + group +  ".matches( \"" + mRegexFilter + "\" );");
//                            addItem = true;
//                        }
//                    };
//                }
//            }
//            if ( addItem ){
//                mItemList.add( item );
//            }
//            PermissionInfo[] permissionInfos = pkgInfo.permissions;
//            if ( permissionInfos == null ) continue;
//            Log.d(TAG, "init(): permissionInfos: " + permissionInfos.toString() );
//
//            for (PermissionInfo permissionInfo : permissionInfos) {
//                Log.d(TAG, "init(): permissionInfo: " + permissionInfo.group );
//                String group = permissionInfo.group;
//                if ( group != null ){
//                    if ( group.matches( ".*" ) ){
//
//            //            mItemList.add(item);
//                    }
//                }
//                //Log.d(TAG, "init(): group: " + group.isEmpty() );
//                //if ( group.matches( mRegexFilter ) ){
//
//            }

//            StringBuilder sb = new StringBuilder("###");
//            sb.append( mListPkgDescription.toString() ).append( mListPkgName.toString() );
//            if( sb.toString().toLowerCase().matches(mRegexFilter.toLowerCase()) ){
//                item.setVisible( true );
//            } else {
//                item.setVisible( false );
//            }
//            if( item.getVisible() ) {
                mItemList.add(item);
//            }
        }

        this.setNumberOfPackages(mItemList.size());

        //readPermissionGroupsList();
        prepareRegexFilterSpinner();
    }

//    public List<String> readPermissionGroupsList(){
//        List<String> permissionGroupList = new ArrayList<String>();
//
//        PermissionGroupInfo permissionGroupInfo;
//        List<PermissionGroupInfo> permissionGroupInfoList = mContext.getPackageManager().getAllPermissionGroups( PM_ALL_FLAGS );
//
//        for( PermissionGroupInfo pgi : permissionGroupInfoList ){
//            Log.d(TAG, "readPermissionGroupsList(): "
//                    + String.valueOf( permissionGroupInfoList.size() )
//                    + "; " + String.valueOf(pgi.loadDescription(mContext.getPackageManager()))  //magyar mondat
//                    //+ "; " + pgi.toString()                                                         //android.permission-group.*
//                    + "; " + pgi.toString().split("\\.")[2].split("\\s+")[0]
//            );
//            String pgi_short = pgi.toString().split("\\.")[2].split("\\s+")[0];
//            permissionGroupList.add(pgi_short);
//        }
//        permissionGroupList.add( mContext.getString( R.string.all ) );
//        return permissionGroupList;
//    }

    public String getRegexFilter() {
        Log.d(TAG, "getRegexFilter(): " + mRegexFilter);
        return mRegexFilter;
    }

    public void setRegexFilterFromActivity(){
        MainActivity mainActivity = (MainActivity) getActivity();
        this.setRegexFilter( mainActivity.getRegexFilter() );
        return;
    }

    public void setRegexFilter(String regexFilter) {
        Log.d(TAG, "setRegexFilter(): " + regexFilter);
        this.mRegexFilter = regexFilter;
    }

    public void updateList(){
        Log.d( TAG, "updateList(): start");
        this.setRegexFilterFromActivity();
        // visszafelé kell törölni, mert menetközben változik a lista mérete
        for ( int i = mAdapter.getCount()-1; i >= 0; i-- ){
            Item item = (Item) mAdapter.getItem( i );
            if ( item.getApplicationLabel().toLowerCase().matches( mRegexFilter.toLowerCase() ) != true ){
            //if ( item.getApplicationLabel().contains( mRegexFilter ) != true ){
                //mAdapter.removeItem( i );
                Log.d( TAG, "updateList(): töröljük ami nem egyezik: " + item.getPackageDescription() );
            }
        }
        getAdapter().notifyDataSetChanged();
        Log.d( TAG, "updateList(): finish");
        return;
    }

    public Adapter getAdapter(){
        return mAdapter;
    }

    private void setNumberOfPackages( int numberOfPackages ){
        Log.d(TAG, "setNumberOfPackages(): " + numberOfPackages );
        TextView tvSumma = (TextView) mRootView.findViewById( R.id.tvSumma );
        tvSumma.setText(getString( R.string.number_of_packages ) + ": " + String.valueOf( numberOfPackages ) );

    }



    private void prepareRegexFilterSpinner(){
        mRegexFilter = new String(".*");

        final Spinner spinnerPermissionGroup = (Spinner) mRootView.findViewById(R.id.spinner_permission_group);

        final ArrayAdapter<String> arrayAdapter;
        List<String> pgList = readPermissionGroupsList();
        if ( (pgList == null) || (pgList.size() == 0)){
            Log.d(TAG, "prepareRegexFilterSpinner(): NULL a lista hossza!");
        }

        arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, readPermissionGroupsList());
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinnerPermissionGroup.setAdapter( arrayAdapter );
        spinnerPermissionGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setRegexFilter( (String) parent.getItemAtPosition( position ) );
                Log.d(TAG, "onItemSelected(): változik az mRegexFilter");

//                Fragment fragment = getFragmentManager().findFragmentById( R.layout.list_fragment );
//                Bundle fragmentArguments = new Bundle();
//                fragmentArguments.putString( REGEX, mRegexFilter );
//                fragment.setArguments( fragmentArguments );
//                arrayAdapter.notifyDataSetChanged();

                Toast.makeText( mContext, "RegexFilter: " + mRegexFilter, Toast.LENGTH_LONG).show();

                FragmentManager fm = getFragmentManager();
                if ( fm == null ){
                    Log.d( TAG, "onItemSelected(): fm == null");
                }

                PMListFragment fragment = (PMListFragment) fm.findFragmentById( R.id.fragment1 );
                if (fragment != null ){
                    fragment.updateList();
                } else {
                    Log.d( TAG, "onItemSelected(): fragment==null");
                }

//                ((Adapter) fragment.getListAdapter()).notifyDataSetChanged();
                //(Adapter) getListAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return;
    }


    public List<String> readPermissionGroupsList(){
        List<String> permissionGroupList = new ArrayList<String>();

//        PermissionGroupInfo permissionGroupInfo;
        List<PermissionGroupInfo> permissionGroupInfoList = mContext
                .getPackageManager()
                .getAllPermissionGroups( PM_ALL_FLAGS );

        for( PermissionGroupInfo permissionGroupInfo : permissionGroupInfoList ){
            Log.d(TAG, "readPermissionGroupsList(): "
                            + String.valueOf( permissionGroupInfoList.size() )
                            + "; " + String.valueOf(permissionGroupInfo.loadDescription( mContext.getPackageManager()))  //magyar mondat
                            //+ "; " + permissionGroupInfo.toString()                                                         //android.permission-group.*
                            + "; " + permissionGroupInfo.toString().split("\\.")[2].split("\\s+")[0]
            );
            //String pgi_short = permissionGroupInfo.toString().split("\\.")[2].split("\\s+")[0];
            String pgi_short = permissionGroupInfo.toString().split("\\s+")[1];
            permissionGroupList.add(pgi_short);
        }
        Collections.sort( permissionGroupList );
        permissionGroupList.add(0, mContext.getString(R.string.all));
        return permissionGroupList;
    }

//    private void preparePermissionGroupInfoSpinner(){
//        Spinner permissionGroupInfoSpinner = (Spinner) mRootView.findViewById( R.id.spinner_permission_group );
//        permissionGroupInfoSpinner.set
//        return;
//    }
}
