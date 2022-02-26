package com.example.EyeU;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class record_screen extends AppCompatActivity {
    private FTPClient ftp = null;        // FTP Client 객체
    private FileInputStream fis = null;        // File Input Stream
    private FileOutputStream fos = null;        // File Output Stream
    private File uploadfile;        // File 객체
    public static File downloadfile;        // File 객체
    private static final int MY_PERMISSION_STORAGE = 1111;

    public static TextView view33;

    public static final String FTP_URL = "112.175.184.76";
    public static final String FTP_ID = "okokok5922";
    public static final String FTP_PW = "rlagkfka5922!";
    public static final String FileName = "index.html";
    public static File DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private final String TAG = "ss";

    private Button btn_conn, btn_disconn, btn_upload, btn_download;
    private Button button_aa;
    private ProgressDialog pDialog;
    private boolean progress_state = false;
    private final int progress_bar_type = 0;
    private Context context;
    private Message message;
    public static Handler uiHandler;

    private long intTotalFileSize = 0;
    private String progress_message;
    ArrayList<FTPFile> filelist = new ArrayList<FTPFile>();
    ArrayList<FTPFile> mp4file = new ArrayList<FTPFile>();


    ListViewAdapter listViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_screen);

        ActionBar actionBar = getSupportActionBar();

        view33 = (TextView)findViewById(R.id.textView_title);
        button_aa = (Button)findViewById(R.id.button_aa);


        button_aa.setOnClickListener(new View.OnClickListener() { //새로고침 부분
            @Override
            public void onClick(View v) {
                recreate();

            }
        });

        uploadfile = new File(Environment.getExternalStorageDirectory().toString() + "/" + FileName);
        downloadfile = new File(Environment.DIRECTORY_DOWNLOADS + FileName);

        ListView listView = findViewById(R.id.list_view2);

        listViewAdapter = new ListViewAdapter(message,context);


        listView.setAdapter(listViewAdapter);

        uiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(getApplicationContext(),"다운로드 중입니다...",Toast.LENGTH_LONG).show();
                        Log.d("다운", "FileDownload");
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "다운로드완료 새로고침을 해주세요!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Log.d("다운", "uiHandler switch error");
                        break;
                }

            }

        };


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                checkPermission();
                ConnFTP();


            }
        });

        thread.start();
        try {
            thread.join();
            listViewAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(record_screen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if (ActivityCompat.shouldShowRequestPermissionRationale(record_screen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(record_screen.this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(record_screen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(record_screen.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }



    private void ConnFTP() {
        Log.d(TAG, "ConnFTP");
        try
        {
            ftp = new FTPClient();        // FTP Client 객체 생성
            ftp.setControlEncoding("UTF-8");        // 문자 코드를 UTF-8로 인코딩
            ftp.connect(FTP_URL);      // 서버접속 " "안에 서버 주소 입력 또는 "서버주소", 포트번호
            ftp.login(FTP_ID, FTP_PW);        // FTP 로그인 ID, PASSWORLD 입력
            ftp.enterLocalPassiveMode();        // Passive Mode 접속일때
            Log.d(TAG, "ConnFTP / success");

            viewFTPFileList();


        }
        catch (IOException e)
        {
            Log.e(TAG, "ConnFTP = " + e);
        }
        Log.d(TAG, "ConnFTP / end");
    }

    private void viewFTPFileList(){
        Log.d(TAG, "viewFTPFileList");

        try
        {

            FTPFile[] ftpFiles = ftp.listFiles("/html");
            if(ftpFiles != null){
                for (FTPFile i : ftpFiles){
                    if (i.getName().endsWith(".mp4")){
                        mp4file.add(i);
                    }
                }
                Log.d("ss","파일리스트 = "+filelist.toString());
                listViewAdapter.additem(mp4file);

            }


        }
        catch (IOException e)
        {
            Log.e(TAG, "viewFTPFileList = " + e);
        }

    }
    private void DisconnFTP() {
        Log.d(TAG, "DisconnFTP");
        if (ftp != null && ftp.isConnected())
        {
            try
            {
                ftp.disconnect();        // 접속 끊기
                Log.d(TAG, "DisconnFTP / success");
            }
            catch (IOException e)
            {
                Log.e(TAG, "DisconnFTP = " + e);
            }
            Log.d(TAG, "DisconnFTP / end");
        }
        else
        {
            Log.d(TAG, "DisconnFTP / else");
        }
    }

    private void Logout() {
        Log.i(TAG, "Logout");
        try
        {
            ftp.logout();// FTP Log Out
            Log.i(TAG, "Logout / success");
        }
        catch (IOException e)
        {
            Log.e(TAG, "FileUpload / fis.logout();= " + e);
        }
        Log.i(TAG, "Logout / end");
    }
}
