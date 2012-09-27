package com.googlecode.viewsource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class ProfilePage extends Activity
{
    public static final String USER_ID = "email";
    public static final String ACCESS_TOKEN = "token";





    ///////


    String[] myMenuList ; //  getResources().getStringArray(R.array.cat_names);
    TypedArray drawablsIds;// = {R.drawable.one, R.drawable.two, R.drawable.three,
            //R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
            //R.drawable.eight, R.drawable.nine, R.drawable.ten};


    private ArrayAdapter<String> adapter;
    ListView vkMenuList;

    //////
    TextView token;
    TextView uid;
    TextView name;
    TextView sname;
    TextView sex;
    TextView bdate;
    TextView photo;
    ImageView photoIm;
    TextView activity;
    TextView friend1;
    Button getFriends;

    Intent intent = new Intent();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run);
        Bundle extras = getIntent().getExtras();
        final String token_to = extras.getString(ACCESS_TOKEN);
        final String MyId = extras.getString(USER_ID);
        Dothat dt = new Dothat(token_to, MyId);
        dt.execute();


        ///////////////////////
        Context ctx = getApplicationContext();
        Resources res = ctx.getResources();
        myMenuList =   getResources().getStringArray(R.array.vkmenunames);
        //drawablsIds = getResources().getIntArray(R.array.vkmenuicons);
        drawablsIds = res.obtainTypedArray(R.array.vkmenuicons);
        vkMenuList.setAdapter(new ImageAndTextAdapter(this, R.layout.vkmenulist, myMenuList, drawablsIds));
        //vkMenuList.setAdapter(new VkMenuTextImageAdapter(myMenuList, drawablsIds));
        //adapter = new ArrayAdapter<String>(this,R.layout.vkmenulist,R.id.row_name,myMenuList);
        //vkMenuList.setAdapter(adapter);

        //////////////////////////
        //token = (TextView)findViewById(R.id.token);
        //token.setText(MyId);


        /*getFriends = (Button) findViewById(R.id.frbutton);
        getFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Показуємо інтенту в який клас ми хочемо перейти
                intent.setClass(v.getContext(),YourFriendsList.class);
                intent.putExtra(YourFriendsList.ACCESS_TOKEN, token_to);

                startActivity(intent);
                finish();

            }
        });  */





    }

    private class Dothat extends AsyncTask<Void, Void, List<MyInfo>> {
        String myToken = null;
        String myId = null;

        public Dothat(String tok, String id){
            this.myToken = tok;
            this.myId = id;
        }

        @Override
        protected void onPreExecute() {
            uid     = (TextView)findViewById(R.id.uid);
            name    = (TextView)findViewById(R.id.name);
            //sname   = (TextView)findViewById(R.id.sname);
            //sex     = (TextView)findViewById(R.id.sex);
            //bdate   = (TextView)findViewById(R.id.bdate);
            //photo   = (TextView)findViewById(R.id.photo);
            photoIm = (ImageView) findViewById(R.id.photoIm);
            //friend1 = (TextView)findViewById(R.id.friend1);
            vkMenuList = (ListView)findViewById(R.id.listView);
            activity = (TextView)findViewById(R.id.activ);

        }

        //List<Result> some2 = null;
        Bitmap myPhoto = null;
        @Override
        protected List<MyInfo> doInBackground(Void... voids) {
            List<MyInfo> some = null;
            try{
                some = getMyPageInfo(myToken,myId);
                try{
                    myPhoto = downloadImage(some.get(0).photo_medium_rec);
                }catch(Exception e){
                    myPhoto = null;
                }
            }catch(Exception e){
                Log.e("Parsing error","Can't get some parameters");
            }

            return some;





        }
        List<MyInfo> myList;

        public List<MyInfo> getMyPageInfo(String token, String id) throws IOException, URISyntaxException {
            String itsToken = token;
            String itsId = id;
            ReqMethods req = new ReqMethods();
            JsonParsingMyProfile myPr = new JsonParsingMyProfile();
            myList = myPr.myPrifileParsing(req.usersGet(itsToken,itsId));
            //String lol = req.usersGet(itsToken);
            return myList;
        }

        List<Result> friendsList =null;
        public List<Result> getMyFriendsList(String text) throws IOException, URISyntaxException {
            String itsToken = text;
            ReqMethods req = new ReqMethods();
            JsonParsingFriendsList myFriends = new JsonParsingFriendsList();
            friendsList = myFriends.serJosn(req.friendsGet(itsToken));
            //String lol = req.usersGet(itsToken);
            return friendsList;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(List<MyInfo> result) {
            Bitmap im =   myPhoto;
            //photo.setText(result.get(0).photo_medium_rec);
            uid.setText("id" + result.get(0).uid);
            name.setText((result.get(0).first_name)+" "+result.get(0).last_name);
            activity.setText(result.get(0).activity);
            Log.i("k", (String) activity.getText()+"1");
            if(((String) activity.getText()).isEmpty()){
                activity.setText("Статус відсутній :(");
            }

            /*
            //
            // Тут стать користувача
            //
            sname.setText(result.get(0).last_name);
            if(result.get(0).sex=="0"){
                sex.setText("");
            if (result.get(0).sex=="1")
                sex.setText("woman");
            }
            if (result.get(0).sex=="2")
                sex.setText("man");

            bdate.setText(result.get(0).bdate); */



            photoIm.setImageBitmap(im);
            //friend1.setText(some2.get(0).first_name);
        }








        public  Bitmap downloadImage(String iUrl) {
            //
            // Метод скачування зображення для аватарки
            //
            Bitmap bitmap = null;
            HttpURLConnection conn = null;
            BufferedInputStream buf_stream = null;
            try {
                conn = (HttpURLConnection) new URL(iUrl).openConnection();
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.connect();
                buf_stream = new BufferedInputStream(conn.getInputStream(), 7858);
                bitmap = BitmapFactory.decodeStream(buf_stream);
                buf_stream.close();
                conn.disconnect();
                buf_stream = null;
                conn = null;
            } catch (Exception ex) {
                return null;
            } finally {
                if ( buf_stream != null )
                    try { buf_stream.close(); } catch (IOException ex) {}
                if ( conn != null )
                    conn.disconnect();
            }
            return bitmap;
        }

        //setContentView(R.layout.dialog);
        //Bundle extras = getIntent().getExtras();
        //String tokann = extras.getString(ACCESS_TOKEN);

    }







}



