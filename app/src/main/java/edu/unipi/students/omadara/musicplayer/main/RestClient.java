package edu.unipi.students.omadara.musicplayer.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.unipi.students.omadara.musicplayer.models.Genre;
import edu.unipi.students.omadara.musicplayer.models.Album;
import edu.unipi.students.omadara.musicplayer.models.Track;

public class RestClient {
    public interface Callback<T> {
        void onRequestFinished(T t, boolean isLast);
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

    public void requestAlbums(final Callback<Album> callback, final int maxNumber, final Response.ErrorListener errorListener) {
        final String albumsUrl = BASE_ENDPOINT + "/albums";
        requestQueue.add(new JsonArrayRequest(Request.Method.GET, albumsUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray jsonAlbums) {
                final int N = Math.min(maxNumber, jsonAlbums.length());
                final int[] completedCount = new int[1];
                for(int i = 0; i < N; i++) {
                    try {
                        JSONObject jsonAlbum = jsonAlbums.getJSONObject(i);
                        final Album album = new Album();
                        album.setId(jsonAlbum.getString("id"));
                        album.setTitle(jsonAlbum.getString("name"));
                        album.setArtist(jsonAlbum.getString("artist_name"));
                        album.setTrackCount(jsonAlbum.getInt("track_count"));
                        album.setDuration(-1);
                        String thumbUrl = jsonAlbum.getString("cover_image_thumb_url").replaceFirst("http:","https:");
                        requestQueue.add(new ImageRequest(thumbUrl, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                album.setThumbnail(bitmap);
                                if(album.getDuration() != -1) {
                                    callback.onRequestFinished(album, ++completedCount[0] == N);
                                }
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, errorListener));
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
                                if(album.getThumbnail() != null) {
                                    callback.onRequestFinished(album, ++completedCount[0] == N);
                                }
                            }
                        }, errorListener));
                    } catch (JSONException e) {
                        Log.e("musicplayer", "JSONException trying to read from an album json.", e);
                    }
                }
            }
        }, errorListener));
    }

    public void requestAlbumTracks(String albumId, final Callback<Track> callback, Response.ErrorListener errorListener) {
        String tracksUrl = BASE_ENDPOINT + "/albums/" + albumId + "/tracks";
        requestQueue.add(new JsonArrayRequest(Request.Method.GET, tracksUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonTracks) {
                for(int i = 0; i < jsonTracks.length(); i++) {
                    try {
                        JSONObject jsonTrack = jsonTracks.getJSONObject(i);
                        Track track = new Track();
                        track.setName(jsonTrack.getString("name"));
                        track.setDuration(jsonTrack.getInt("duration"));
                        track.setMediaUrl(jsonTrack.getString("media_file"));
                        callback.onRequestFinished(track, i == jsonTrack.length() - 1);
                    } catch (JSONException e) {
                        Log.e("musicplayer", "JSONException trying to read a track.", e);
                    }
                }
            }
        }, errorListener));
    }

    public void requestGenres(final Callback<Genre> callback, final boolean withThumbnails, final Response.ErrorListener errorListener) {
        String genresUrl = BASE_ENDPOINT + "/music_collections?parent_collections=true";
        requestQueue.add(new JsonArrayRequest(Request.Method.GET, genresUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray jsonGenres) {
                final int[] completedCount = new int[1];
                final int N = jsonGenres.length() - 7;
                for (int i = 0; i < N; i++) {
                    try {
                        JSONObject jsonGenre = jsonGenres.getJSONObject(i);
                        final Genre genre = new Genre();
                        genre.setId(jsonGenre.getString("id"));
                        genre.setName(jsonGenre.getString("name"));
                        if(withThumbnails) {
                            String thumbUrl = jsonGenre.getString("thumbnail_url").replaceFirst("http:","https:");
                            requestQueue.add(new ImageRequest(thumbUrl, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    genre.setThumbnail(bitmap);
                                    callback.onRequestFinished(genre, ++completedCount[0] == N);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, errorListener));
                        }else{
                            callback.onRequestFinished(genre, ++completedCount[0] == N);
                        }
                    } catch (JSONException e) {
                        Log.e("musicplayer", "JSONException trying to read a genre.", e);
                    }

                }
            }
        }, errorListener));
    }

    public void requestGenreTrack(String genreId, final Callback<Track> callback, Response.ErrorListener errorListener) {
        String streamUrl = "http://streaming.earbits.com/api/v1/stream.json?collection_id=" + genreId;
        requestQueue.add(new JsonObjectRequest(Request.Method.GET, streamUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Track track = new Track();
                    JSONObject jsonTrack = response.getJSONObject("track");
                    track.setName(jsonTrack.getString("artist_name") + " - " + jsonTrack.getString("name"));
                    track.setDuration(jsonTrack.getInt("duration"));
                    track.setMediaUrl(jsonTrack.getString("media_file"));
                    callback.onRequestFinished(track, true);
                } catch (JSONException e) {
                    Log.e("musicplayer", "JSONException trying to read a track.", e);
                }
            }
        }, errorListener));
    }

    public void requestSubGenreTracks(String genreId, final Callback<Track> callback, final Response.ErrorListener errorListener) {
        String subGenresUrl = BASE_ENDPOINT + "/music_collections/" + genreId + "/child_collections";
        requestQueue.add(new JsonArrayRequest(Request.Method.GET, subGenresUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonSubGenres) {
                final int[] completedCount = new int[1];
                final int N = jsonSubGenres.length();
                for (int i = 0; i < N; i++) {
                    try {
                        String subGenreId = jsonSubGenres.getJSONObject(i).getString("id");
                        requestGenreTrack(subGenreId, new Callback<Track>() {
                            @Override
                            public void onRequestFinished(Track track, boolean isLast) {
                                callback.onRequestFinished(track, ++completedCount[0] == N);
                            }
                        }, errorListener);
                    } catch (JSONException e) {
                        Log.e("musicplayer", "JSONException trying to read a subgenre's id.", e);
                    }
                }
            }
        }, errorListener));
    }

}
