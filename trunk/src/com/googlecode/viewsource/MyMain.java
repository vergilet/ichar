package com.googlecode.viewsource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.ParseException;

public class MyMain extends Activity
{
    ImageButton LButton;
    ImageButton SButton;
    String token = "soo";
    String MyId = "id";
    String LoginT = null;
    String PassT = null;
    EditText LoginEditor;
    EditText PassEditor;
    public ProgressDialog dialog;
    Intent intent = new Intent();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        LButton = (ImageButton) findViewById(R.id.login_button);
        LButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginEditor = (EditText)findViewById(R.id.Login_text);
                PassEditor = (EditText) findViewById(R.id.Pass_text);
                LoginT = LoginEditor.getText().toString();
                PassT = PassEditor.getText().toString();

                //Запускаєм прогресбар
                dialog = new ProgressDialog(v.getContext());
                dialog.setMessage("Synchronization...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.show();

                //Ініціалізуємо підключення
                HttpAuth auth = new HttpAuth();
                auth.execute();

                //Показуємо інтенту в який клас ми хочемо перейти
                intent.setClass(v.getContext(), ProfilePage.class);

            }
        });


        SButton = (ImageButton) findViewById(R.id.Setting_button);
        SButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Показуємо інтенту в який клас ми хочемо перейти
                intent.setClass(v.getContext(), SettingActivity.class);
                //intent.putExtra(ProfilePage.ACCESS_TOKEN, "lol");

                startActivity(intent);
                finish();

            }
        });
    }

    private class HttpAuth extends AsyncTask<Void, Void, String[]> {
        String answer[];
        TextView text_alert;

        public HttpAuth(){

        }

        @Override
        protected void onPreExecute() {
            //Тут можна всьо ініціалізовувати перед запуском
            this.text_alert = (TextView) findViewById(R.id.text_alert);
        }

        //Наші статичні дані (Ід додатку, доступ, та й усе)
        String APP_ID = "2783462";
        String settings = "4096";
        String redirect_uri = "http://api.vk.com/blank.html";

        HttpClient client = new DefaultHttpClient();

        @Override
        protected String[] doInBackground(Void... voids) {
            String[] er = new String[1];
            er[0]="er";
            try{
                answer = getAccessToken(LoginT, PassT);
                return answer;
            }catch (Exception e){
                return er;
            }
        }

        public String[] getAccessToken(String login, String pass)  {
            String body = "l";
            HttpPost post = null;
            HttpResponse response = null;
            HttpParams params = new BasicHttpParams();
            HttpClientParams.setRedirecting(params, false);
            client = new DefaultHttpClient(params);

            //Формуєм параметри запиту з ід-шкою додатку, настройками доступу,
            //редірект урлом, методом отримання відповіді і що варіант відповіді(код чи токен)
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            qparams.add(new BasicNameValuePair("client_id", APP_ID));
            qparams.add(new BasicNameValuePair("scope", settings));
            qparams.add(new BasicNameValuePair("redirect_uri", redirect_uri));
            qparams.add(new BasicNameValuePair("display", "wap"));
            qparams.add(new BasicNameValuePair("response_type", "token"));

            //Формуєм сам запит і додаєм параметри ^
            URI uri = null;
            try {
                uri = URIUtils.createURI("http", "oauth.vk.com", -1, "/oauth/authorize",
                        URLEncodedUtils.format(qparams, "UTF-8"), null);
            } catch (URISyntaxException e) {
                return null;
            }
            System.out.println(uri);
            post = new HttpPost(uri);
            try {
                response = client.execute(post);
            } catch (IOException e) {
                return null;  //To change body of catch statement use File | Settings | File Templates.
            }
            post.abort();
            try {
                //З відповіді сервера дістаєм заголовок з шляхом
                //        
                body = EntityUtils.toString(response.getEntity());
            } catch (IOException ex) {
                Logger.getLogger(MyMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(MyMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(body);

            //Парсим сторінку і дістаєм ip_h i to
            String form_action = "<input type=\"hidden\" name=\"ip_h\" value=\"";
            int uri_start_index = body.indexOf(form_action) + form_action.length();
            int uri_end_index = uri_start_index + body.substring(uri_start_index).indexOf('"');
            String ip_h = body.substring(uri_start_index, uri_end_index);

            form_action = "<input type=\"hidden\" name=\"to\" value=\"";
            uri_start_index = body.indexOf(form_action) + form_action.length();
            uri_end_index = uri_start_index + body.substring(uri_start_index).indexOf('"');
            String to_h = body.substring(uri_start_index, uri_end_index);

            System.out.println(ip_h);
            System.out.println(to_h);

            //Задаєм параметри для авторизації( деякі параметри константи )
            //ми додаємо тільки іпх, тох, логін, і пасс
            List<NameValuePair> postform = new ArrayList<NameValuePair>();
            postform.add(new BasicNameValuePair("q", "1"));
            postform.add(new BasicNameValuePair("ip_h", ip_h));
            postform.add(new BasicNameValuePair("from_host", "api.vk.com"));
            postform.add(new BasicNameValuePair("to", to_h));
            postform.add(new BasicNameValuePair("expire", "0"));
            postform.add(new BasicNameValuePair("email", login));
            postform.add(new BasicNameValuePair("pass", pass));

            //Формуєм основний запит авторизації( тут пoходу треба захищене
            //з'єднання через HTTPS
            URI uri2 = null;
            try {
                uri2 = URIUtils.createURI("https", "login.vk.com", -1, "/?act=login&soft=1&utf8=1",
                        URLEncodedUtils.format(postform, "UTF-8"), null);
            } catch (URISyntaxException e) {
                return null;  //To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println(uri2);
            post = new HttpPost(uri2);
            try {
                response = client.execute(post);
            } catch (Exception e) {
                return null;  //To change body of catch statement use File | Settings | File Templates.
            }
            post.abort();




            //Дістаємо код сторінки на якій маємо дати дозвіл додатку
            //String body = null;
            try {
                body = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                return null;
            }

            //Перевіряємо доступ і парсим акшо його нема
            if (body.contains("<form method=\"POST\" action=\"")) {

                //Парсим сторінку і дістаєм силку підтвердження доступу
                form_action = "<form method=\"POST\" action=\"";
                uri_start_index = body.indexOf(form_action) + form_action.length();
                uri_end_index = uri_start_index + body.substring(uri_start_index).indexOf('>') - 1;
                String grant_access_uri = body.substring(uri_start_index, uri_end_index);
                System.out.println(grant_access_uri);
                //Дістали код дозволу, відправляєм
                post = new HttpPost(grant_access_uri);
                try {
                    response = client.execute(post);
                } catch (IOException e) {
                    return null;
                }
                post.abort();
                try {
                    body = EntityUtils.toString(response.getEntity());
                } catch (IOException ex) {
                    Logger.getLogger(MyMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(MyMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(body);



            } else {
                //Якщо ми вже дозволили доступ до додатку то юзаєм це
                String access_granted = response.getFirstHeader("location").getValue();//.split("?")[1].split("&")[0].split("=")[1];
                System.out.println("zz" + access_granted);
                post = new HttpPost(access_granted);
                try {
                    response = client.execute(post);
                } catch (IOException e) {
                    return null;
                }
                post.abort();


                post = new HttpPost(response.getFirstHeader("location").getValue());
                try {
                    response = client.execute(post);
                } catch (IOException e) {
                    return null;
                }
                post.abort();


            }

            //І ось він, ось він код моєї мрії
            //Це буде або Токен або Код, в залежності від першого запиту
            String[] access_token = new String[2];
            access_token[0] = response.getFirstHeader("location").getValue().split("#")[1].split("&")[0].split("=")[1];
            System.out.println(access_token[0]);

            access_token[1] = response.getFirstHeader("location").getValue().split("user_id=")[1];//.split("&")[0].split("=")[2]; //length-1//
            System.out.println(access_token[1]);

            return access_token;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onCancelled() {
            this.text_alert.setText("Operation canceled");
        }

        @Override
        protected void onPostExecute(String[] result) {
            dialog.dismiss();
            LoginEditor.setEnabled(true);
            PassEditor.setEnabled(true);
            if(result[0] == "er"){
                token = "Error";
                this.text_alert.setTextColor(Color.WHITE);
                this.text_alert.setTextSize(13);
                this.text_alert.setBackgroundColor(Color.argb(100,255,0,0));
                this.text_alert.setText("Login or password incorrect");
            }else{
                token = result[0];
                MyId = result[1];
                intent.putExtra(ProfilePage.ACCESS_TOKEN, token);
                intent.putExtra(ProfilePage.USER_ID, MyId);
                startActivity(intent);
                finish();
            }
        }
    }
}
