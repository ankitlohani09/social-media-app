package com.socialmedia_app.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String userNotFollowingInfluencers) {
        super(userNotFollowingInfluencers);
    }
}
