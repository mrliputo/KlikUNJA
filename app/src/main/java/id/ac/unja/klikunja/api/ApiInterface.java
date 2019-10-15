package id.ac.unja.klikunja.api;

import java.util.List;

import id.ac.unja.klikunja.models.News;
import id.ac.unja.klikunja.models.Notices;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("wp-json/wp/v2/posts")
    Call<List<News>> getPostInfo(
            @Query("categories") String categories,
            @Query("_embed") String embed,
            @Query("per_page") int per_page,
            @Query("page") int page
    );

    @GET("wp-json/wp/v2/posts")
    Call<List<News>> getPostSearch(
            @Query("categories") String categories,
            @Query("_embed") String embed,
            @Query("search") String keyword,
            @Query("per_page") int per_page,
            @Query("page") int page
    );

    @GET("notice/feed/json")
    Call<Notices> getNotices(
            @Query("paged") int paged
    );
}
