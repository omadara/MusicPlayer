package edu.unipi.students.omadara.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
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
    private ImageLoader imageLoader;


    public static synchronized RestClient getInstance(Context context) {
        if (instance == null) {
            instance = new RestClient(context);
        }
        return instance;
    }

    private RestClient(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.start();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public void getAlbums(final Callback<List<Album>> callback) {
        final String albumsUrl = BASE_ENDPOINT + "/albums";
        requestQueue.add(new JsonArrayRequest(Request.Method.GET, albumsUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonAlbums) {
                List<Album> albums = new ArrayList<>();
                for(int i = 0; i < jsonAlbums.length(); i++) {
                    try {
                        JSONObject jsonAlbum = jsonAlbums.getJSONObject(i);
                        final Album album = new Album();
                        album.setId(jsonAlbum.getString("id"));
                        album.setTitle(jsonAlbum.getString("name"));
                        album.setArtist(jsonAlbum.getString("artist_name"));
                        album.setTrackCount(jsonAlbum.getInt("track_count"));
                        imageLoader.get(jsonAlbum.getString("cover_image_thumb_nail"), new ImageLoader.ImageListener() {
                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                album.setThumbnail(response.getBitmap());
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        requestQueue.add(new JsonArrayRequest(Request.Method.GET, albumsUrl + "/" + album.getId() + "/tracks", null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonTracks) {
                                int totalDuration = 0;
                                for(int j = 0; j < jsonTracks.length(); j++) {
                                    try {
                                        totalDuration += jsonTracks.getJSONObject(j).getInt("duration");
                                    } catch (JSONException e) { }
                                }
                                album.setDuration(totalDuration);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }));
                        albums.add(album);
                    } catch (JSONException e) { }
                }
                callback.onCall(albums);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }
}
