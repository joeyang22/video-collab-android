package me.joeyang.musiccollab.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import me.joeyang.musiccollab.Fragments.JoinRoomFragment;
import me.joeyang.musiccollab.Fragments.RoomFragment;
import me.joeyang.musiccollab.JsonConstants;
import me.joeyang.musiccollab.R;
import me.joeyang.musiccollab.SocketConstants;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    Context mContext;
    private String roomId;
    private String name;
    private boolean inRoom = false;
    JoinRoomFragment joinRoomFragment;
    private Socket mSocket;
    {
        try{
            mSocket = IO.socket("https://pacific-hamlet-3110.herokuapp.com");
            Log.i("asdf","This socket is being configured correctly");
        }catch (Exception e){
            mSocket = null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},0);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        }

        setContentView(R.layout.activity_main);
        addJoinRoomFragment();
    }

    private void addJoinRoomFragment() {
        joinRoomFragment = new JoinRoomFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFragmentContainer, joinRoomFragment).commit();
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

    public void disconnect (View v){
        mSocket.disconnect();
    }

    public void createRoom(View view){
        name = joinRoomFragment.getNameFieldText();
        if (name==null || name==""){
            new AlertDialog.Builder(mContext).setMessage(getString(R.string.enter_name_alert)).show();
            return;
        }
        if (mSocket.connected()){
            mSocket.disconnect();
        }else{
            mSocket.on(Socket.EVENT_CONNECT, onConnection);
            mSocket.on(SocketConstants.roomCreated, onRoomCreated);
            mSocket.on(SocketConstants.joinSuccessful, onJoinSuccessful);

            mSocket.connect();
        }
    }
    public void joinRoom(View view) {
        name = joinRoomFragment.getNameFieldText();
        if (name==null || name==""){
            new AlertDialog.Builder(mContext).setMessage(getString(R.string.enter_name_alert)).show();
            return;
        }
        roomId = joinRoomFragment.getRoomFieldText();
        if (roomId!= null && roomId!= ""){
            if (mSocket.connected()){
                mSocket.disconnect();
            }else{
                mSocket.on(Socket.EVENT_CONNECT, onJoinConnection);
                mSocket.on(SocketConstants.joinSuccessful, onJoinSuccessful);
                mSocket.connect();
            }
        }
    }
    private Emitter.Listener onConnection = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obj = new JSONObject();
            try{
                obj.put(JsonConstants.userId, Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
            }catch (JSONException e){
                return;
            }
            mSocket.emit(SocketConstants.createPlaylist, obj.toString());
        }
    };

    private Emitter.Listener onRoomCreated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obj = (JSONObject) args[0];
            mSocket.emit(SocketConstants.joiningRoom,obj);
            try{
                roomId = obj.getString(JsonConstants.roomId);
            }catch(JSONException e){
                roomId = "";
            }
        }
    };

    private Emitter.Listener onJoinSuccessful = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (!inRoom) {

                RoomFragment fragment = RoomFragment.newInstance(roomId);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFragmentContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                for (Object o : args){
                    Log.i(LOG_TAG, o.toString());
                }
                inRoom = true;
            }
        }
    };

    private Emitter.Listener onJoinConnection = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject object = new JSONObject();
            try{
                object.put(JsonConstants.roomId, roomId);
            }catch (JSONException e){
                return;
            }
            mSocket.emit(SocketConstants.joiningRoom, object);
        }
    };


}
