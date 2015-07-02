package com.ihostingbd.rssreadertemplate;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MainActivity extends ActionBarActivity {
    String key_items = "item";
    String key_title = "title";
    String key_description ="description";
    String key_link = "link";
    String key_pubdate="pubdate";
    ListView listPost= null;
    List<HashMap<String,Object>> post_lists = new ArrayList<HashMap<String,Object>>();
    List<String>lists = new ArrayList<String>();
    ArrayAdapter<String>adapter = null;
    RSSReaderClient readerClient = new RSSReaderClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listPost = (ListView) findViewById(R.id.listPost);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2,android.R.id.text1,lists){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view= super.getView(position, convertView, parent);
                TextView txt1 = (TextView)view.findViewById(android.R.id.text1);
                TextView txt2 =(TextView)view.findViewById(android.R.id.text2);
                HashMap<String,Object>data = post_lists.get(position);
                txt1.setText(data.get(key_title).toString());
                txt2.setText(data.get(key_description).toString());
                return  view;
            }
        };

        Document xmlFeed = readerClient.getRSSFromServer("http://osmangninahid.com/blog/rss/rss.xml");
        NodeList nodeList = xmlFeed.getElementsByTagName("item");
        for(int i=0;i<nodeList.getLength();i++){
            Element item = (Element)nodeList.item(i);
            HashMap<String,Object>feed = new HashMap<String,Object>();
            feed.put(key_title,readerClient.getValue(item,key_title));
            feed.put(key_description,readerClient.getValue(item,key_description));
            feed.put(key_link,readerClient.getValue(item,key_link));
            feed.put(key_pubdate,readerClient.getValue(item,key_pubdate));
            post_lists.add(feed);
            lists.add(feed.get(key_title).toString());
        }
        listPost.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
