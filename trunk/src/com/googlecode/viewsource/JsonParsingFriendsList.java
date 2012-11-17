package com.googlecode.viewsource;

import com.google.gson.Gson;
import java.util.List;

public class JsonParsingFriendsList {

    public List<Result> serJosn(String text) {
        String somejson = text;
        Gson gson = new Gson();
        ResultContainer cashResponse = gson.fromJson(somejson, ResultContainer.class);
        List<Result> results = cashResponse.response;
        return results;
    }
}

class ResultContainer {

    List<Result> response;
}

class Result {
    String uid;
    String first_name;
    String last_name;
    String photo;
    char online;
}