package com.example.mclaine.multipleactivities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import java.util.HashMap;
import java.util.Stack;

import static com.example.mclaine.multipleactivities.R.id.realtabcontent;


class AppMainFragmentActivity extends FragmentActivity {
    private static final String TAB_MyApps = "tab_myapps_identifier";
    private static final String TAB_AppCatalog = "tab_appcatalog_identifier";
    /* Your Tab host */
    private TabHost mTabHost;

    /* A HashMap of stacks, where we use tab identifier as keys..*/
    private HashMap<String, Stack<Fragment>> mStacks;

    /*Save current tabs identifier in this..*/
    private String mCurrentTab;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_tab_fragment_layout);

        /*  
         *  Navigation stacks for each tab gets created.. 
         *  tab identifier is used as key to get respective stack for each tab
         */
        mStacks             =   new HashMap<String, Stack<Fragment>>();
        Stack<Fragment> put = mStacks.put(TAB_MyApps, new Stack<Fragment>());
        Stack<Fragment> put1 = mStacks.put(TAB_AppCatalog, new Stack<Fragment>());

        mTabHost                =   (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(listener);
        mTabHost.setup();

        initializeTabs();
    }


    private View createTabView(final int id) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_icon, null);
        ImageView imageView =   (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        return view;
    }

    public void initializeTabs(){
        /* Setup your tab icons and content views.. Nothing special in this..*/
        TabHost.TabSpec spec    =   mTabHost.newTabSpec(TAB_MyApps);
        mTabHost.setCurrentTab(-3);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.tab_home_state_btn));
        mTabHost.addTab(spec);


        spec                    =   mTabHost.newTabSpec(TAB_AppCatalog);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.tab_status_state_btn));
        mTabHost.addTab(spec);
    }


    /*Comes here when user switch tab, or we do programmatically*/
    TabHost.OnTabChangeListener listener    =   new TabHost.OnTabChangeListener() {
        class AppTabAFirstFragment extends Fragment {
        }

        public void onTabChanged(String tabId) {
        /*Set current tab..*/
            mCurrentTab                     =   tabId;

            if(mStacks.get(tabId).size() == 0){
          /*
           *    First time this tab is selected. So add first fragment of that tab.
           *    Dont need animation, so that argument is false.
           *    We are adding a new fragment which is not present in stack. So add to stack is true.
           */
                if(tabId.equals(TAB_MyApps)){
                    pushFragments(tabId, new AppTabAFirstFragment(), false,true);
                }else if(tabId.equals(TAB_AppCatalog)){
                    pushFragments(tabId, new AppTabBFirstFragment(), false,true);
                }
            }else {
          /*
           *    We are switching tabs, and target tab is already has atleast one fragment. 
           *    No need of animation, no need of stack pushing. Just show the target fragment
           */
                pushFragments(tabId, mStacks.get(tabId).lastElement(), false,false);
            }
        }
    };


    /* Might be useful if we want to switch tab programmatically, from inside any of the fragment.*/
    public void setCurrentTab(int val){
        mTabHost.setCurrentTab(val);
    }


    /* 
     *      To add fragment to a tab. 
     *  tag             ->  Tab identifier
     *  fragment        ->  Fragment to show, in tab identified by tag
     *  shouldAnimate   ->  should animate transaction. false when we switch tabs, or adding first fragment to a tab
     *                      true when when we are pushing more fragment into navigation stack. 
     *  shouldAdd       ->  Should add to fragment navigation stack (mStacks.get(tag)). false when we are switching tabs (except for the first time)
     *                      true in all other cases.
     */
    public void pushFragments(String tag, Fragment fragment,boolean shouldAnimate, boolean shouldAdd){
        if(shouldAdd)
            mStacks.get(tag).push(fragment);
        FragmentManager manager         =   getSupportFragmentManager();
        FragmentTransaction ft            =   manager.beginTransaction();
        if(shouldAnimate) {
            final FragmentTransaction fragmentTransaction = ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        //    fragmentTransaction;
        }
//        FragmentTransaction replace = ft.replace(R.id.realtabcontent, fragment);
        ft.commit();
    }


    public void popFragments(){
      /*    
       *    Select the second last fragment in current tab's stack.. 
       *    which will be shown after the fragment transaction given below 
       */
        Fragment fragment             =   mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

      /*pop current fragment from stack.. */
        mStacks.get(mCurrentTab).pop();

      /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        FragmentManager   manager         =   getSupportFragmentManager();
        FragmentTransaction ft            =   manager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
//        final FragmentTransaction replace = ft.replace(realtabcontent, fragment);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        if(mStacks.get(mCurrentTab).size() == 1){
            // We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
            finish();
            return;
        }

        /*  Each fragment represent a screen in application (at least in my requirement, just like an activity used to represent a screen). So if I want to do any particular action
         *  when back button is pressed, I can do that inside the fragment itself. For this I used AppBaseFragment, so that each fragment can override onBackPressed() or onActivityResult()
         *  kind of events, and activity can pass it to them. Make sure just do your non navigation (popping) logic in fragment, since popping of fragment is done here itself.
         */
     //   ((AppBaseFragment)mStacks.get(mCurrentTab).lastElement()).onBackPressed();

        /* Goto previous fragment in navigation stack of this tab */
        popFragments();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mStacks.get(mCurrentTab).size() == 0){
            return;
        }

        /*Now current fragment on screen gets onActivityResult callback..*/
        mStacks.get(mCurrentTab).lastElement().onActivityResult(requestCode, resultCode, data);
    }
}