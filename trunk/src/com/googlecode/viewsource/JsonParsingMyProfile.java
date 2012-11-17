package com.googlecode.viewsource;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marika's Computer
 * Date: 17.04.12
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
public class JsonParsingMyProfile {
          public List<MyInfo> myPrifileParsing(String text){
              String somejson = text;
              Gson gson = new Gson();
              MyInfoContainer container = gson.fromJson(somejson, MyInfoContainer.class);
              List<MyInfo> information = container.response;
              return information;
          }
}


class MyInfoContainer {
    List<MyInfo> response;
}

class MyInfo {
    String uid;
    String first_name;
    String last_name;
    String sex;
    String bdate;
    String photo_medium_rec;
    String activity;
}