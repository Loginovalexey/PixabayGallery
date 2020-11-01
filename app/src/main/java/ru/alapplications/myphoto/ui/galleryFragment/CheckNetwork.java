package ru.alapplications.myphoto.ui.galleryFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import ru.alapplications.myphoto.app.App;

public class CheckNetwork {
    private static boolean isNetworkConnected;


    public static boolean isIsNetworkConnected ( ) {
        return isNetworkConnected;
    }

    public static void setIsNetworkConnected ( boolean isNetworkConnected ) {
        CheckNetwork.isNetworkConnected = isNetworkConnected;
    }



    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void registerNetworkCallback()
    {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance ( ).getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            {
                connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
                                                                       @Override
                                                                       public void onAvailable( Network network) {

                                                                           isNetworkConnected = true;
                                                                           Log.d(App.TAG,"isNetworkConnected = true");
                                                                       }
                                                                       @Override
                                                                       public void onLost(Network network) {
                                                                           isNetworkConnected = false;
                                                                           Log.d(App.TAG,"isNetworkConnected = false");
                                                                       }
                                                                   }

                );
            }
            isNetworkConnected = false;
        }catch (Exception e){
            isNetworkConnected = false;
        }
    }
}
