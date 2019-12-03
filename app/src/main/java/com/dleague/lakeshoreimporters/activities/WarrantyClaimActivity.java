package com.dleague.lakeshoreimporters.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dleague.lakeshoreimporters.BuildConfig;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.utils.Constants;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.SendMail;
import com.dleague.lakeshoreimporters.utils.Validations;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class WarrantyClaimActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_simple)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_simple)
    TextView toolbarTitle;
    @BindView(R.id.et_wc_firstname)
    EditText etEmail;
    @BindView(R.id.et_wc_lastname)
    EditText etFirstName;
    @BindView(R.id.et_wc_email)
    EditText etLastName;
    @BindView(R.id.et_wc_order_number)
    EditText etOrderNumber;
    @BindView(R.id.et_wc_order_detail)
    EditText etOrderDetail;
    @BindView(R.id.et_wc_defect)
    EditText etDefect;
    @BindView(R.id.et_wc_recommendation)
    EditText etRecommendation;
    @BindView(R.id.cb_wc_1)
    CheckBox checkBox1;
    @BindView(R.id.cb_wc_2)
    CheckBox checkBox2;
    @BindView(R.id.cb_wc_3)
    CheckBox checkBox3;
    @BindView(R.id.cb_wc_4)
    CheckBox checkBox4;
    @BindView(R.id.cb_wc_5)
    CheckBox checkBox5;
    @BindView(R.id.cb_wc_6)
    CheckBox checkBox6;
    @BindView(R.id.cb_wc_7)
    CheckBox checkBox7;
    @BindView(R.id.iv_wc_image)
    ImageView imageView;
    @BindView(R.id.btn_wc_upload_file)
    Button btnUplodaFile;

    private final int REQUEST_TAKE_PHOTO = 1;
    private static final int CAMERA_INTENT_CODE = 101;
    private static final int APPLICATION_SETTING_INTENET_CODE = 102;
    private String mCurrentPhotoPath;
    private File imageFile;
    private View rootView;
    private boolean cb1, cb2, cb3, cb4, cb5, cb6, cb7;
    private String userEmail;
    private String mMsgResponse;
    private String mailContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_warranty_claim);
        ButterKnife.bind(this);
        toolbarTitle.setText("Warranty Claim");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnCheckedChanged(R.id.cb_wc_1)
    void setCheckBox1(boolean isChecked) {
        cb1 = isChecked;
    }

    @OnCheckedChanged(R.id.cb_wc_2)
    void setCheckBox2(boolean isChecked) {
        cb2 = isChecked;
    }

    @OnCheckedChanged(R.id.cb_wc_3)
    void setCheckBox3(boolean isChecked) {
        cb3 = isChecked;
    }

    @OnCheckedChanged(R.id.cb_wc_4)
    void setCheckBox4(boolean isChecked) {
        cb4 = isChecked;
    }

    @OnCheckedChanged(R.id.cb_wc_5)
    void setCheckBox5(boolean isChecked) {
        cb5 = isChecked;
    }

    @OnCheckedChanged(R.id.cb_wc_6)
    void setCheckBox6(boolean isChecked) {
        cb6 = isChecked;
    }

    @OnCheckedChanged(R.id.cb_wc_7)
    void setCheckBox7(boolean isChecked) {
        cb7 = isChecked;
    }

    @OnClick(R.id.btn_wc_submit)
    void setSubmitClick() {
        if (fieldsValidation()) {
            mailContent = getContent();
            if (mCurrentPhotoPath.isEmpty()) {

                SendMail sendMail = new SendMail(this, Constants.EMAIL, "Warranty Claim", mailContent, null);
                sendMail.execute();
            } else {

                SendMail sendMail = new SendMail(this, Constants.EMAIL, "Warranty Claim", mailContent, imageFile);
                sendMail.execute();
            }
        }
    }

    @OnClick(R.id.btn_wc_upload_file)
    void uploadImage() {
        EnableCameraPermission();
    }

    private void init() {
        userEmail = "";
        mCurrentPhotoPath = "";
    }

    private boolean fieldsValidation() {
        if (Validations.isStringEmpty(etEmail.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter email.");
            return false;
        }

        if (Validations.isStringEmpty(etFirstName.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter first name.");
            return false;
        }

        if (Validations.isStringEmpty(etLastName.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter last name.");
            return false;
        }

        if (Validations.isStringEmpty(etOrderNumber.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter Order Number.");
            return false;
        }

        if (Validations.isStringEmpty(etOrderDetail.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter order details.");
            return false;
        }

        if (Validations.isStringEmpty(etDefect.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter defect.");
            return false;
        }

        if (Validations.isStringEmpty(etRecommendation.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter order recommendations.");
            return false;
        }

        if (!cb7) {
            MessageUtil.showSnackbarMessage(rootView, "Please agree the Term's & Conditions.");
            return false;
        }
        return true;
    }

    private String getContent() {
        String msg = "First Name:   " + etFirstName.getText().toString() + "\n\n" +
                "Last Name:   " + etLastName.getText().toString() + "\n\n" +
                "Email:   " + etEmail.getText().toString() + "\n\n" +
                getResources().getString(R.string.order_number) + ":   " + etOrderNumber.getText().toString() + "\n\n" +
                getResources().getString(R.string.order_description) + ":   " + etOrderDetail.getText().toString() + "\n\n" +
                getResources().getString(R.string.order_defect) + ":   " + etDefect.getText().toString() + "\n\n" +
                getResources().getString(R.string.recommendations) + ":   " + etRecommendation.getText().toString() + "\n\n" +
                getResources().getString(R.string.wc_cb1) + ":   " + ((cb1) ? "On" : "Off") + "\n\n" +
                getResources().getString(R.string.wc_cb2) + ":   " + ((cb2) ? "On" : "Off") + "\n\n" +
                getResources().getString(R.string.wc_cb3) + ":   " + ((cb3) ? "On" : "Off") + "\n\n" +
                getResources().getString(R.string.wc_cb4) + ":   " + ((cb4) ? "On" : "Off") + "\n\n" +
                getResources().getString(R.string.wc_cb5) + ":   " + ((cb5) ? "On" : "Off") + "\n\n" +
                getResources().getString(R.string.wc_cb6) + ":   " + ((cb6) ? "On" : "Off") + "\n\n";
        return msg;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void EnableCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {  // if permission is granted then show contact list
            galleryIntent();
        } else {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_INTENT_CODE);  // else show permission Dialog
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {                   // permission Dialog listeners
        switch (RC) {
            case CAMERA_INTENT_CODE:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {        // if permission is granted then show contact list
                    galleryIntent();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {                                          // now, user has denied permission (but not permanently!)
                        showMessageOKCancel("This permission is mandatory for the application. Please allow access.",
                                new DialogInterface.OnClickListener() {
                                    @TargetApi(Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_INTENT_CODE);
                                    }
                                });
                    } else {                                                                        // if permission has denied permission permanently!
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "You have previously declined this permission.\n" +
                                "You must approve this permission in \"Permissions\" in the app settings on your device.", Snackbar.LENGTH_INDEFINITE).setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)), APPLICATION_SETTING_INTENET_CODE);
                            }
                        });

                        snackbar.show();
                    }

                }
                break;
        }
    }

    private void galleryIntent() {
        dispatchTakePictureIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                imageFile = file;
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media
                            .getBitmap(getContentResolver(), Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setColorFilter(0);
                    imageView.setPadding(0, 0, 0, 0);
                }
            } else if (resultCode == RESULT_CANCELED) {
                MessageUtil.showSnackbarMessage(rootView, "Premission denied, Can't capture image.");
            }
        } else if (requestCode == APPLICATION_SETTING_INTENET_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {  // if permission is granted then show contact list
                galleryIntent();
            } else {
                Snackbar snackbar = Snackbar.make(rootView, "You have previously declined this permission.\n" +
                        "You must approve this permission in \"Permissions\" in the app settings on your device.", Snackbar.LENGTH_INDEFINITE).setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)), APPLICATION_SETTING_INTENET_CODE);
                    }
                });

                snackbar.show();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.dleague.lakeshoreimporters",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
