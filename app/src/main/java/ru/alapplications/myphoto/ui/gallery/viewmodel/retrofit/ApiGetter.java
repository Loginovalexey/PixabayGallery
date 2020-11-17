package ru.alapplications.myphoto.ui.gallery.viewmodel.retrofit;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.alapplications.myphoto.model.entities.ServerResponse;

/**
 * Интерфейс, определяющий запрос к веб-серверу
 */

public interface ApiGetter {
    @GET("?key=16223926-10798aa5252eec5cd7fbb1bdc")
    Single<ServerResponse> getJson ( @Query("q") String q ,
                                     @Query("lang") String lang ,
                                     @Query("image_type") String imageType ,
                                     @Query("orientation") String orientation ,
                                     @Query("category") String category ,
                                     @Query("colors") String colors ,
                                     @Query("editors_choice") boolean editorsChoice ,
                                     @Query("safesearch") boolean safeSearch ,
                                     @Query("order") String order ,
                                     @Query("page") int page ,
                                     @Query("per_page") int per_page
    );
}
