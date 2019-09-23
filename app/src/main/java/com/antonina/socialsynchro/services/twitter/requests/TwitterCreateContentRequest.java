package com.antonina.socialsynchro.services.twitter.requests;

import com.antonina.socialsynchro.services.twitter.requests.authorization.TwitterUserAuthorizationStrategy;

import java.util.List;

public class TwitterCreateContentRequest extends TwitterRequest {
    private final String status;

    protected TwitterCreateContentRequest(String authorizationHeader, String status) {
        super(authorizationHeader);
        this.status = status;
    }

    public String getStatus() {
        return percentEncode(status);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/1.1/statuses/update.json";
        protected String status;
        private String accessToken;
        private String secretToken;

        @Override
        public TwitterCreateContentRequest build() {
            prepareAuthorization();
            return new TwitterCreateContentRequest(authorization.buildAuthorizationHeader(), status);
        }

        @Override
        protected void prepareAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(accessToken)
                    .secretToken(secretToken)
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL);
            authorization.addSignatureParameter("status", status);
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder secretToken(String secretToken) {
            this.secretToken = secretToken;
            return this;
        }
    }
}
