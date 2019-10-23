package com.minhnv.dagger2androidpro.ui.main;

public interface MainNavigator {
    void onError(Throwable throwable);
    void onSuccess();
    void ServerLoadList();
}
