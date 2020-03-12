package com.example.myapplication.loveyeqin99;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private ProgressBar progressBar;

    String file_str = Environment.getExternalStorageDirectory().getPath();
    File dir_file = new File(file_str+"/loveyeqin99");
    File photo = new File(file_str+"/loveyeqin99/1.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        Picasso.with(MainActivity.this).load(photo).resize(400,534).into(imageView);
        textView = findViewById(R.id.textView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1&&resultCode==this.RESULT_OK){
            imageView = findViewById(R.id.imageView);
            Picasso.with(MainActivity.this).load(photo).resize(400,543).into(imageView);
        }
    }

    public void takePhoto(View view) throws IOException {
        if(!dir_file.exists()){
            dir_file.mkdir();
        }
        if(photo.exists()) {
            photo.delete();
        }
       photo.createNewFile();
        Uri imageUri;
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, photo.getPath());
        imageUri = this.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println(Uri.fromFile(photo));
        System.out.println();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,1);
    }

    public void detect(View view) {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);



        OkHttpClient okHttpClient =new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).build();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"),photo);

        MultipartBody multipartBody =new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo",photo.getName(),requestBody)
                .build();

        Request request = new Request.Builder()
                .url("http://106.13.0.159:5000/compare")
                .post(multipartBody)
                .build();

        Call call = okHttpClient.newCall(request);
        Toast.makeText(MainActivity.this,"正在识别，请稍后。。。",Toast.LENGTH_LONG).show();
        call.enqueue(new Callback() {


            @Override
            public void onFailure(Call call, final IOException e) {

                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT);

                    }
                });
                 }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

                progressBar.setVisibility(View.INVISIBLE);
                Gson gson = new Gson();

                final UpLoadBean upLoadBean = gson.fromJson(result,UpLoadBean.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(upLoadBean.getData().getRecognition_result()==1){
                            Intent intent =new Intent(MainActivity.this,Main2Activity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"不是老婆，滚！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



    }



    public Uri getImageUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        String path = file.getPath();
        Uri imageUri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUri = Uri.fromFile(file);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, path);
            imageUri = this.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        return imageUri;
    }

}
