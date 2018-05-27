package com.happeyhunter.thermopi.rest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by david on 13/01/18.
 */

public class AuthenticationInterceptor implements Interceptor {

    private static final String AUTH_TYPE = "Bearer";
    private String authToken;

    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    private String getAuthorizationHeader() {
        return AUTH_TYPE + " " + authToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request returnRequest;

        if(authToken != null && authToken.trim().length() > 0) {
            returnRequest = chain.request().newBuilder()
                    .header("Authorization", getAuthorizationHeader()).build();
        } else {
            returnRequest = chain.request();
        }

        return chain.proceed(returnRequest);
    }
}

