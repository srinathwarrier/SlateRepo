package com.slate1.interfaces;

/**
 * Created by I076324 on 5/8/2015.
 */
public interface CheckUserAsyncResponse {
    void goToAddUserScreen();
    void goToSlateScreen();
    void saveToSharedPreferences(String userId, String regId);
    void updateRegistrationId(String userId);
}
