package com.kawaidesu.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // -----------------------------------------------
    // ボタンクリック
    // -----------------------------------------------    
    public void onClick(View v) {
        switch(v.getId()){
        case R.id.button1:
            // ----- 非同期通信
            Uri.Builder builder = new Uri.Builder();
            AsyncHttpRequest task = new AsyncHttpRequest();
            task.execute(builder);
        }  
    }

    // ------------------------------
    // 非同期通信
    // ------------------------------
    public class AsyncHttpRequest extends AsyncTask<Uri.Builder, Void, String> {
        @SuppressLint("UnlocalizedSms") @Override
        protected String doInBackground(Builder... params) {
        	try {
        		// ----- メール送信
        		MailSender ms = new MailSender();
        		ms.send();
            } catch (Exception e) {
                return e.toString();
            }
        	return null;
         }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "メールを送信しました", Toast.LENGTH_SHORT).show();
        }
    }

    // -----------------------------------------------
    // メール送信
    // -----------------------------------------------    
    public class MailSender {
        private Properties properties;
         
        public MailSender(){
            properties = System.getProperties();
        }
         
        public void send(){
            properties.put("mail.smtp.host", "smtp.host.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication("info@hoge.com", "hogehoge");
                }
            });
            MimeMessage message = new MimeMessage(session);
             
            try {
                String from = "info@hoge.com";
                String[] to = {"to@hoge.com"};
                message.setFrom(new InternetAddress(from));
                InternetAddress[] toAddress = new InternetAddress[to.length];
                for(int i = 0; i < to.length; i++ ){
                    toAddress[i] = new InternetAddress(to[i]);
                }
                for(int i = 0; i < toAddress.length; i++){
                    message.addRecipient(Message.RecipientType.TO, toAddress[i]);
                }

                message.setSubject("メールのタイトル");
                message.setText("メールの本文");
                Transport.send(message);
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

}
