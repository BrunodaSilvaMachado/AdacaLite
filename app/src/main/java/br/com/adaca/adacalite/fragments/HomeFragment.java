package br.com.adaca.adacalite.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import br.com.adaca.adacalite.R;
import br.com.adaca.adacalite.service.PreferenceService;

public class HomeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private HomeViewModel mViewModel;
    private WebView mWebView;
    private PreferenceService preferenceService;
    private SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment(){/*Required empty constructor*/}

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceService = new PreferenceService(getContext());
        mViewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mWebView = view.findViewById(R.id.home_webview);

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        webViewSetup();
        swipeRefreshLayout.setOnRefreshListener(()->{
            if(mWebView != null)
                mWebView.reload();
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //mWebView.setInitialScale(75);
        if(mViewModel.getWebviewUrl() != null) {
            mWebView.loadUrl(mViewModel.getWebviewUrl());
        }
        else{
            mWebView.loadUrl(preferenceService.getServletAddress());
        }
    }

    public void onStop() {
        super.onStop();
        mViewModel.setWebviewUrl(mWebView.getUrl());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void webViewSetup()
    {
        if(mWebView == null)
            return;

        mWebView.setWebChromeClient((new WebChromeClient()));
        mWebView.setWebViewClient(new MyWebViewClient());
        //mWebView.addJavascriptInterface(this, "adaca");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.contains("url.")){
            if(mWebView != null){
                mWebView.loadUrl(preferenceService.getServletAddress());
            }
        }
    }

    public void onNavigation(String tag){
        if(mWebView == null)
            return;
        //mWebView.setInitialScale(75);
        mWebView.loadUrl(preferenceService.getLocation() + "/Jogos-Adaca/Jogos/" + tag);
    }

    public boolean isGoBack() {
        return mWebView.canGoBack();
    }

    public void goBack() {
        mWebView.goBack();
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            swipeRefreshLayout.setRefreshing(false);
            super.onPageFinished(view, url);
        }
    }
}
