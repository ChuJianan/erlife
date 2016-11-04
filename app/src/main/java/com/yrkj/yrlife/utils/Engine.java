//package com.yrkj.yrlife.utils;
//
//import com.yrkj.yrlife.model.BannerModel;
//import com.yrkj.yrlife.model.RefreshModel;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.http.GET;
//import retrofit2.http.Path;
//import retrofit2.http.Url;
//
///**
// * Created by cjn on 2016/11/4.
// */
//
//public interface  Engine {
//    @GET("{itemCount}item.json")
//    Call<BannerModel> fetchItemsWithItemCount(@Path("itemCount") int itemCount);
//
//    @GET
//    Call<List<RefreshModel>> loadContentData(@Url String url);
//}
