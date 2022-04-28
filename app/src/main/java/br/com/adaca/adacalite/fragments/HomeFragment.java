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
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import br.com.adaca.adacalite.R;
import br.com.adaca.adacalite.service.PreferenceService;

public class HomeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private HomeViewModel mViewModel;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private PreferenceService preferenceService;

    public HomeFragment() {/*Required empty constructor*/}

    @NonNull
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceService = new PreferenceService(getContext());
        mViewModel = new ViewModelProvider(this, new HomeViewModel.HomeViewModelFactory()).get(HomeViewModel.class);
        mViewModel.getWebview().observe(this, webView -> {
            if (webView != null) {
                mWebView = webView;
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mWebView = view.findViewById(R.id.home_webview);
        mProgressBar = view.findViewById(R.id.progressBar);

        webViewSetup();
        return view;
    }

    public void onStop() {
        super.onStop();
        mViewModel.webviewDataChange(mWebView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferenceService.unRegister();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void webViewSetup() {
        if (mWebView == null)
            return;

        mWebView.setWebChromeClient((new MyWebViewClient()));
        mWebView.setWebViewClient(new WebViewClient());
        //mWebView.addJavascriptInterface(this, "adaca");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        mWebView.loadUrl(preferenceService.getServletAddress());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.contains("url.")) {
            if (mWebView != null) {
                mWebView.loadUrl(preferenceService.getServletAddress());
            }
        }
    }

    public void onNavigation(String tag) {
        if (mWebView != null) {
            mWebView.loadUrl(preferenceService.getLocation() + "/Jogos-Adaca/Jogos/" + tag);
        }
    }

    public boolean isGoBack() {
        return mWebView.canGoBack();
    }

    public void goBack() {
        mWebView.goBack();
    }

    public void reload() {
        if (mWebView != null) mWebView.reload();
    }

    private final class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
