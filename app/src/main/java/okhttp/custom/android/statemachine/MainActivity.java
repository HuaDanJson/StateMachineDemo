package okhttp.custom.android.statemachine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements UpdateStateListener {

    private final static String TAG = "UploadStateMachine";

    private UploadStateMachine machine;

    private Button btnStartUpload;

    private Button btnUploading;

    private Button btnUploadFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartUpload = findViewById(R.id.btn_start_upload);
        btnUploading = findViewById(R.id.btn_progress_uploading);
        btnUploadFinish = findViewById(R.id.btn_upload_finish);
        initMachine();
        btnStartUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                machine.receiveUploadRequest();
                btnStartUpload.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        machine.uploadingFinis();
                    }
                }, 2000);
            }
        });

        btnUploading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnUploadFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void initMachine() {
        machine = new UploadStateMachine();
        machine.addAllStateToMachine();
        machine.registerListener(this);
        machine.start();
    }

    @Override
    public void update(String tip) {
        Log.i(TAG, "MainActivity update message = " + tip);
        switch (tip) {
            case UploadStateMachine.Default:
                Log.i(TAG, "MainActivity update case UploadStateMachine.Default:");
                btnStartUpload.post(new Runnable() {
                    @Override
                    public void run() {
                        btnStartUpload.setVisibility(View.VISIBLE);
                        btnUploading.setVisibility(View.GONE);
                        btnUploadFinish.setVisibility(View.GONE);
                    }
                });

                break;

            case UploadStateMachine.ReceiveUploadRequest:
                Log.i(TAG, "MainActivity update case UploadStateMachine.ReceiveUploadRequest:");
                btnStartUpload.post(new Runnable() {
                    @Override
                    public void run() {
                        btnStartUpload.setVisibility(View.GONE);
                        btnUploading.setVisibility(View.VISIBLE);
                        btnUploadFinish.setVisibility(View.GONE);
                    }
                });
                break;

            case UploadStateMachine.Uploading:
                Log.i(TAG, "MainActivity update case UploadStateMachine.Uploading:");
                btnStartUpload.post(new Runnable() {
                    @Override
                    public void run() {
                        btnStartUpload.setVisibility(View.GONE);
                        btnUploading.setVisibility(View.VISIBLE);
                        btnUploadFinish.setVisibility(View.GONE);
                    }
                });
                break;

            case UploadStateMachine.UploadFinish:
                Log.i(TAG, "MainActivity update case UploadStateMachine.UploadFinish:");
                btnStartUpload.post(new Runnable() {
                    @Override
                    public void run() {
                        btnStartUpload.setVisibility(View.GONE);
                        btnUploading.setVisibility(View.GONE);
                        btnUploadFinish.setVisibility(View.VISIBLE);

                        btnStartUpload.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                machine.default_();
                            }
                        }, 2000);
                    }
                });
                break;

            default:
                break;
        }
    }
}
