package com.example.mclaine.multipleactivities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.example.mclaine.multipleactivities.AppConstants.*;

/**
 * Created by Mclaine on 7/12/2015.
 */
class AppTabAFragment extends BaseFragment {
    private Button mGotoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view       =   inflater.inflate(R.layout.fragment_one_layout, container, false);

        Button mGoToButton = (Button) view.findViewById(R.id.goto_button);
        mGoToButton.setOnClickListener(listener.get());

        return view;
    }

    private final ThreadLocal<View.OnClickListener> listener = new ThreadLocal<View.OnClickListener>() {
        @Override
        protected View.OnClickListener initialValue() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            /* Go to next fragment in navigation stack*/
                    mActivity.get().pushFragments(new AppTabAFragment2(), true, true);
                }
            };
        }
    };
}