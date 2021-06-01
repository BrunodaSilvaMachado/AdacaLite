package br.com.adaca.adacalite.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import br.com.adaca.adacalite.R;

public class PreferenceService {
    private static final String ADACA_SERVLET_ADDRESS = "adaca.servlet.address";
    private static final String URL_PROTOCOL = "url.protocol";
    private static final String URL_HOST = "url.host";
    private static final String URL_PORT = "url.port";

    private final String ADACA_DEFAULT_ADDRESS;
    private final String URL_HOST_DEFAULT;
    private final String URL_PORT_DEFAULT;
    private static SharedPreferences mPrefs;
    private SharedPreferences
            .OnSharedPreferenceChangeListener s = null;
    public PreferenceService(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        ADACA_DEFAULT_ADDRESS = context.getString(R.string.servlet_address_default);
        URL_HOST_DEFAULT = context.getString(R.string.url_host_default);
        URL_PORT_DEFAULT = context.getString(R.string.url_port_default);
        if(context instanceof SharedPreferences.OnSharedPreferenceChangeListener){
            s = (SharedPreferences.OnSharedPreferenceChangeListener) context;
            mPrefs.registerOnSharedPreferenceChangeListener(s);
        }

    }

    @NonNull
    public String getServletAddress(){
        return mPrefs.getString(ADACA_SERVLET_ADDRESS,ADACA_DEFAULT_ADDRESS);
    }

    @NonNull
    public String getLocation(){
        return mPrefs.getString(URL_PROTOCOL,"http://") +
                mPrefs.getString(URL_HOST,URL_HOST_DEFAULT) +
                ":" +
                mPrefs.getString(URL_PORT,URL_PORT_DEFAULT);
    }

    public void unRegister()
    {
        if(s != null){
            mPrefs.unregisterOnSharedPreferenceChangeListener(s);
            s = null;
        }
    }
}
