package br.com.adaca.adacalite.fragments;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> mWebview = new MutableLiveData<>();

    public String getWebviewUrl() {
        return mWebview.getValue();
    }

    public void setWebviewUrl(String url) {
        mWebview.setValue(url);
    }
}
