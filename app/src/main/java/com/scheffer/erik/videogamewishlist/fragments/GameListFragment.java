package com.scheffer.erik.videogamewishlist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.recyclerviewadapters.GameRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class GameListFragment extends Fragment {

    public static final String GAMES_LIST_KEY = "games-list";

    public GameListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            List<Game> games = new ArrayList<>();
            if (getArguments() != null) {
                games = getArguments().getParcelableArrayList(GAMES_LIST_KEY);
            }

            recyclerView.setAdapter(new GameRecyclerViewAdapter(games));
        }
        return view;
    }
}
