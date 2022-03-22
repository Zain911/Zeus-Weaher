package com.iti.aurora.addmedicine.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iti.aurora.MainActivity;
import com.iti.aurora.R;
import com.iti.aurora.addmedicine.presenter.AddMedicinePresenter;
import com.iti.aurora.addmedicine.presenter.AddMedicinePresenterInterface;
import com.iti.aurora.database.ConcreteLocalSource;
import com.iti.aurora.model.Repository;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.RecurrencyModel;
import com.iti.aurora.model.medicine.StrengthUnit;
import com.iti.aurora.utils.Constants;
import com.iti.aurora.utils.dialogs.refillask.RefillDialogRemindMeClickHandler;
import com.iti.aurora.utils.dialogs.refillask.RefillReminderDialog;
import com.iti.aurora.utils.selectdays.DaysOfWeek;
import com.iti.aurora.utils.selectdays.IUpdateText;
import com.iti.aurora.utils.selectdays.SelectDaysAlertDialog;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class AddMedicineActivity extends AppCompatActivity implements AddMedicineViewInterface {
    TextInputEditText nameAddMedication_inputEditText,
            strengthAddMedication_inputEditText,
            reason_inputEditText;
    TextInputLayout textInputLayout_addMedication,
            strengthMedication_TextInputEditText,
            reason_TextInputEditText;
    Spinner formAddMedication_spinner,
            recurrencyAddMedication_spinner,
            instructionsAddMedication_spinner,
            strenghtAddMedication_spinner;
    CardView datesAddMedication_cardview;
    TextView startDatepickerAddmedication_Textview, endDatePicker_textview, timePicker_textview;
    TextView selectedDaysTextView;
    Button addMedication_button;

    int recurrencyDaysNumber;
    Calendar myCalendar = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
    int minute = myCalendar.get(Calendar.MINUTE);

    SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
    SelectDaysAlertDialog selectDaysAlertDialog = new SelectDaysAlertDialog(this, new IUpdateText() {
        @Override
        public void updateText(String text) {
            selectedDaysTextView.setText(text);
        }
    });

    private DateTime selectedStartDate;
    private DateTime selectedEndDate;
    AddMedicinePresenterInterface addMedicinePresenterInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        selectedStartDate = new DateTime();
        selectedEndDate = new DateTime();
        addMedicinePresenterInterface = new AddMedicinePresenter(
                AddMedicineActivity.this, Repository.getInstance(
                ConcreteLocalSource.getInstance(AddMedicineActivity.this), AddMedicineActivity.this)
        );
        //for using alarm manager
        addMedicinePresenterInterface.setContext(AddMedicineActivity.this);
        addMedicinePresenterInterface.getSelectedDaysAlertdialog(this.selectDaysAlertDialog);
        setInitialUI();
    }

    private void setInitialUI() {
        nameAddMedication_inputEditText = findViewById(R.id.nameAddMedication_inputEditText);
        textInputLayout_addMedication = findViewById(R.id.textInputLayout_addMedication);
        strengthAddMedication_inputEditText = findViewById(R.id.strengthAddMedication_inputEditText);
        strengthMedication_TextInputEditText = findViewById(R.id.strengthMedication_TextInputEditText);
        reason_inputEditText = findViewById(R.id.reason_inputEditText);
        reason_TextInputEditText = findViewById(R.id.reasonOfTakingTextView);
        formAddMedication_spinner = findViewById(R.id.formAddMedication_spinner);
        recurrencyAddMedication_spinner = findViewById(R.id.recurrencyAddMedication_spinner);
        instructionsAddMedication_spinner = findViewById(R.id.instructionsAddMedication_spinner);
        strenghtAddMedication_spinner = findViewById(R.id.strenghtAddMedication_spinner);
        startDatepickerAddmedication_Textview = findViewById(R.id.startDatepickerAddmedication_Textview);
        endDatePicker_textview = findViewById(R.id.endDatePicker_textview);
        timePicker_textview = findViewById(R.id.timePicker_textview);
        addMedication_button = findViewById(R.id.addMedication_button);
        datesAddMedication_cardview = findViewById(R.id.datesAddMedication_cardview);

        selectedDaysTextView = findViewById(R.id.dayTextView);
        selectedDaysTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDaysAlertDialog = new SelectDaysAlertDialog(AddMedicineActivity.this, new IUpdateText() {
                    @Override
                    public void updateText(String text) {
                        selectedDaysTextView.setText(text);
                    }
                }, recurrencyDaysNumber);
                addMedicinePresenterInterface.getSelectedDaysAlertdialog(selectDaysAlertDialog);
                selectDaysAlertDialog.showSelectDaysDialog();
            }
        });


        setSpinnerAdapter(formAddMedication_spinner, Constants.AddMedicineConstants.formType);
        setSpinnerAdapter(instructionsAddMedication_spinner, Constants.AddMedicineConstants.instructions);
        setSpinnerAdapter(recurrencyAddMedication_spinner, Constants.AddMedicineConstants.recurrency);
        setSpinnerAdapter(strenghtAddMedication_spinner, Constants.AddMedicineConstants.strength);
        DatePickerDialog.OnDateSetListener startDate = (view, year, month, day) -> {
            setMyCalendar(year, month, day);
            selectedStartDate = new DateTime(year, month + 1, day, selectedStartDate.getHourOfDay(), selectedStartDate.getMinuteOfHour());
            setStartDateTimeField();
        };
        DatePickerDialog.OnDateSetListener endDate = (view, year, month, day) -> {
            setMyCalendar(year, month, day);
            selectedEndDate = new DateTime(year, month + 1, day, selectedStartDate.getHourOfDay(), selectedStartDate.getMinuteOfHour());
            setEndDatePickerTimeField();
        };

        startDatepickerAddmedication_Textview.setOnClickListener(view -> {
            startDatepickerAddmedication_Textview.setClickable(false);
            DatePickerDialog dialog = new DatePickerDialog(this, startDate,
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
            if (dialog.isShowing())
                new Handler().postDelayed(() -> startDatepickerAddmedication_Textview.setClickable(true), 1000);

        });
        endDatePicker_textview.setOnClickListener(view -> {
            endDatePicker_textview.setClickable(false);
            DatePickerDialog dialog = new DatePickerDialog(this, endDate,
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
            if (dialog.isShowing())
                new Handler().postDelayed(() -> endDatePicker_textview.setClickable(true), 1000);
        });

        timePicker_textview.setOnClickListener(view -> {
            timePicker_textview.setClickable(false);
            new TimePickerDialog(this,
                    (view1, hourOfDay, minute) -> {
                        selectedStartDate = new DateTime(selectedStartDate.getYear(), selectedStartDate.getMonthOfYear(), selectedStartDate.getDayOfMonth(), hourOfDay, minute);
                        selectedEndDate = new DateTime(selectedEndDate.getYear(), selectedEndDate.getMonthOfYear(), selectedEndDate.getDayOfMonth(), hourOfDay, minute);
                        String AM_PM;
                        int hours = hourOfDay;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                            hours -= 12;
                        }
                        timePicker_textview.setText(hours + ":" + minute + " " + AM_PM);
                    }, hour, minute, false).show();
            new Handler().postDelayed(() -> timePicker_textview.setClickable(true), 1000);


        });

        addMedication_button.setOnClickListener(view -> {
            if (checkInputMedication()) {
                getMedicationFormValue();
            }
        });

        recurrencyAddMedication_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = adapterView.getSelectedItem().toString();
                if ((itemSelected.equals("Select Doses") |
                        itemSelected.equals("Every day") |
                        itemSelected.equals("Every 2 days") |
                        itemSelected.equals("Every 3 days") |
                        itemSelected.equals("Every 28 days"))) {
                    datesAddMedication_cardview.setVisibility(View.INVISIBLE);
                } else {
                    datesAddMedication_cardview.setVisibility(View.VISIBLE);
                    recurrencyDaysNumber = mapDosesNameWithNum(recurrencyAddMedication_spinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getMedicationFormValue() {
        Medicine medicine = new Medicine();
        medicine.setName(String.valueOf(nameAddMedication_inputEditText.getText()));
        medicine.setActive(true);
        medicine.setStrengthUnit(StrengthUnit.valueOf(strenghtAddMedication_spinner.getSelectedItem().toString()));
        medicine.setNumberOfUnits(Integer.parseInt(String.valueOf(strengthAddMedication_inputEditText.getText())));
        medicine.setInstruction(instructionsAddMedication_spinner.getSelectedItem().toString());
        medicine.setMedicineForm(formAddMedication_spinner.getSelectedItem().toString());
        medicine.setReasonOfTaking(String.valueOf(reason_inputEditText.getText()));
        List<DaysOfWeek> daysSelected = selectDaysAlertDialog.getSelectedDaysFromDialog();

        addMedicine(medicine, selectedStartDate, selectedEndDate.plusMinutes(2),
                RecurrencyModel.valueOf(recurrencyAddMedication_spinner.getSelectedItem().toString().replace(' ', '_')),
                daysSelected
        );
    }


    @Override
    public void addMedicine(Medicine medicine, DateTime startDate, DateTime endDate, RecurrencyModel recurrencyModel, List<DaysOfWeek> daysSelected) {

        RefillReminderDialog dialogRefillReminderBinding = new RefillReminderDialog(AddMedicineActivity.this, medicine.getMedId(), new RefillDialogRemindMeClickHandler() {
            @Override
            public void addRefillReminderToMedicine(long medicineId, int noOfDosages, int numberOfDosagesPerPack, int remindMeOn) {
                //method implementation
                addMedicinePresenterInterface.addMedicineToDbWithDosages(medicine, startDate, endDate, recurrencyModel, daysSelected, noOfDosages, numberOfDosagesPerPack, remindMeOn);
            }

            @Override
            public void addMedicineWithoutReminder() {
                addMedicinePresenterInterface.addMedicineToDB(medicine, startDate, endDate, recurrencyModel, daysSelected);
            }
        });
        dialogRefillReminderBinding.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogRefillReminderBinding.show();
        dialogRefillReminderBinding.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                AddMedicineActivity.this.finish();
            }
        });
    }

    private void setSpinnerAdapter(Spinner spinner, String[] formArray) {
        ArrayAdapter<String> formArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, formArray);
        formArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(formArrayAdapter);
    }

    private void setMyCalendar(int year, int month, int day) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, day);
    }

    private void setStartDateTimeField() {
        startDatepickerAddmedication_Textview.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void setEndDatePickerTimeField() {
        endDatePicker_textview.setText(dateFormat.format(myCalendar.getTime()));
    }

    private boolean checkInputMedication() {
        boolean isValid = false;
        if ((Objects.requireNonNull(nameAddMedication_inputEditText.getText()).length() > 0)) {
            isValid = true;
            textInputLayout_addMedication.setErrorEnabled(false);

        } else {
            textInputLayout_addMedication.setErrorEnabled(true);
            textInputLayout_addMedication.setError("enter name");
            return false;
        }
        if (Objects.requireNonNull(strengthAddMedication_inputEditText.getText().toString()).length() > 0) {
            isValid = true;
            strengthMedication_TextInputEditText.setErrorEnabled(false);
        } else {
            strengthMedication_TextInputEditText.setErrorEnabled(true);
            strengthMedication_TextInputEditText.setError("Enter Strength");
            return false;
        }
        if (Objects.requireNonNull(reason_inputEditText.getText()).length() > 0) {
            isValid = true;
            reason_TextInputEditText.setErrorEnabled(false);
        } else {
            reason_TextInputEditText.setErrorEnabled(true);
            reason_TextInputEditText.setError("Please specify reason");
            return false;
        }
        if (strenghtAddMedication_spinner.getSelectedItemPosition() != strenghtAddMedication_spinner.getItemIdAtPosition(0)) {
            isValid = true;
        } else {
            strenghtAddMedication_spinner.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (formAddMedication_spinner.getSelectedItemPosition() != formAddMedication_spinner.getItemIdAtPosition(0)) {
            isValid = true;
        } else {
            formAddMedication_spinner.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (recurrencyAddMedication_spinner.getSelectedItemPosition() != recurrencyAddMedication_spinner.getItemIdAtPosition(0)) {
            isValid = true;
        } else {
            recurrencyAddMedication_spinner.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (instructionsAddMedication_spinner.getSelectedItemPosition() != instructionsAddMedication_spinner.getItemIdAtPosition(0)) {
            isValid = true;
        } else {
            instructionsAddMedication_spinner.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (!startDatepickerAddmedication_Textview.getText().toString().equals(""))
            isValid = true;
        else {
            startDatepickerAddmedication_Textview.setHintTextColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (!endDatePicker_textview.getText().toString().equals(""))
            isValid = true;
        else {
            endDatePicker_textview.setHintTextColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (!timePicker_textview.getText().toString().equals(""))
            isValid = true;
        else {
            timePicker_textview.setHintTextColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (System.currentTimeMillis()+ 59 * 1000 > selectedStartDate.getMillis()) {
            Toast.makeText(this, "time should be at least 1 min from now", Toast.LENGTH_SHORT).show();
            return false;
        }
        return isValid;
    }

    private int mapDosesNameWithNum(String name) {
        int numberOfDays = 0;
        switch (name) {
            case "Once a week":
                numberOfDays = 1;
                break;
            case "Two days a week":
                numberOfDays = 2;
                break;
            case "Three days a week":
                numberOfDays = 3;
                break;
            case "Five days a week":
                numberOfDays = 5;
                break;
        }
        return numberOfDays;
    }


}