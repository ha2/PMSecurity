package com.example.arpad.pmsecurity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by arpad on 2015.01.27..
 */
public class Item implements Serializable, Comparator<Item>, Comparable<Item> {
    final static int PM_ALL_FLAGS = PackageManager.GET_ACTIVITIES + PackageManager.GET_GIDS
            + PackageManager.GET_CONFIGURATIONS + PackageManager.GET_INSTRUMENTATION
            + PackageManager.GET_PERMISSIONS + PackageManager.GET_PROVIDERS
            + PackageManager.GET_RECEIVERS + PackageManager.GET_SERVICES
            + PackageManager.GET_SIGNATURES + PackageManager.GET_UNINSTALLED_PACKAGES;
    private static final String TAG = "TAG:Item: ";

    private Context mContext;
    private String mPkgName;
    private String mPkgDescription;
    private Drawable mPkgIcon;
    private boolean mVisible;
    private String mApplicationLabel;
    private PackageManager mPackageManager;


    public void setVisible( boolean visible){
        this.mVisible = visible;
    }

    public boolean getVisible(){
        return mVisible;
    }

    public String getPackageName() {
        return mPkgName;
    }

    public String getPackageDescription(){
        return mPkgDescription;
    }

    public Drawable getPkgIcon() {
        return  mPkgIcon;
    }

    public String getApplicationLabel() {
        return mApplicationLabel;
    }

    @Override
    public String toString() {
        //return super.toString();
        return new String( "Package name: " + mPkgName + "\nPackage description: " + mPkgDescription );
    }

    public Item( Context context, String packageName ) {
        this.mContext = context;
        this.mPkgName = packageName;
        this.init();
    }

    @Override
    public int compareTo(Item another) {
        //return (this.mPkgName).compareTo( another.mPkgName );
        return (this.mApplicationLabel).compareTo( another.mApplicationLabel );
    }

    @Override
    public int compare(Item item1, Item item2) {
        //return 0;
        //return item1.mPkgName.compareTo( item2.getPackageName() );
        return item1.mApplicationLabel.compareTo( item2.getApplicationLabel() );
    }

    private void init(){
        mPackageManager = mContext.getPackageManager();
        mVisible = true;
        mApplicationLabel = this.readPkgApplicationLabel();
        mPkgIcon = this.readPkgIcon();
        //Log.d(TAG, "init(): readPkgReqPerm()");

        StringBuilder sb = new StringBuilder();
        sb.append( this.readPkgName() );
        sb.append( this.readPkgRequestedPermissions() );

        mPkgDescription = sb.toString();
    }


    private String readPkgApplicationLabel(){
        //get ApplicationLabel from PM
        StringBuilder returnValue = new StringBuilder();
        //returnValue.append( mContext.getString( R.string.application_label) + ": ");

        ApplicationInfo applicationInfo;
        try {
            applicationInfo = mPackageManager.getApplicationInfo(mPkgName, PM_ALL_FLAGS);
            mApplicationLabel = String.valueOf(mPackageManager.getApplicationLabel( applicationInfo ));
            returnValue.append( mApplicationLabel );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mApplicationLabel = mContext.getString( R.string.no_application_label );
        }
        return returnValue.toString();
    }

    private Drawable readPkgIcon(){
        try {
            mPkgIcon = mPackageManager.getApplicationIcon(mPkgName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mPkgIcon;
    }

    private void setPkgDescription(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = mPackageManager.getPackageInfo( mPkgName, PM_ALL_FLAGS );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if ( packageInfo == null ){
            mPkgDescription = new String( mContext.getString(R.string.no_permission) );
        }
        String[] requestedPermissions = packageInfo.requestedPermissions;


        return;
    }

    private String readPkgRequestedPermissions(){
        StringBuilder returnValue = new StringBuilder();
        TreeSet<String> requestedPermissionsTreeSet = new TreeSet<>();
        returnValue.append( mContext.getString( R.string.requested_permissions ) + ": ");

        PackageInfo packageInfo = null;
        try {
            packageInfo = mPackageManager.getPackageInfo( mPkgName, PM_ALL_FLAGS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if( packageInfo == null ){
            returnValue.append( mContext.getString( R.string.no_permission ) );
            //return returnValue.toString();
        } else {
            String[] requestedPermissions = packageInfo.requestedPermissions;
            if ( requestedPermissions == null ){
                returnValue.append( mContext.getString( R.string.no_permission ) );
                return returnValue.toString();
            } else {
                for( String requestedPermission : requestedPermissions ){
                    requestedPermissionsTreeSet.add(requestedPermission);
                }

                for ( String s : requestedPermissionsTreeSet ){
                    returnValue.append( "\n".concat(s) );
                }
            }
        }

        return returnValue.append("\n").toString();
    }

    /**
     * @return Formázza a PkgName sorát
     */
    private String readPkgName(){
        StringBuilder returnValue = new StringBuilder();
        returnValue.append( mContext.getString( R.string.package_name ) + ": ");
        if ( (mPkgName == null) || (mPkgName.isEmpty()) ){
            returnValue.append( mContext.getString( R.string.no_package_name ));
        } else {
            returnValue.append( mPkgName );
        }
        return returnValue.append("\n").toString();
    }
}
