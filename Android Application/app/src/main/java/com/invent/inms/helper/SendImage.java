package com.invent.inms.helper;

import android.widget.Toast;

import com.invent.inms.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class SendImage {

    String imageUrl = "40.121.131.36:50053/receiveImages";
    String uploadKey = "upload";

    CloseableHttpClient httpclient = HttpClients.createDefault();

    public void sendImageToServer(String currentImagePath) {
        try {
            HttpPost httppost = new HttpPost(imageUrl); // your server

            FileBody bin = new FileBody(new File(currentImagePath)); // image for uploading

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("upload", bin)
                    .addTextBody("key",uploadKey)
                    .build();
            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        }catch ( Exception e) {
        }
    }
}