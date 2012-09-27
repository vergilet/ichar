package com.googlecode.viewsource;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marika's Computer
 * Date: 17.04.12
 * Time: 18:37
 * To change this template use File | Settings | File Templates.
 */
public class ReqMethods {
    HttpClient client = new DefaultHttpClient();
    HttpGet get;
    HttpResponse response;
    List<Result> friendsList;
    //messages.send

    public String friendsGet(String token) throws IOException, URISyntaxException {
        //String mess = message;
        String command = "https://api.vkontakte.ru/method/friends.get?&fields=uid,first_name,last_name,photo,online&access_token=" + token;
        URL url = new URL(command);
        URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        get = new HttpGet(uri);
        response = client.execute(get);
        String all = EntityUtils.toString(response.getEntity());
        return all;
    }





    public String usersGet(String token, String id) throws IOException, URISyntaxException {
        //String mess = message;
        String command = "https://api.vkontakte.ru/method/users.get?uids="+ id +"&fields=uid,first_name,last_name,sex,bdate,photo_medium_rec,activity&access_token=" + token;
        URL url = new URL(command);
        Log.i("z",command);
        URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        get = new HttpGet(uri);
        response = client.execute(get);
        String all = EntityUtils.toString(response.getEntity());
        return all;
    }
}
