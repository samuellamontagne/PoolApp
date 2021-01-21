package com.example.poolapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.poolapp.FirstFragment;


import org.w3c.dom.ls.LSOutput;

public class SecondFragment extends Fragment {

    TextView showPlayersTextView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState

    ) {
        // Inflate the layout for this fragment
        View fragmentSecondLayout = inflater.inflate(R.layout.fragment_second, container, false);

        showPlayersTextView = fragmentSecondLayout.findViewById(R.id.textview_second);
        //showPlayersTextView.setText(FirstFragment.trendingPlayerListTemp.toString());

        return fragmentSecondLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String trendPlayers = SecondFragmentArgs.fromBundle(getArguments()).getTrendingPlayers();
        TextView trendingView = view.getRootView().findViewById(R.id.textview_second);
        trendingView.setText(trendPlayers);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}