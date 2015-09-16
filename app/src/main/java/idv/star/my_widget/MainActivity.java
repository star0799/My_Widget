package idv.star.my_widget;

import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private ImageButton ibBluetooth,ibWifi,ibLED,ibLock,ibRotation;
    private WifiManager wiFiManager;
    private Camera camera;
    private boolean isLighOn=false;
    private ComponentName componentName;
    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private final int LOCK_SCREEN_CODE = 100;
    private DevicePolicyManager manger;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manger=(DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName=new ComponentName(this, deviceAdminReceiver.class);


        camera = Camera.open();

        //檢查有無Wifi
        wiFiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if(adapter!=null){

        }
        else {
            Toast.makeText(MainActivity.this, "此手機無Wifi", Toast.LENGTH_SHORT).show();
        }




        findViews();
    }

    protected void findViews(){
        ibBluetooth=(ImageButton)findViewById(R.id.ibBluetooth);
        ibBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isEnabled() != true) {   //檢查藍芽有無開啟.藍芽未開啟
                    //開啟藍芽
                    adapter.enable();
                } else {
                    //關閉藍芽
                    adapter.disable();
                }
            }
        });


        ibLED=(ImageButton)findViewById(R.id.ibLED);

        ibLED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLighOn) {
                    Parameters p = camera.getParameters();
                    p.setFlashMode(Parameters.FLASH_MODE_OFF);
                    camera.setParameters(p);
                    camera.stopPreview();

                    isLighOn = false;

                } else {
                    Parameters p = camera.getParameters();
                    p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    camera.startPreview();


                    isLighOn = true;

                }

            }


        });
        ibWifi=(ImageButton)findViewById(R.id.ibWifi);
        ibWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnOffWifi();
            }
        });
        ibLock=(ImageButton)findViewById(R.id.ibLock);
        ibLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onLock();

            }
        });
       ibRotation=(ImageButton)findViewById(R.id.ibRotation);
        ibRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAutoOrientationEnabled(MainActivity.this, false);

            }
        });






    }

    private void turnOnOffWifi(){

        if (!wiFiManager.isWifiEnabled()) {//Wifi未開啟則
            wiFiManager.setWifiEnabled(true);//打開Wifi
        }
        else{
            wiFiManager.setWifiEnabled(false);//關閉Wifi
        }



    }
    private void onLock(){
        boolean active=manger.isAdminActive(componentName);
        if(active){
            manger.lockNow();

        }
        else{
            Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
            startActivityForResult(intent, 0);
            manger.lockNow();

        }


        }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }






    public void setAutoOrientationEnabled(Context context, boolean enabled)
    {
        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
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
