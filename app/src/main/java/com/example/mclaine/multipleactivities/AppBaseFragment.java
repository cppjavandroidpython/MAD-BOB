package com.example.mclaine.multipleactivities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Mclaine on 7/12/2015.
 */
class BaseFragment extends Fragment {
    public final ThreadLocal<AppMainTabActivity> mActivity = new ThreadLocal<AppMainTabActivity>() {
        public Object getActivity(Object activity) {
            return activity;
        }

        @Override
        protected AppMainTabActivity initialValue() {
            return (AppMainTabActivity) this.getActivity(mActivity);
        }


    };

    {
    }

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onBackPressed(){
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
    }
}