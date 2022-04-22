package br.com.adaca.adacalite.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdacaAccountsManager {
    public static String[] readUserProfile(@NonNull Context context) {
        Cursor c = context.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);

        if (c != null) {
            if (c.moveToFirst() && c.getCount() == 1 && c.getPosition() == 0) {
                int displaName = c.getColumnIndex("display_name");
                int photo = c.getColumnIndex("photo_thumb_uri");
                return new String[] {c.getString(displaName),c.getString(photo)};
            }
            c.close();
        }
        return new String[] {null,null};
    }

    @Nullable
    public static String getUserName(@NonNull Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        return (accounts.length > 0)? accounts[0].name: null;
    }
}
