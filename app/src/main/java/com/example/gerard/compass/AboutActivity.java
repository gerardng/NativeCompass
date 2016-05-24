package com.example.gerard.compass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Element adsElement = new Element();
        //adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Gerard Ng is a student at BCIT taking Computer Systems Technology. He specializes in android apps but also has experience with web development.")
                .setImage(R.drawable.gerard_logo)
                .addItem(new Element().setTitle("Version 1.2"))
                //.addItem(adsElement)
                .addGroup("Connect with Gerard")
                .addEmail("gerardngjr@yahoo.com")
                .addWebsite("http://github.com/gerardng")
                .addFacebook("gerardngjr")
                .addTwitter("gngversion1")
                //.addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                //.addPlayStore("com.ideashower.readitlater.pro")
                //.addInstagram("gerard")
                .addGitHub("gerardng")
                //.addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }
}


