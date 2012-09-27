package com.googlecode.viewsource;
//nope

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Marika's Computer
 * Date: 20.04.12
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class YourFriendsList extends ListActivity {

    public static final String ACCESS_TOKEN = "token";
    final String[] catnames = new String[] { "Рыжик", "Барсик", "Мурзик",
            "Мурка", "Васька", "Томасина", "Бобик", "Кристина", "Пушок",
            "Дымка", "Кузя", "Китти", "Барбос", "Масяня", "Симба" };
    private ArrayAdapter<String> adapter;

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.dialog);
        //Bundle extras = getIntent().getExtras();
        //String tokann = extras.getString(ACCESS_TOKEN);


        adapter = new ArrayAdapter<String>(this, com.googlecode.viewsource.R.layout.listvsimg, R.id.row_name, catnames);
        setListAdapter(adapter);
    }
}