package tech.tedybearblog.moviecatalogue4.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tech.tedybearblog.moviecatalogue4.API.MovieAPI;
import tech.tedybearblog.moviecatalogue4.API.Network;
import tech.tedybearblog.moviecatalogue4.Item.Movie;
import tech.tedybearblog.moviecatalogue4.R;
import tech.tedybearblog.moviecatalogue4.adapter.ListMovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieFragment extends Fragment {
    public static final String KEY_MOVIES = "movies";
    public ArrayList<Movie> listMovies = new ArrayList<>();
    public ProgressBar progressBar;
    private RecyclerView rvMovie;
    private ListMovieAdapter listMovieAdapter;

    public MovieFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progressBar);
        rvMovie.setHasFixedSize(true);

        listMovieAdapter = new ListMovieAdapter(listMovies);
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvMovie.setAdapter(listMovieAdapter);

        if (savedInstanceState == null) {
            new MovieTask().execute(MovieAPI.getURL("popular"));
        } else {
            listMovies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            listMovieAdapter.refill(listMovies);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIES, listMovies);
        super.onSaveInstanceState(outState);
    }

    public class MovieTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading(true);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String teks = null;
            try {
                teks = Network.getFromNetwork(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return teks;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && !TextUtils.isEmpty(s)) {
                try {
                    JSONObject jObject = new JSONObject(s);
                    JSONArray jArray = jObject.getJSONArray("results");
                    for (int i = 0; i < jArray.length(); i++) {
                        Movie movie = new Movie(jArray.getJSONObject(i));
                        listMovies.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rvMovie.setAdapter(new ListMovieAdapter(listMovies));
                showLoading(false);
            }
        }

    }
}
