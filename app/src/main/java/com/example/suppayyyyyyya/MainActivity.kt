package com.example.suppayyyyyyya

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.suppayyyyyyya.databinding.ActivityMainBinding
import com.whty.comm.inter.ICommunication
import com.whty.device.utils.TLV
import com.whty.tymposapi.DeviceApi
import com.whty.tymposapi.IDeviceDelegate
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


open class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var deviceApi: DeviceApi? = null
    private var isInit = false
    private var receiver: BroadcastReceiver? = null
    private val devicedialog: DeviceDialogUtil? = null
    private val btAdapter = BluetoothAdapter.getDefaultAdapter()
    private var mDeviceAddress: String? = null
    private var mDeviceName: String? = null
    private var currentDevice: BluetoothDevice? = null
    private var deviceConnected = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        deviceApi = DeviceApi.getInstance(this@MainActivity)
        deviceApi?.setDelegate( object : IDeviceDelegate {
            override fun onConnectedDevice(p0: Boolean) {
                println("test data $p0")
            }

            override fun onDisconnectedDevice(p0: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onUpdateWorkingKey(p0: BooleanArray?) {
                TODO("Not yet implemented")
            }

            override fun onGetMacWithMKIndex(p0: HashMap<String, String>?) {
                TODO("Not yet implemented")
            }

            override fun onReadCard(p0: HashMap<String, String>?) {
                TODO("Not yet implemented")
            }

            override fun onWaitingcard() {
                TODO("Not yet implemented")
            }

            override fun onReadCardData(p0: HashMap<String, String>?) {
                TODO("Not yet implemented")
            }

            override fun onGetPinBlock(p0: HashMap<String, String>?) {
                TODO("Not yet implemented")
            }

            override fun onDownGradeTransaction(p0: HashMap<String, String>?) {
                TODO("Not yet implemented")
            }

            override fun onSelectICApplication(p0: MutableList<TLV>?) {
                TODO("Not yet implemented")
            }

        })

        val dialogHandler =  object : Handler(Looper.myLooper()!!) {
            @SuppressLint("MissingPermission")
            override fun dispatchMessage(msg: Message) {
                super.dispatchMessage(msg)
                when (msg.what) {
                    SharedMSG.No_Device_Selected -> Toast.makeText(
                        this@MainActivity,
                        UIMessage.donot_select_device, Toast.LENGTH_SHORT
                    )
                        .show()

                    SharedMSG.Device_Found -> if (mDeviceAddress == null || mDeviceAddress!!.length <= 0) {
                        devicedialog?.listDevice(this@MainActivity)
                    }

                    SharedMSG.No_Device -> Toast.makeText(
                        this@MainActivity,
                        UIMessage.donot_connect_device, Toast.LENGTH_SHORT
                    )
                        .show()

                    SharedMSG.Device_Ensured -> {
                        currentDevice = msg.obj as BluetoothDevice
                        mDeviceName = currentDevice?.getName()
                        mDeviceAddress = currentDevice?.getAddress()
                        binding.textResult.setText(
                            UIMessage.selected_device + " "
                                    + mDeviceName
                        )
                    }

                    SharedMSG.Device_Disconnected -> {
                        deviceConnected = false
                        binding.textResult.setText(UIMessage.disconnected_device_success)
                    }
                }
            }
        }

        receiver = BlueToothDeviceReceiver(dialogHandler)
        val intent = IntentFilter()
        intent.addAction(BluetoothDevice.ACTION_FOUND)
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        intent.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        intent.addAction(BluetoothDevice.ACTION_NAME_CHANGED)
        intent.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)

        intent.priority = -1000
        applicationContext.registerReceiver(receiver, intent)
        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)
        mayRequestLocation()

        binding.init.setOnClickListener {
                if (!isInit) {
                    val init = deviceApi?.initDevice(ICommunication.BLUETOOTH_DEVICE)
                    if (init != null) {
                        isInit = init
                    }
                }
                println("test data init $isInit")
                binding.textResult.text = "is Init $isInit"

            val handler = Handler(Looper.myLooper()!!)
            handler.postDelayed({
                if (isInit) {
                    object : Thread() {
                        override fun run() {
                            Looper.prepare()
                            val connect = deviceApi?.connectDevice("DE:E6:5A:BA:80:4C")
                            if (connect != null) {
                                deviceConnected = connect
                            }
                        }
                    }.start()

                    if (deviceConnected) {
                        binding.textResult.text = "Device Connected $deviceConnected"
                    }
                }
            },3000)
        }



    }

    private fun mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            val checkPermission = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            val checkPermission2 = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val checkPermission3 = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val checkPermission4 = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val checkPermission5: Int
            val checkPermission6: Int
            val checkPermission7: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                checkPermission5 = ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
                checkPermission6 = ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.BLUETOOTH_SCAN
                )
                checkPermission7 = ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                )
                if (checkPermission != PackageManager.PERMISSION_GRANTED || checkPermission2 != PackageManager.PERMISSION_GRANTED || checkPermission3 != PackageManager.PERMISSION_GRANTED || checkPermission4 != PackageManager.PERMISSION_GRANTED || checkPermission5 != PackageManager.PERMISSION_GRANTED || checkPermission6 != PackageManager.PERMISSION_GRANTED || checkPermission7 != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat
                        .requestPermissions(
                            this, arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_ADVERTISE
                            ),
                            0
                        )
                }
            } else {
                if (checkPermission != PackageManager.PERMISSION_GRANTED || checkPermission2 != PackageManager.PERMISSION_GRANTED || checkPermission3 != PackageManager.PERMISSION_GRANTED || checkPermission4 != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat
                        .requestPermissions(
                            this, arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            0
                        )
                }
            }
        }
    }
}