package ru.alapplications.myphoto.ui.old;

public class CacheHelper {

//
//    private static final String            CACHE_STATE_SP_FILE_NAME = "cacheState.pref";
//    private static final String            IS_EXIST_PREF            = "isExist";
//    private static final String            DATE_PREF                = "date";
//    private static       DateFormat        dateFormat;
//    private static       SharedPreferences sharedPreferences;
//    private static       Disposable        disposable;
//
//    @Inject
//    static MyPhotoDao myPhotoDao;
//
//    @Inject
//    App app;
//
//    @Inject
//    Model model;
//
//    public CacheHelper ( ) {
//        App.getAppComponent ( ).inject ( this );
//        dateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );
//        sharedPreferences = app.getSharedPreferences ( CACHE_STATE_SP_FILE_NAME , MODE_PRIVATE );
//    }
//
//    @SuppressLint("CheckResult")
//    public static void writeCache ( List<Hit> hits , int totalHits ) {
//        TotalInfo totalInfo = new TotalInfo ( totalHits );
//        SharedPreferences.Editor editor = sharedPreferences.edit ( );
//        if ( hits.size ( ) > 0 ) {
//            Single.create ( emitter -> {
//
//                emitter.onSuccess (
//                        myPhotoDao.clearDbTableAndCacheHits ( hits , totalInfo ) );
//            } )
//                    .observeOn ( Schedulers.io ( ) )
//                    .subscribeOn ( Schedulers.io ( ) )
//                    .subscribe (
//                            resultList -> {
//                                Log.d ( App.TAG , "Cache Ok, size:" + resultList.toString ( ) );
//                                editor.putBoolean ( IS_EXIST_PREF , true );
//                                editor.putString ( DATE_PREF , dateFormat.format ( new Date ( ) ) );
//                                editor.commit ( );
//                            } ,
//                            throwable -> {
//                                Log.e ( App.TAG , "Cache Error" + throwable );
//                                editor.putBoolean ( IS_EXIST_PREF , false );
//                                editor.commit ( );
//                            } );
//
//        } else {
//            editor.putBoolean ( IS_EXIST_PREF , false );
//            editor.commit ( );
//        }
//        if ( (disposable != null) && (!disposable.isDisposed ( )) ) {
//            disposable.dispose ( );
//        }
//    }
//
//    @SuppressLint("CheckResult")
//    public void loadCache ( OnLoadDataListener onLoadDataListener ) {
//        if ( sharedPreferences.contains ( IS_EXIST_PREF ) &&
//                (sharedPreferences.getBoolean ( IS_EXIST_PREF , true )) &&
//                (sharedPreferences.getString ( DATE_PREF , "" ).equals ( dateFormat.format ( new Date ( ) ) )) ) {
//
//            Single.create ( emitter -> emitter.onSuccess (
//                    myPhotoDao.getTotalAndHits ( model ) ) )
//                    .observeOn ( AndroidSchedulers.mainThread ( ) )
//                    .subscribeOn ( Schedulers.io ( ) )
//                    .subscribe (
//                            result -> {
//                                Log.d ( App.TAG , "Cache deleted" );
//
//                                onLoadDataListener.getLoadResult ( null );
//                            } ,
//                            throwable -> {
//                                Log.e ( App.TAG , "Cache Error" + throwable );
//
//                                onLoadDataListener.getLoadResult ( null );
//                            } );
//        }
//    }
//
//    public void clearCache ( OnLoadDataListener onLoadDataListener ) {
//        SharedPreferences.Editor editor = sharedPreferences.edit ( );
//        Single.create ( emitter -> emitter.onSuccess (
//                myPhotoDao.clearTotalAndHits ( ) ) )
//                .observeOn ( AndroidSchedulers.mainThread ( ) )
//                .subscribeOn ( Schedulers.io ( ) )
//                .subscribe (
//                        result -> {
//                            Log.d ( App.TAG , "Cache deleted" );
//                            editor.putBoolean ( IS_EXIST_PREF , false );
//                            editor.commit ( );
//                            onLoadDataListener.getLoadResult ( null );
//                        } ,
//                        throwable -> {
//                            Log.e ( App.TAG , "Cache Error" + throwable );
//                            editor.putBoolean ( IS_EXIST_PREF , false );
//                            editor.commit ( );
//                            onLoadDataListener.getLoadResult ( null );
//                        } );
//    }
}