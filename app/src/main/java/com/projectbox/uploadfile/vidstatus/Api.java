package com.projectbox.uploadfile.vidstatus;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface Api {
    @GET("api.php?req=statuslist&statustype=videostatus")
    Call<Status> getLatestVideoStatus(@Query("page") int page);

    @GET("api.php?req=statuslist&statustype=imagestatus")
    Call<Status> getLatestImageStatus(@Query("page") int page);

    @GET("api.php?req=statuslist&statustype=textstatus")
    Call<Status> getLatestTextStatus(@Query("page") int page);

    @GET("api.php?req=popularstatus&statustype=videostatus")
    Call<Status> getTrendingVideoStatus(@Query("page") int page);

    @GET("api.php?req=popularstatus&statustype=imagestatus")
    Call<Status> getTrendingImageStatus(@Query("page") int page);

    @GET("api.php?req=popularstatus&statustype=textstatus")
    Call<Status> getTrendingTextStatus(@Query("page") int page);

    @GET("api.php?req=statuslist&statustype=videostatus")
    Call<Status> getTrendingVideoStatusByCat(@Query("page") int page,@Query("category_id") String category_id);

    @GET("api.php?req=statuslist&statustype=imagestatus")
    Call<Status> getTrendingImageStatusByCat(@Query("page") int page,@Query("category_id") String category_id);

    @GET("api.php?req=statuslist&statustype=textstatus")
    Call<Status> getTrendingTextStatusByCat(@Query("page") int page,@Query("category_id") String category_id);

    @GET("api.php?req=statuslike")
    Call<Status> setStatusLike(@Query("page") int page,@Query("category_id") String category_id);

    @GET("api.php?req=cactegory&statustype=videostatus")
    Call<CategoryModel> getVideoCatigories();

    @GET("api.php?req=cactegory&statustype=imagestatus")
    Call<CategoryModel> getImageCatigories();

    @GET("api.php?req=cactegory&statustype=textstatus")
    Call<CategoryModel> getTextCatigories();

    @GET("api.php?req=searchstatus&statustype=videostatus")
    Call<Status> getSearchResults(@Query("tag") String tag);

}