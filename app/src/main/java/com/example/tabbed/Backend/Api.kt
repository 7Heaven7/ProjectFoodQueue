package com.example.tabbed.Backend

import com.example.tabbed.Response.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("getMenus")
    fun getMenusAPI(
    ): Call<GetMenuResponse>

    @GET("getMenus2?")
    fun getMenusAPI2(
        @Query("type") type: String
    ): Call<GetMenuResponse>

    @FormUrlEncoded
    @POST("orderMenu")
    fun orderMenuAPI( //post value
        @Field("id_menu") id_menu: Int,
        @Field("id_usertable") id_usertable: Int,
        @Field("quantity") quantity: Int
    ): Call<DefaultResponse> //response data

    @FormUrlEncoded
    @POST("orderMenu2")
    fun orderMenuAPI2( //post value
        //@Field("id_menu") id_menu: List<Int>,
        //@Field("id_usertable") id_usertable: List<Int>,
        //@Field("quantity") quantity: List<Int>,
        @FieldMap(encoded = true) orderlist: Map<String, Int>
    ): Call<DefaultResponse> //response data

    @GET("customerGetOrder?")
    fun customerGetOrderAPI(
        @Query("id_usertable") id_usertable: Int
    ): Call<CustomerQueueResponse>

    @FormUrlEncoded
    @PUT("customerPendingBill")
    fun customerPendingBillAPI(
        @Field("id_usertable") id_usertable: Int
    ):Call<DefaultResponse>

    @GET("customerGetBill?")
    fun customerGetBillAPI(
        @Query("id_usertable") id_usertable: Int
    ): Call<UserBillResponse>

    @FormUrlEncoded
    @POST("customerlogin")
    fun userLoginAPI(
        @Field("user") user: String,
        @Field("password") password: String
    ):Call<LoginResponse>

    ////////////////////////////// EMPLOYEE //////////////////////////////

    @FormUrlEncoded
    @POST("adminlogin")
    fun adminLoginAPI(
        @Field("admin") admin_name: String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @GET("adminGetOrder")
    fun adminGetOrderAPI(
    ):Call<AdminGetOrderResponse>

    @FormUrlEncoded
    @PUT("adminUpdateOrder/{id_orderlist}")
    fun adminUpdateOrderAPI(
        @Path("id_orderlist") id_orderlist: Int,
        @Field("menu_status") menu_status: String
    ):Call<DefaultResponse>

    @GET("adminGetBill")
    fun adminGetBillAPI(
    ):Call<AdminBillResponse>

    @FormUrlEncoded
    @PUT("cancelOrder/{id_orderlist}")
    fun cancelOrderAPI(
        @Path("id_orderlist") id_orderlist: Int,
        @Field("menu_status") menu_status: String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @PUT("adminUpdateBill/{id_bill}")
    fun adminUpdateBillAPI(
        @Path("id_bill") id_bill: Int,
        @Field("bill_status") bill_status: String
    ):Call<DefaultResponse>

    @GET("waiterMonitorCustomerQueue")
    fun adminGetCountOrderAPI(
    ):Call<AdminGetCountResponse>

    ////////////////////////////// MENU //////////////////////////////

    @FormUrlEncoded
    @POST("addMenu")
    fun addMenuAPI( //post value
        @Field("menu_name") menu_name: String,
        @Field("image_file") image_file: String,
        @Field("type") type: String,
        @Field("price") price: String
    ): Call<DefaultResponse> //response data

    @FormUrlEncoded
    @PUT("updateMenu/{id_menu}")
    fun updateMenuAPI(
        @Path("id_menu") id_menu: Int,
        @Field("menu_name") menu_name: String,
        @Field("image_file") image_file: String,
        @Field("type") type: String,
        @Field("price") price: String
    ): Call<DefaultResponse>

    @DELETE("deleteMenu/{id_menu}")
    fun deleteMenuAPI(
        @Path("id_menu") id_menu: Int
    ): Call<DefaultResponse>
}