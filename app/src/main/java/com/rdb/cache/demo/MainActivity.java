package com.rdb.cache.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rdb.cache.DBCache;
import com.rdb.cache.SPCache;
import com.rdb.cache.TimeProvider;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView resultView = findViewById(R.id.resultView);
        GsonConverter converter = new GsonConverter();
        SPCache.init(this, "cache", converter);
        DBCache.init(this, "cache", new TimeProvider() {
            @Override
            public long getCurTimeMillis() {
                return System.currentTimeMillis();
            }
        }, converter);
        User writeUser = new User("张三", 30);
        SPCache.putObject("user", writeUser);
        DBCache.putObject("user", writeUser, "0", Long.MAX_VALUE);
        User readUser1 = SPCache.getObject("user", User.class);
        User readUser2 = DBCache.getObject("user", "0", User.class);
        resultView.append("SPCache：");
        resultView.append(converter.toString(readUser1));
        resultView.append("\nDBCache：");
        resultView.append(converter.toString(readUser2));
    }

}
