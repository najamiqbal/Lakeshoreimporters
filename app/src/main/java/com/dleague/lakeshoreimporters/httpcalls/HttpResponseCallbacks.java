package com.dleague.lakeshoreimporters.httpcalls;

public interface HttpResponseCallbacks {
    void onSuccess(int responseCode, Object object);
    void onFailure(int responseCode, String message);
}
