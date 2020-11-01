package ru.alapplications.myphoto.ui.galleryFragment.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConnectionStateMonitor extends LiveData<Boolean> {

    private Context                             mContext;
    private ConnectivityManager.NetworkCallback networkCallback = null;
    private NetworkReceiver                     networkReceiver;
    private ConnectivityManager                 connectivityManager;

    public ConnectionStateMonitor ( Context context ) {
        mContext = context;
        connectivityManager = ( ConnectivityManager ) mContext.getSystemService ( Context.CONNECTIVITY_SERVICE );
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            networkCallback = new NetworkCallback ( this );
        } else {
            networkReceiver = new NetworkReceiver ( );
        }
    }

    @Override
    protected void onActive ( ) {
        super.onActive ( );
        updateConnection ( );
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
            connectivityManager.registerDefaultNetworkCallback ( networkCallback );

        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            NetworkRequest networkRequest = new NetworkRequest.Builder ( )
                    .addTransportType ( NetworkCapabilities.TRANSPORT_CELLULAR )
                    .addTransportType ( NetworkCapabilities.TRANSPORT_WIFI )
                    .build ( );
            connectivityManager.registerNetworkCallback ( networkRequest , networkCallback );
        } else {
            mContext.registerReceiver ( networkReceiver , new IntentFilter ( ConnectivityManager.CONNECTIVITY_ACTION ) );
        }
    }

    @Override
    protected void onInactive ( ) {
        super.onInactive ( );
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            connectivityManager.unregisterNetworkCallback ( networkCallback );
        } else {
            mContext.unregisterReceiver ( networkReceiver );
        }
    }

    public static boolean isOnline ( Context context ) {
        ConnectivityManager connectivityManager = ( ConnectivityManager ) context.getSystemService ( Context.CONNECTIVITY_SERVICE );

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo ( );

        if ( networkInfo != null && networkInfo.isConnected ( ) )
            return true;
        else
            return false;
    }

    public Single<Boolean> hasConnection ( ) {
        return Single.fromCallable ( ( ) -> {
            if (InetAddress.getByName("www.google.com").isReachable(1000))
                return true;
            else
                return false;
//            try {
//
//                int timeoutMs = 1000;
//                Socket socket = new Socket ( );
//                SocketAddress socketAddress = new InetSocketAddress ( "8.8.8.8",53);
//                socket.connect ( socketAddress , timeoutMs );
//                socket.close ( );
//                return true;
//            } catch (IOException e) {
//                return false;
//            }
        } );
    }

    class NetworkCallback extends ConnectivityManager.NetworkCallback {

        private ConnectionStateMonitor mConnectionStateMonitor;

        public NetworkCallback ( ConnectionStateMonitor connectionStateMonitor ) {
            mConnectionStateMonitor = connectionStateMonitor;
        }

        @Override
        public void onAvailable ( Network network ) {
            if ( network != null ) {
                mConnectionStateMonitor.postValue ( true );
            }
        }

        @Override
        public void onLost ( Network network ) {
            mConnectionStateMonitor.postValue ( false );
        }
    }

    private void updateConnection ( ) {
        if ( connectivityManager != null ) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo ( );
            if ( activeNetwork != null && activeNetwork.isConnectedOrConnecting ( ) ) {
                postValue ( true );
            } else {
                postValue ( false );
            }
        }

    }

    class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive ( Context context , Intent intent ) {
            if ( intent.getAction ( ).equals ( ConnectivityManager.CONNECTIVITY_ACTION ) ) {
                updateConnection ( );
            }
        }
    }
}
