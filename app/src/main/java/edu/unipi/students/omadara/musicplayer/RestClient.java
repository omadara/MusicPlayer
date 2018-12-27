package edu.unipi.students.omadara.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFinishedListener;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestClient {
    public interface Callback<T> {
        void onCall(T t);
    }
    private static RestClient instance;
    private static final String BASE_ENDPOINT = "https://api-core.earbits.com/v1";
    private RequestQueue requestQueue;


    public static synchronized RestClient getInstance(Context context) {
        if (instance == null) {
            instance = new RestClient(context);
        }
        return instance;
    }

    private RestClient(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.start();
    }

    public void getAlbums(final Callback<List<Album>> callback, final int maxNumber, final Response.ErrorListener errorListener) {
        final String albumsUrl = BASE_ENDPOINT + "/albums";
        final Object getAlbumsTag = new Object();
        requestQueue.add(new JsonArrayRequest(Request.Method.GET, albumsUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray jsonAlbums) {
                final List<Album> albums = new ArrayList<>();
                final int N = Math.min(maxNumber, jsonAlbums.length());
                for(int i = 0; i < N; i++) {
                    try {
                        JSONObject jsonAlbum = jsonAlbums.getJSONObject(i);
                        final Album album = new Album();
                        album.setId(jsonAlbum.getString("id"));
                        album.setTitle(jsonAlbum.getString("name"));
                        album.setArtist(jsonAlbum.getString("artist_name"));
                        album.setTrackCount(jsonAlbum.getInt("track_count"));
                        String thumbUrl = jsonAlbum.getString("cover_image_thumb_url").replaceFirst("http:","https:");
                        requestQueue.add(new ImageRequest(thumbUrl, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                album.setThumbnail(bitmap);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, errorListener).setTag(getAlbumsTag));
                        requestQueue.add(new JsonArrayRequest(Request.Method.GET, albumsUrl + "/" + album.getId() + "/tracks", null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonTracks) {
                                int totalDuration = 0;
                                for(int j = 0; j < jsonTracks.length(); j++) {
                                    try {
                                        totalDuration += jsonTracks.getJSONObject(j).getInt("duration");
                                    } catch (JSONException e) {
                                        Log.e("musicplayer", "JSONException trying to read a track duration.", e);
                                    }
                                }
                                album.setDuration(totalDuration);
                            }
                        }, errorListener).setTag(getAlbumsTag));
                        albums.add(album);
                    } catch (JSONException e) {
                        Log.e("musicplayer", "JSONException trying to read from an album json.", e);
                    }
                }
                requestQueue.addRequestFinishedListener(new RequestFinishedListener<Object>() {
                    int awaitingRequests = N * 2;
                    @Override
                    public void onRequestFinished(Request<Object> request) {
                        if(request.getTag() == getAlbumsTag && --awaitingRequests == 0) {
                            requestQueue.removeRequestFinishedListener(this);
                            callback.onCall(albums);
                        }
                    }
                });
            }
        }, errorListener).setTag(getAlbumsTag));
    }
}
