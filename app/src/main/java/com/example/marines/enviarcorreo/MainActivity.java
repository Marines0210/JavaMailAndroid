package com.example.marines.enviarcorreo;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("NewApi") public class MainActivity extends AppCompatActivity{
//Envio de correo masivo a una lista de cliente especificos
    //Seccion de dudas sin que el cliente se loguee


    Session session = null;
    ProgressDialog pdialog = null;
    String usu="";
    String contra="";
    EditText mensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enviar = (Button) findViewById(R.id.enviar);
        mensaje = (EditText) findViewById(R.id.mensaje);

        usu="controldepermisosdif@gmail.com";
        contra="controldepermisos";


        enviar.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View arg0){

                if(!mensaje.getText().toString().trim().equalsIgnoreCase("")){
                        //Nombre de host del servidor agregamos una variable propiedad
                        Properties props = new Properties();
                        props.put("mail.smtp.host", "smtp.googlemail.com");

                        //Agregamos el socket para recivir respuesta del servidor  gmail
                        props.put("mail.smtp.socketFactory.port", "465");

                        //Agregamos nuestro socket en SSLsocket con esto nosotros tramitiremos la informacion de manera segura
                        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.port", "465");
                        try{


                                //obtenemos una sesion con las propiedades que realizamos anteriormente y nos autenticamos
                                session = Session.getDefaultInstance(props, new Authenticator() {
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication(usu, contra);
                                    }
                                });

                                pdialog = ProgressDialog.show(MainActivity.this, "", "Enviando Mensaje...", true);

                            EnviarCorreo task = new EnviarCorreo();
                                task.execute();

                        }catch(Exception e){
                            e.printStackTrace();
                        }


                    }else{
                       mensaje.setError("Es necesario llenar este campo");
                    }
            }

        });
    }

    private class EnviarCorreo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {

                if (session != null ){
                    //Creamos un mensaje
                    Message message = new MimeMessage(session);

                //Agregamos el emisor
                message.setFrom(new InternetAddress(usu));
                //Agregamos el receptor
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("mega-0210@hotmail.com"));
                //agremos el asunto
                message.setSubject("Contraseña de su cuenta Control de Permisos");
                //Agregamos formato del mensaje
                message.setContent(mensaje.getText().toString(), "text/html; charset=utf-8");
                Transport.send(message);
            }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pdialog.dismiss();
            mensaje.setText("");
            Toast.makeText(getApplicationContext(), "Mensaje Enviado ", Toast.LENGTH_LONG).show();
        }

    }

}
