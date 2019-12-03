package com.dleague.lakeshoreimporters.networkcalls;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

public interface NetworkCallbacks {
    void onPreServiceCall();
    void onSuccess(int responseCode, Response response);
    void onFailure(int responseCode, ApolloException exception);
}
