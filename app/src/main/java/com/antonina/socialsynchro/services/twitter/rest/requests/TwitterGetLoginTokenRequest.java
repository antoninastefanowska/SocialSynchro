package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

public class TwitterGetLoginTokenRequest extends TwitterRequest {
    private TwitterGetLoginTokenRequest(String authorizationString) {
        super(authorizationString);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
        private final static String CALLBACK_URL = "https://socialsynchro.pythonanywhere.com/backend/post_twitter_verifier";

        @Override
        public TwitterGetLoginTokenRequest build() {
            configureAuthorization();
            return new TwitterGetLoginTokenRequest(authorization.buildAuthorizationString());
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .secretToken("")
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL)
                    .addAuthorizationParameter("oauth_callback", CALLBACK_URL);
        }
    }
}
