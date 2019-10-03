package tech.tedybearblog.moviecatalogue4.Fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tech.tedybearblog.moviecatalogue4.Item.TVShow;
import tech.tedybearblog.moviecatalogue4.R;
import tech.tedybearblog.moviecatalogue4.adapter.ListTVShowAdapter;
import tech.tedybearblog.moviecatalogue4.db.FavoriteHelper;

import java.util.ArrayList;

public class FavoriteTVShowFragment extends Fragment {
    public static final String KEY_TVSHOW = "tvshow";
    public ArrayList<TVShow> listTVShow = new ArrayList<>();
    public RecyclerView rvTVShow;
    public ProgressBar progressBar;
    private ListTVShowAdapter listTVShowAdapter;
    private FavoriteHelper favoriteHelper;
    private Bundle saveState;


    public FavoriteTVShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTVShow = view.findViewById(R.id.rv_tv_show);
        progressBar = view.findViewById(R.id.progressBar);
        if (savedInstanceState != null) {
            saveState = savedInstanceState;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoriteHelper.close();
    }

    @Override
    public void onStart() {
        rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTVShow.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();

        listTVShowAdapter = new ListTVShowAdapter(getContext());
        rvTVShow.setAdapter(listTVShowAdapter);

        if (saveState == null) {
            listTVShow.clear();
            listTVShow.addAll(favoriteHelper.getAllFavorites2());
            if (listTVShow != null) {
                listTVShowAdapter.refill(listTVShow);
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.empty_data), Toast.LENGTH_SHORT).show();
            }
        } else {
            ArrayList<TVShow> list = saveState.getParcelableArrayList(KEY_TVSHOW);
            if (list != null) {
                listTVShowAdapter.refill(list);
            }
        }
        super.onStart();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_TVSHOW, listTVShowAdapter.getListTVShow());
    }
}
