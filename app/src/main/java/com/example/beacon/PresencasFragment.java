package com.example.beacon;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PresencasFragment extends Fragment {


    public PresencasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_status, container, false);
        init();
        return view;
    }

    private void init(){
        //fazer o find de todas aulas já computadas pelo app sobre o usuário
    }

}
