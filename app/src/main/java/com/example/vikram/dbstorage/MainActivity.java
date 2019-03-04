package com.example.vikram.dbstorage;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText keyEditText;
    EditText valueEditText;
    RoomDbImplementation roomDbInstance;

    Handler uiHandler;

    final int SAVE_INCOMPLETE=0;
    final int SAVE_COMPLETE = 1;
    final int FETCH_COMPLETE = 2;
    final int FETCH_INCOMPLETE=3;
    final int DELETE_COMPLETE=4;
    final int DELETE_INCOMPLETE=5;
    final int UPDATE_COMPLETE=6;
    final int UPDATE_INCOMPLETE=7;

    //Fetched attribs
    String entityValueFromDb;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyEditText=findViewById(R.id.keyEditText);
        valueEditText=findViewById(R.id.valueEditText);

        roomDbInstance=Room.databaseBuilder(this,RoomDbImplementation.class,"LabRoomDb").build();

        uiHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what==SAVE_COMPLETE){
                    keyEditText.setText("");
                    valueEditText.setText("");
                    Toast.makeText(getApplicationContext(),"Saved!",Toast.LENGTH_SHORT).show();
                }
                else if(msg.what==SAVE_INCOMPLETE){
                    Toast.makeText(getApplicationContext(),"Constraint Failed!",Toast.LENGTH_SHORT).show();
                }
                else if(msg.what==FETCH_COMPLETE){
                    valueEditText.setText("");
                    valueEditText.setText(entityValueFromDb);
                }
                else if(msg.what==FETCH_INCOMPLETE){
                    Toast.makeText(getApplicationContext(),"No records found!",Toast.LENGTH_SHORT).show();
                }
                else if(msg.what==DELETE_COMPLETE){
                    keyEditText.setText("");
                    valueEditText.setText("");
                    Toast.makeText(getApplicationContext(),"Deleted!",Toast.LENGTH_SHORT).show();
                }
                else if(msg.what==DELETE_INCOMPLETE){
                    Toast.makeText(getApplicationContext(),"No Record to Delete!",Toast.LENGTH_SHORT).show();
                }
                else if(msg.what==UPDATE_COMPLETE){
                    keyEditText.setText("");
                    valueEditText.setText("");
                    Toast.makeText(getApplicationContext(),"Updated!",Toast.LENGTH_SHORT).show();
                }
                else if(msg.what==UPDATE_INCOMPLETE){
                    Toast.makeText(getApplicationContext(),"No Record to Update!",Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    public void saveToDb(View view){
        final RoomEntity entity = new RoomEntity(keyEditText.getText().toString(),valueEditText.getText().toString());

        Thread dbSaveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=uiHandler.obtainMessage();
                message.what = SAVE_COMPLETE;

                try{
                    roomDbInstance.daoAccess().insertEntity(entity);
                }catch (Exception e){
                    message.what = SAVE_INCOMPLETE;
                }
                message.sendToTarget();
            }
        });
        dbSaveThread.start();
    }

    public void fetchFromDb(View view){

        Thread dbFetchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                RoomEntity entity=roomDbInstance.daoAccess().fetchEntity(keyEditText.getText().toString());
                Message message=uiHandler.obtainMessage();
                if(entity != null) {
                    entityValueFromDb = entity.entityValue;
                    message.what = FETCH_COMPLETE;
                }
                else{
                    message.what=FETCH_INCOMPLETE;
                }
                message.sendToTarget();
            }
        });
        dbFetchThread.start();
    }

    public void updateDb(View view){
        final String entityKey = keyEditText.getText().toString();
        final String entityValue = valueEditText.getText().toString();

        Thread dbUpdateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=uiHandler.obtainMessage();
                message.what = UPDATE_COMPLETE;

                try{
                    roomDbInstance.daoAccess().updateEntity(entityKey,entityValue);
                }catch (Exception e){
                    message.what = UPDATE_INCOMPLETE;
                }
                message.sendToTarget();
            }
        });
        dbUpdateThread.start();
    }

    public void deleteFromDb(View view){
        Thread dbDeleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=uiHandler.obtainMessage();
                message.what = DELETE_COMPLETE;

                try{
                    roomDbInstance.daoAccess().deleteEntity(keyEditText.getText().toString());
                }catch (Exception e){
                    message.what = DELETE_INCOMPLETE;
                }
                message.sendToTarget();
            }
        });
        dbDeleteThread.start();
    }



}
