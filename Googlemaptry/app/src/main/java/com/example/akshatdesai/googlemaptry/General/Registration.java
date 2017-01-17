package com.example.akshatdesai.googlemaptry.General;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.akshatdesai.googlemaptry.Notification.MyInstanceIDListenerService.refreshedToken;

public class Registration extends AppCompatActivity {


    EditText et_name, et_mail, et_pass, et_phone, et_address;
    ImageView iv;
    Button browse,submit, cancel;
    String name, message, mail, pass, address, phone, gender, response="",msg="";
    RadioButton male, female;
    boolean flagForEmail, flagForMobileNo;
    int status, i,abc;
    ProgressDialog pd;
    RadioGroup rg;
    String image;
    File imageFile = null;
    String imagepath;
    private Uri mCropImageUri;
    Bitmap bitmap;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


        et_name = (EditText) findViewById(R.id.et_name);
        et_mail = (EditText) findViewById(R.id.et_mail);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);
        submit = (Button) findViewById(R.id.btn_submit);
        browse = (Button)findViewById(R.id.btn_browse);
        cancel = (Button) findViewById(R.id.btn_cncl);
        rg = (RadioGroup)findViewById(R.id.rg_mf);
        male = (RadioButton) findViewById(R.id.rb_male);
        female = (RadioButton) findViewById(R.id.rb_female);
        iv= (ImageView) findViewById(R.id.imageView2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et_name.getText().toString().trim();
                mail = et_mail.getText().toString().trim();
                flagForEmail=isValidMail(mail);
                pass = et_pass.getText().toString().trim();
                address = et_address.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                flagForMobileNo=isValidNo(phone);
                abc = rg.getCheckedRadioButtonId();



                if(abc == male.getId())
                {
                    gender = "male";
                }else{
                    gender = "female";
                }
                if(EnablePermission.isInternetConnected(Registration.this)) {
                    if (name.equals("") || mail.equals("") ||abc == -1 ||pass.equals("") || address.equals("") || phone.equals("") || gender.equals("")) {
                        Toast.makeText(Registration.this, "Enter Sufficient Details", Toast.LENGTH_LONG).show();
                    }
                    else if(!flagForEmail) {
                        Toast.makeText(Registration.this, "Enter valid email", Toast.LENGTH_LONG).show();
                    }else if(!flagForMobileNo) {
                        Toast.makeText(Registration.this, "Enter valid Mobile No", Toast.LENGTH_LONG).show();
                    }

                    else
                    {

                        new Register_Web().execute();
                    }
                }else {
                    Toast.makeText(Registration.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
                } else {
                    onSelectImageClick(iv);
                }

            }
        });


        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 5: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 5000);
                    }
                } else {
                    Toast.makeText(Registration.this, "Need Camera permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    imageFile = new File(result.getUri().getPath());
                } catch (NullPointerException e) {
                    Toast.makeText(this, "Image Path Error..", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
        if (imageFile != null) {
            Toast.makeText(getApplicationContext(), "Please wait.. while uploading image.", Toast.LENGTH_SHORT).show();
            imagepath = imageFile.getPath();
            iv.setImageURI(Uri.fromFile(imageFile));
            iv.setVisibility(View.VISIBLE);
            final BitmapFactory.Options biOptions = new BitmapFactory.Options();
            biOptions.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeFile(imagepath,biOptions);
            biOptions.inSampleSize = calculateInSampleSize(biOptions,100,100);
            biOptions.inJustDecodeBounds = false;
            bitmap= BitmapFactory.decodeFile(imagepath,biOptions);
            image = getStringImage(bitmap);
            //new().execute();
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }




    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }




    private boolean isValidMail(String email){
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email);
        check = m.matches();

        /*if(!check)
        {
            et_mail.setError("Not Valid Email");
        }*/
        return check;
    }

    private boolean isValidNo(String phoneNo){
        boolean check;
        Pattern p;
        Matcher m;
        String MobilePattern = "[0-9]{10}";

        p = Pattern.compile(MobilePattern);

        m = p.matcher(phoneNo);
        check = m.matches();

        /*if(!check)
        {
            et_phone.setError("Not Valid phoneNo");
        }*/
        return check;
    }

    class Register_Web extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Registration.this);
            pd.setMessage("Please wait..");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

           /* String filePath = fileUri.getPath();
            File sourceFile = new File(filePath);
            Log.e("Image",""+sourceFile);*/

            try {
                URL url = new URL("http://"+ WebServiceConstant.ip+"/Tracking/registration.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                HashMap<String,String> params = new HashMap<>();
                params.put("Name", name);
                params.put("Email", mail);
                params.put("Password", pass);
                params.put("Phone", phone);
                params.put("Address", address);
                params.put("Gender", gender);
                params.put("token", refreshedToken);
                if(image != null){
                params.put("imgPath", image);
                }
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(params));
                writer.flush();
                writer.close();
                os.close();

                // conn.connect();

                int responseCode=conn.getResponseCode();
                Log.e("responceCode:",""+responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
                Log.e("response",""+response);


                /*String params = "Name=" + URLEncoder.encode(String.valueOf(name), "UTF-8") + "&" + "Email=" + URLEncoder.encode(String.valueOf(mail), "UTF-8") + "&" + "Password=" + URLEncoder.encode(String.valueOf(pass), "UTF-8") + "&" + "Phone=" + URLEncoder.encode(String.valueOf(phone),"UTF-8") + "&" + "Address=" + URLEncoder.encode(String.valueOf(address), "UTF-8") + "&" + "Gender=" + URLEncoder.encode(String.valueOf(gender), "UTF-8") + "&" + "token=" + URLEncoder.encode(String.valueOf(refreshedToken), "UTF-8")+"&" + "imgPath=" + URLEncoder.encode(String.valueOf(image), "UTF-8");
                URL url = new URL("http://"+ WebServiceConstant.ip+"/Tracking/registration.php?" + params);

                Log.e("URL",""+url);
                Object obj = null;

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( params );
                wr.flush();

                // Get the server response

               BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }
                msg = sb.toString();
                Log.e("responce", "" + msg);





                *//*HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
               // httpURLConnection.setRequestProperty("uploaded_file",String.valueOf(sourceFile));

                int rescode = httpURLConnection.getResponseCode();

                Log.e("responce", "" +rescode);
                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    Log.e("responce", "" +responce);
                    in.close();
                    msg = responce.toString();
                    Log.e("responce", "" + msg);
                *//*
*/

            } catch (UnsupportedEncodingException e1) {
                //  e1.printStackTrace();
                Log.e("Unsupported",e1.getMessage()+"");
            } catch (MalformedURLException e) {
                Log.e("MalformedURLException",e.getMessage()+"");
                //  e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException",e.getMessage()+"");
                // e.printStackTrace();
            }

            return null;
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pd.cancel();
            if (msg.equals("0")) {
                Toast.makeText(Registration.this, "Problem in registration.", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(Registration.this, "Successfully registered.", Toast.LENGTH_SHORT).show();


                Intent i = new Intent(getApplicationContext(), Login_new.class);
                startActivity(i);
            }

        }
    }
}