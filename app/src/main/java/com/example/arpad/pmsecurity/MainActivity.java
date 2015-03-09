package com.example.arpad.pmsecurity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends ActionBarActivity {
    private static final int OPTIONSMENU_FILTER = 1013;
    private static final String TAG = "TAG.MainActivity()";
    private final static int PM_ALL_FLAGS = PackageManager.GET_ACTIVITIES + PackageManager.GET_GIDS
            + PackageManager.GET_CONFIGURATIONS + PackageManager.GET_INSTRUMENTATION
            + PackageManager.GET_PERMISSIONS + PackageManager.GET_PROVIDERS
            + PackageManager.GET_RECEIVERS + PackageManager.GET_SERVICES
            + PackageManager.GET_SIGNATURES + PackageManager.GET_UNINSTALLED_PACKAGES;
    private static final String REGEX = "REGEX";

    private int pkgNumber;
    private PackageManager pm;
    private List<PackageInfo> listPkgInfo;
    private List<PermissionGroupInfo> listPermissionGroupInfo;
    private List<String> mListGroupPgi = new ArrayList<>();
    private List<String> mListLoadLabelPgi = new ArrayList<>();
    private Set<String> mSetGroupPgi;
    private List<Item> mListItem;


    private String mRegexFilter;// = new String(".*");


    private static TextView tvPkgList;
    private int pgiNumber;
    private static ListView lvPackages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add("elso");
        menu.add(0, OPTIONSMENU_FILTER, 0, getResources().getString( R.string.filter ) );
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if( id == OPTIONSMENU_FILTER ){
            Log.d(TAG, "onOptionsItemSelected()");
            //new OptionsFilterFragment().show(getFragmentManager(),OptionsFilterFragment.TAG);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init(){
        mListItem = new ArrayList<Item>();

        pm = getPackageManager();

        listPkgInfo = pm.getInstalledPackages( PM_ALL_FLAGS );
        pkgNumber = listPkgInfo.size();


        TreeSet<String> permissionsTreeSet = new TreeSet<String>(){};

        for( PackageInfo packageInfo : listPkgInfo ){
            if ( packageInfo.requestedPermissions != null ){
                for( String permission : packageInfo.requestedPermissions ){
                    permissionsTreeSet.add( permission );
                    Log.d(TAG, "init(): add permission: " + permission );
                };
            } else {
                permissionsTreeSet.add("NULL");
            }
        }


        for(int i = 0; i < listPkgInfo.size(); i++){
            Item item2 = new Item( getApplicationContext(), listPkgInfo.get(i).packageName );
            mListItem.add(item2);
            Log.d( TAG, "init(): 137.sor");
        }



//        pm = getPackageManager();
//
//        listPkgInfo = pm.getInstalledPackages( PM_ALL_FLAGS );
//        pkgNumber = listPkgInfo.size();
//
//        listPermissionGroupInfo = pm.getAllPermissionGroups( pm.GET_META_DATA );
//        pgiNumber = listPermissionGroupInfo.size();
//        tvPkgList = (TextView) findViewById( R.id.tvPkgList );
//        //tvPkgList.setText( "Csomagok száma: " + pkgNumber + "\n" + listPkgInfo.toString());
//
//        int rowNumber = 0;
//        StringBuilder sbPkgInfo = new StringBuilder();
//        for (PackageInfo packageInfo : listPkgInfo){
//            sbPkgInfo.append( "\n========== " + String.valueOf(rowNumber++) + ". ==========" );
//            sbPkgInfo.append( "\n" + packageInfo.packageName + ":" );
//            PermissionInfo[] permissionInfos = packageInfo.permissions;
//            if ( permissionInfos != null ){
//                for( PermissionInfo permissionInfo : permissionInfos ){
//                    PackageManager pm = getPackageManager();
//                    sbPkgInfo.append( "\n\t- LoadLabel(): " + permissionInfo.loadLabel( pm ) );
//                    sbPkgInfo.append( "\n\t- LoadDescription(): " + permissionInfo.loadDescription(pm) );
//                    sbPkgInfo.append( "\n\t- Group: " + permissionInfo.group);
//                    sbPkgInfo.append( "\n\t- Name: " + permissionInfo.name );
//                    sbPkgInfo.append( "\n\t- PackageName: " + permissionInfo.packageName );
//                    sbPkgInfo.append( "\n\t- NonLocalizedDescription: " + permissionInfo.nonLocalizedDescription );
//                    sbPkgInfo.append( "\n\t- DescribeContents(): " + String.valueOf( permissionInfo.describeContents() ));
//                    sbPkgInfo.append( "\n\t- DescriptionRes: " + String.valueOf( permissionInfo.descriptionRes ) );
//                    sbPkgInfo.append( "\n\t- Flags: " + String.valueOf( permissionInfo.flags ) );
//                    sbPkgInfo.append( "\n\t- ProtectionLevel: " + String.valueOf( permissionInfo.protectionLevel ) );
//                    sbPkgInfo.append( "\n\t- LabelRes: " + String.valueOf( permissionInfo.labelRes ) );
//                    if ( !mListGroupPgi.contains( permissionInfo.group ) )
//                        mListGroupPgi.add( permissionInfo.group );
//                    if ( !mListLoadLabelPgi.contains( permissionInfo.loadLabel(pm) ))
//                        mListLoadLabelPgi.add( permissionInfo.loadLabel(pm).toString());
//                    //mSetGroupPgi.add( permissionInfo.group.toString() );
//                }
//
//
//            }
////            String[] requestedPermissions = packageInfo.requestedPermissions;
////            for( String req : requestedPermissions){
////                sbPkgInfo.append( "\n - " + "req.toString()" );
////            }
//            sbPkgInfo.append( "\n=========================" );
//        }
//
//        StringBuilder sbPGI = new StringBuilder();
//        for (PermissionGroupInfo pgi : listPermissionGroupInfo){
//            sbPGI.append( pgi.loadDescription(pm).toString()).append( "\n - " );
//        }
//        tvPkgList.setText( "Csomagok száma: " + pkgNumber +
//                "\n--8<---8<---8<---8<---8<---8<---8<---8<---8<---\nmListGroupPgi(" + mListGroupPgi.size() + "): " + mListGroupPgi.toString() +
//                "\n--8<---8<---8<---8<---8<---8<---8<---8<---8<---\nmListLoadLabelPgi(" + mListLoadLabelPgi.size() + "): " + mListLoadLabelPgi.toString() +
//                "\n--8<---8<---8<---8<---8<---8<---8<---8<---8<---\nPGI Number: " + pgiNumber +
//                "\n--8<---8<---8<---8<---8<---8<---8<---8<---8<---\nPermission Group Info:\n" + sbPGI +
//                "\n--8<---8<---8<---8<---8<---8<---8<---8<---8<---\nPackage Info:\n" + sbPkgInfo );

        //prepareRegexFilterSpinner();
    }

    public void broadcasts_list(){
        //TODO: broadcastokat kiolvasni a PM-en keresztul
        return;
    }

    public List<Item> getListItem(){ return mListItem; };

    public void filterDialog(){
    }

    public void reloadPackageManager(){

        List<PackageInfo> pkgInfos = pm.getInstalledPackages( PM_ALL_FLAGS );
        for( PackageInfo pi : pkgInfos ){
            if( pi.packageName.matches(mRegexFilter) == false )
                pkgInfos.remove( pi );
        }

    }



    public String getRegexFilter() { return mRegexFilter; }
    public void setRegexFilter(String RegexFilter) {
        //this.mRegexFilter = RegexFilter;
        String oldRegexFilter = mRegexFilter;
        this.mRegexFilter =
                RegexFilter.compareTo( getApplicationContext().getString( R.string.all )) == 0
                        ? getApplicationContext().getString( R.string.all_regex )
                        : RegexFilter;
        Log.d( TAG, "setRegexFilter(): " + oldRegexFilter + "->" + mRegexFilter );

    }


}
