package com.dleague.lakeshoreimporters.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.BinReqDTO;
import com.dleague.lakeshoreimporters.httpcalls.HttpNetworkCall;
import com.dleague.lakeshoreimporters.httpcalls.HttpResponseCallbacks;
import com.dleague.lakeshoreimporters.utils.Constants;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.HelperMethods;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.SendMail;
import com.dleague.lakeshoreimporters.utils.TimeUtils;
import com.dleague.lakeshoreimporters.utils.Validations;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_EMAIL;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_NAME;

public class BinRequestForm extends AppCompatActivity implements HttpResponseCallbacks {

    @BindView(R.id.toolbar_simple)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_simple)
    TextView tvToolbarTitle;
    @BindView(R.id.et_form_email)
    EditText etEmail;
    @BindView(R.id.et_form_firstname)
    EditText etFirstName;
    @BindView(R.id.et_form_lastname)
    EditText etLastName;
    @BindView(R.id.et_form_pickup_items)
    EditText etPickupItemsDescriptions;
    @BindView(R.id.tv_form_pickup_time)
    TextView tvPickUpTime;
    @BindView(R.id.rbg_locations)
    RadioGroup radioGroup;
    @BindView(R.id.rb_location)
    RadioButton radioButtonLocation;
    @BindView(R.id.cb_form_1)
    CheckBox checkBox1;
    @BindView(R.id.cb_form_2)
    CheckBox checkBox2;
    @BindView(R.id.cb_form_3)
    CheckBox checkBox3;
    @BindView(R.id.cb_form_4)
    CheckBox checkBox4;

    private View rootView;
    private boolean cb1, cb2, cb3, cb4;
    private TimeUtils timeUtils;
    private String locationStr;
    private BinReqDTO binReqDTO;
    private HttpNetworkCall httpCalls;
    private int code;
    private DialogBuilder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_request_form);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvToolbarTitle.setText("");
        getExtras();
        init();
    }

    @OnCheckedChanged(R.id.cb_form_1)
    void setCheckBox1(boolean isChecked) {
        cb1 = isChecked;
    }

    @OnCheckedChanged(R.id.cb_form_2)
    void setCheckBox2(boolean isChecked) {
        cb2 = isChecked;
    }

    @OnCheckedChanged(R.id.cb_form_3)
    void setCheckBox3(boolean isChecked) {
        cb3 = isChecked;
    }

    @OnCheckedChanged(R.id.cb_form_4)
    void setCheckBox4(boolean isChecked) {
        cb4 = isChecked;
    }

    @OnClick(R.id.btn_form_submit)
    void setSubmitClick() {
        if (fieldsValidation()) {
            createObject();
            new SendBindReqTask().execute();
        }
    }

    @OnClick(R.id.tv_form_pickup_time)
    void setPickUpTime() {
        HelperMethods.hideKeyboard(Objects.requireNonNull(this));
        DateTime dateTime = null;
        int _year = 0, _month = 0, _dayOfMonth = 0;
        dateTime = new DateTime();
        _year = dateTime.getYear();
        _month = dateTime.getMonthOfYear() - 1;
        _dayOfMonth = dateTime.getDayOfMonth();
        DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(this), new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tvPickUpTime.setText(timeUtils.getDate(cal.getTimeInMillis() / 1000));
            }
        },
                _year, _month, _dayOfMonth);
        dialog.show();
    }

    private void getExtras() {
        locationStr = getIntent().getStringExtra("locationStr");
        code = getIntent().getIntExtra("code", 0);
        radioButtonLocation.setText(locationStr);
    }

    private void init() {
        rootView = findViewById(android.R.id.content);
        timeUtils = new TimeUtils();
        dialogBuilder = new DialogBuilder(this);
        httpCalls = new HttpNetworkCall(this, this);
        etFirstName.setText(AppSpace.sharedPref.readValue(CUSTOMER_NAME, "").split(" ")[0]);
        etLastName.setText(AppSpace.sharedPref.readValue(CUSTOMER_NAME, "").split(" ")[1]);
        etEmail.setText(AppSpace.sharedPref.readValue(CUSTOMER_EMAIL, ""));
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

        if (Validations.isStringEmpty(etPickupItemsDescriptions.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter pickup items description.");
            return false;
        }

        if (Validations.isStringEmpty(tvPickUpTime.getText().toString())) {
            MessageUtil.showSnackbarMessage(rootView, "Please enter order pickup time.");
            return false;
        }
        return true;
    }

    private String getContent() {
        String msg = "First Name:   " + etFirstName.getText().toString() + "\n" +
                "Last Name:   " + etLastName.getText().toString() + "\n" +
                "Email:   " + etEmail.getText().toString() + "\n" +
                "Host Location:   " + radioButtonLocation.getText().toString() + "\n" +
                getResources().getString(R.string.item_youre_picking) + ":   " + etPickupItemsDescriptions.getText().toString() + "\n" +
                getResources().getString(R.string.date_pick) + ":   " + tvPickUpTime.getText().toString() + "\n" +
                getResources().getString(R.string.cb_form_1) + ":   " + ((cb1) ? "Off" : "On") + "\n" +
                getResources().getString(R.string.cb_form_2) + ":   " + ((cb2) ? "Off" : "On") + "\n" +
                getResources().getString(R.string.cb_form_3) + ":   " + ((cb3) ? "Off" : "On") + "\n" +
                getResources().getString(R.string.cb_form_4) + ":   " + ((cb4) ? "Off" : "On") + "\n";
        return msg;
    }

    private void createObject() {
        String sheet = "";
        if (code == 1) {
            sheet = "Strathmore";
        } else if (code == 2) {
            sheet = "Langdon";
        } else if (code == 3) {
            sheet = "Chestermere";
        } else if (code == 4) {
            sheet = "Calgary";
        } else if (code == 5) {
            sheet = "South Calgary";
        }
        binReqDTO = new BinReqDTO(etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(),
                etPickupItemsDescriptions.getText().toString(), radioButtonLocation.getText().toString(), sheet,
                "addItem", tvPickUpTime.getText().toString(), ((cb1) ? "I understand, and agree" : "I disagree"),
                ((cb2) ? "I understand, and agree" : "I disagree"), ((cb3) ? "I understand, and agree" : "I disagree"),
                ((cb4) ? "I understand, and agree" : "I disagree"));
    }

    @Override
    public void onSuccess(int responseCode, Object object) {
        hideLoadingDialog();
        finish();
    }

    @Override
    public void onFailure(int responseCode, String message) {
        hideLoadingDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BinRequestForm.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class SendBindReqTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            showLoadingDialog();
            httpCalls.sendBinRequest(binReqDTO);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    private void showLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.loadingDialog("Loading...");
            }
        });
    }

    private void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.dismissDialog();
            }
        });
    }

}
