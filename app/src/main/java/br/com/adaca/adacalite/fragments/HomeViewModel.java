package br.com.adaca.adacalite.fragments;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.webkit.WebView;
import androidx.lifecycle.ViewModelProvider;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<WebView> mWebview = new MutableLiveData<>();

    public LiveData<WebView> getWebview() {
        return mWebview;
    }

    public void webviewDataChange(WebView webView) {
        mWebview.setValue(webView);
    }
    final static class HomeViewModelFactory implements ViewModelProvider.Factory {

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(HomeViewModel.class)) {
                return (T) new HomeViewModel();
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }
}
