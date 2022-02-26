package com.example.EyeU;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.core.content.FileProvider;

public class ListViewAdapter extends BaseAdapter {
    FileOutputStream fos ;
    FTPClient ftp;
    private ArrayList<FTPFile> listItems = new ArrayList<FTPFile>();
    final static record_screen RECORD_SCREEN = new record_screen();
    public static boolean s = false;
    private Context context;
    private Message message;
    private Handler uiHandler;

    public ListViewAdapter(Message message, Context context){
        this.context=context;
        this.message= message;

    }
    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
         context = parent.getContext();

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.myrow, parent, false);
        }


        final TextView textView = (TextView) convertView.findViewById(R.id.textView);
        Button button =  (Button)convertView.findViewById(R.id.button);


        // list_item item = listItems.get(position);

        textView.setText(listItems.get(position).getName());
        final String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        final File file = new File(dir+ File.separator+"/download/"+textView.getText().toString());
        if (file.exists() == true){
            button.setVisibility(View.VISIBLE);
        }
        else{
            button.setVisibility(View.INVISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file.exists() == true){
                    Toast.makeText(context, "파일이 있습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setFlags(intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(context, "com.example.EyeU.fileProvider", file);
                    Log.e("z",uri+"");

                    intent.setDataAndType(uri,"video/*");
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, "파일이 없습니다.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Thread ftpThread  = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Message message1 = new Message();
                            message = record_screen.uiHandler.obtainMessage();

                            message1.what = 0;
                            record_screen.uiHandler.sendMessage(message1);

                            ftp = new FTPClient();        // FTP Client 객체 생성
                            ftp.setControlEncoding("UTF-8");        // 문자 코드를 UTF-8로 인코딩
                            ftp.connect(record_screen.FTP_URL);      // 서버접속 " "안에 서버 주소 입력 또는 "서버주소", 포트번호
                            ftp.login(record_screen.FTP_ID, record_screen.FTP_PW);        // FTP 로그인 ID, PASSWORLD 입력
                            ftp.enterLocalPassiveMode();
                            ftp.changeWorkingDirectory("/html");        // 작업 디렉토리 변경
                            FTPFile files[]=ftp.listFiles();

                            FTPFile files2[]=ftp.listFiles();

                            ftp.setFileType(FTP.BINARY_FILE_TYPE);        // 다운로드 파일 타입 셋팅


                            record_screen.downloadfile = new File(record_screen.DIR+"/"+listItems.get(position).getName());
                            fos = new FileOutputStream( record_screen.downloadfile);
                            ftp.retrieveFile( record_screen.downloadfile.getName(), fos);


                            Log.d("z", "FileDownload / success");



                            // 다운로드할 File 생성


                            message1 = new Message();
                            message1.what = 1;
                            record_screen.uiHandler.sendMessage(message1);

                        }

                        catch (IOException e)
                        {
                            Log.e("에러", "FileDownload = " + e);
                        }

                        finally
                        {
                            if (fos != null)
                            {
                                try
                                {
                                    fos.close();        // Stream 닫기
                                }
                                catch (IOException e)
                                {
                                    Log.e("에러", "FileDownload / fos.close() = " + e);
                                }
                            }
                        }
                        Log.d("끝", "FileDownload / end");
                        try {
                            ftp.logout();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                });

            }
        });

        return convertView;
    }
    public void additem(ArrayList<FTPFile> file){

        listItems = file ;

    }


}