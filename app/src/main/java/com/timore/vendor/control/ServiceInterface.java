package com.timore.vendor.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.timore.vendor.beanBojo.Comment;
import com.timore.vendor.beanBojo.Notif;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.beanBojo.Profile;
import com.timore.vendor.beanBojo.User;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by ZEID on 7/14/2015.
 */
public interface ServiceInterface {

    /*post is send as ajson */
    /*
    @POST("/service?")
    void signup(@Body Client cl, Callback<Signup> cb);
*/

    /*USERS*/
    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
    @FormUrlEncoded
    @POST("/users/login")
    void login(@Field("username") String username, @Field("password") String pass, Callback<JsonArray> cb);

    @FormUrlEncoded
    @POST("/users/register/")
    void register(@Field("rocsel") String one, @Field("username") String username, @Field("email") String email, @Field("password") String pass, Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/users/update")
    void updateAccount(@Field("rocsel") int rocsel, @Field("username") String username, @Field("mobile") String mobile,
                       @Field("image") String image, @Field("id") long id, @Field("about") String about,
                       @Field("email") String email, @Field("latitude") double latitude, @Field("longitude") double longitude,
                       @Field("whatsapp") String whatsapp, @Field("facebook") String facebook, @Field("twitter") String twitter,
                       @Field("instagram") String instagram, @Field("hashtag") String hashtag, @Field("site") String site,
                       Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/users/account")
    void getAccountData(@Field("id") long id, @Field("user_id") long user_id, Callback<User> cb);

    @FormUrlEncoded
    @POST("/users/password")
    void updateUserPassword(@Field("user_id") long user_id, @Field("oldpassword") String oldpassword, @Field("password") String password,
                            Callback<JsonObject> cb);


    @FormUrlEncoded
    @POST("/users/search")
    void searchInUsers(@Field("user_id") long user_id, @Field("username") String username, @Field("mobile") String mobile,
                       @Field("hashtag") String hashtag, @Field("email") String email, Callback<List<User>> cb);

    @FormUrlEncoded
    @POST("/users/forget_password")
    void forgetPass(@Field("rocsel") long rocs, @Field("email") String email, Callback<JsonObject> cb);


    @FormUrlEncoded
    @POST("/users/get")
    void getAllUsers(@Field("user_id") long user_id, Callback<List<User>> callback);


    /* POSTS */
    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

    @FormUrlEncoded
    @POST("/posts/get")
    void getPosts(@Field("user_id") long userId, Callback<List<Post>> cb);

    @FormUrlEncoded
    @POST("/posts/insert")
    void addPost(@Field("rocsel") int one, @Field("user_id") long userId, @Field("title") String title
            , @Field("file") File file, @Field("file_type") String file_type, @Field("content") String content, Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/posts/insert")
    void addPost(@Field("rocsel") int one, @Field("user_id") long userId, @Field("title") String title
            , @Field("file") String file, @Field("file_type") String file_type, @Field("content") String content, Callback<JsonObject> cb);


    @FormUrlEncoded
    @POST("/posts/update")
    void updatePost(@Field("rocsel") int one, @Field("id") long id, @Field("user_id") long uid, @Field("title") String title
            , @Field("file") String file, @Field("content") String content, Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/posts/delete")
    void deletePost(@Field("rocsel") long one, @Field("id") long id, @Field("user_id") long userId
            , Callback<JsonObject> cb);

    /*HASH TAG*/
    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

    @FormUrlEncoded
    @POST("/hashtag/get_all")
    void getAllHashTag(Callback<List<Post>> cb);

    @FormUrlEncoded
    @POST("/hashtag/get_all")
    void getHashTag(@Field("parent") long parent, Callback<List<Post>> cb);

    @FormUrlEncoded
    @POST("/hashtag/insert")
    void addHashTag(@Field("rocsel") long rocsel, @Field("parent") long parent, @Field("tag") long tag, Callback<List<Post>> cb);

    @FormUrlEncoded
    @POST("/hashtag/delete")
    void deleteHashTag(@Field("id") long id, Callback<List<Post>> cb);

    /*FOLLOWING*/
    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

    @FormUrlEncoded
    @POST("/follow/insert")
    void followUser(@Field("rocsel") long one, @Field("user_id") long userId, @Field("follower") long followerId, Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/follow/unfollow")
    void unFollowUser(@Field("user_id") long userId, @Field("follower") long followerId, Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/follow/followers")
    void getAllFollowers(@Field("user_id") long userId, Callback<List<Post>> cb);

    @FormUrlEncoded
    @POST("/follow/following")
    void getAllFollowing(@Field("user_id") long userId, Callback<List<Post>> cb);

    /*LIKES*/
    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

    @FormUrlEncoded
    @POST("/notification/like")
    void likePost(@Field("rocsel") long rocsel, @Field("user_id") long userId, @Field("parent") long postId,
                  Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/notification/unlike")
    void unLikePost(@Field("rocsel") long rocsel, @Field("user_id") long userId, @Field("parent") long parent,
                    Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/profile/like_check")
    void getUserPosts(@Field("user_id") long userId, @Field("post_id") long post_id, Callback<List<Post>> cb);

    @FormUrlEncoded
    @POST("/profile/like_count")
    void like_count(@Field("parent") long parent, Callback<List<Post>> cb);
    /*COMMENTS*/
    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

    @FormUrlEncoded
    @POST("/notification/comment")
    void comment(@Field("rocsel") long rocsel, @Field("user_id") long userId, @Field("parent") long parent,
                 @Field("content ") String content, Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/notification/comment_update")
    void updateComment(@Field("id") long id, Callback<List<Post>> cb);

    @FormUrlEncoded
    @POST("/notification/comment_delete")
    void deleteComment(@Field("rocsel") long rocsel, @Field("id") long id, Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/notification/comment_get")
    void getPostComments(@Field("rocsel") long rocsel, @Field("parent") long parent,
                         Callback<List<Comment>> cb);

/*
    @FormUrlEncoded
    @POST("/notification/get")
    void getNotification(@Field("user_id") long user_id, Callback<JsonArray> cb);
*/

    @FormUrlEncoded
    @POST("/notification/get")
    void getNotification(@Field("user_id") long user_id, Callback<List<Notif>> cb);

    /*PROFILE*/
    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/


    @FormUrlEncoded
    @POST("/profile/get")
    void getUserProfile(@Field("user_id") long userid, @Field("user") long myid, Callback<Profile> cb);


    @FormUrlEncoded
    @POST("/profile/posts")
    void getUserPosts(@Field("user_id") long userId, Callback<List<Post>> cb);
    /*NOTIFICATION*/
    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

    @FormUrlEncoded
    @POST("/notification/report")
    void reportPost(@Field("rocsel") long rocsel, @Field("user_id") long userId, @Field("parent") long parent,
                    @Field("content") String content, Callback<JsonObject> cb);

    @FormUrlEncoded
    @POST("/upload")
    void uploadImage(@Field("image") String image, Callback<JsonObject> cb);

}
