package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

public class TwitterGetContentRequest extends TwitterRequest {
    private final String id;

    private TwitterGetContentRequest(String authorizationString, String id) {
        super(authorizationString);
        this.id = percentEncode(id);
    }

    public String getID() {
        return id;
    }

    public static String getRequestEndpoint() {
        return "/statuses/show/:id";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/1.1/statuses/show.json";
        private String id;

        @Override
        public TwitterGetContentRequest build() {
            configureAuthorization();
            return new TwitterGetContentRequest(authorization.buildAuthorizationString(), id);
        }

        @Override
        protected void configureAuthorization() {
            if (authorization.isUserAuthorization()) {
                ((TwitterUserAuthorizationStrategy)authorization)
                        .requestMethod("GET")
                        .requestURL(REQUEST_URL)
                        .addSignatureParameter("id", id);
            }
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder authorizationStrategy(TwitterAuthorizationStrategy authorizationStrategy) {
            authorization = authorizationStrategy;
            return this;
        }
    }
}
