package com.iti.aurora.editmeds.view;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iti.aurora.R;
import com.iti.aurora.database.ConcreteLocalSource;
import com.iti.aurora.editmeds.presenter.EditMedicinePresenter;
import com.iti.aurora.editmeds.presenter.EditMedicinePresenterInterface;

import com.iti.aurora.model.Repository;
import com.iti.aurora.model.medicine.Medicine;

import com.iti.aurora.model.medicine.RecurrencyModel;
import com.iti.aurora.model.medicine.StrengthUnit;
import com.iti.aurora.model.medicine.Treatment;
import com.iti.aurora.utils.Constants;
import com.iti.aurora.utils.dialogs.TwoButtonsDialog;
import com.iti.aurora.utils.selectdays.DaysOfWeek;
import com.iti.aurora.utils.selectdays.IUpdateText;
import com.iti.aurora.utils.selectdays.SelectDaysAlertDialog;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EditMedsActivity extends AppCompatActivity implements EditMedicineViewInterface {

    TextInputEditText nameEditMedication_inputEditText,
            strengthEditMedication_inputEditText,
            reasonEdit_inputEditText;
    TextInputLayout textInputLayout_editMedication,
            strengthMedicationEdit_TextInputEditText,
            reasonEdit_TextInputEditText;
    Spinner formEditMedication_spinner,
            recurrencyEditMedication_spinner,
            instructionsEditMedication_spinner,
            strenghtEditMedication_spinner;
    CardView datesEditMedication_cardview;
    TextView startDatepickerEditmedication_Textview, endDateEditPicker_textview, timePickerEdit_textview;
    TextView selectedDaysEditTextView;
    Button editMedication_button;

    int recurrencyDaysNumber;
    Calendar myCalendar = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
    int minute = myCalendar.get(Calendar.MINUTE);

    SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
    SelectDaysAlertDialog selectDaysAlertDialog = new SelectDaysAlertDialog(this, new IUpdateText() {
        @Override
        public void updateText(String text) {
            selectedDaysEditTextView.setText(text);
        }
    });

    private DateTime selectedStartDate;
    private DateTime selectedEndDate;
    EditMedicinePresenterInterface editMedicinePresenterInterface;
    Medicine medicine;
    List<Treatment> treatments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_meds);
        selectedStartDate = new DateTime();
        selectedEndDate = new DateTime();
        editMedicinePresenterInterface = new EditMedicinePresenter(
                EditMedsActivity.this, Repository.getInstance(
                ConcreteLocalSource.getInstance(EditMedsActivity.this), EditMedsActivity.this)
        );
        //for using alarm manager
        editMedicinePresenterInterface.setContext(EditMedsActivity.this);
        medicine = (Medicine) getIntent().getSerializableExtra(Constants.MEDICINE_PASSING_FLAG);

        editMedicinePresenterInterface.getListOfTreatmentsFromRepo(medicine);

        setInitialUI();

    }

    private void setInitialUI() {
        nameEditMedication_inputEditText = findViewById(R.id.nameEditMedication_inputEditText);
        textInputLayout_editMedication = findViewById(R.id.textInputLayout_editMedication);
        strengthEditMedication_inputEditText = findViewById(R.id.strengthEditMedication_inputEditText);
        strengthMedicationEdit_TextInputEditText = findViewById(R.id.strengthEditMedication_TextInputEditText);
        reasonEdit_inputEditText = findViewById(R.id.reasonEdit_inputEditText);
        reasonEdit_TextInputEditText = findViewById(R.id.reasonOfTakingEditTextView);
        formEditMedication_spinner = findViewById(R.id.formEditMedication_spinner);
        recurrencyEditMedication_spinner = findViewById(R.id.recurrencyEditMedication_spinner);
        instructionsEditMedication_spinner = findViewById(R.id.instructionsEditMedication_spinner);
        strenghtEditMedication_spinner = findViewById(R.id.strenghtEditMedication_spinner);
        startDatepickerEditmedication_Textview = findViewById(R.id.startDatepickerEditmedication_Textview);
        endDateEditPicker_textview = findViewById(R.id.endEditDatePicker_textview);
        timePickerEdit_textview = findViewById(R.id.timePickerEdit_textview);
        editMedication_button = findViewById(R.id.editMedication_button);
        datesEditMedication_cardview = findViewById(R.id.datesEditMedication_cardview);

        selectedDaysEditTextView = findViewById(R.id.dayEditTextView);
        selectedDaysEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDaysAlertDialog = new SelectDaysAlertDialog(EditMedsActivity.this, new IUpdateText() {
                    @Override
                    public void updateText(String text) {
                        selectedDaysEditTextView.setText(text);
                    }
                }, recurrencyDaysNumber);
                editMedicinePresenterInterface.getSelectedDaysAlertdialog(selectDaysAlertDialog);
                selectDaysAlertDialog.showSelectDaysDialog();
            }
        });

        editMedicinePresenterInterface.getSelectedDaysAlertdialog(this.selectDaysAlertDialog);

        setSpinnerAdapter(formEditMedication_spinner, Constants.AddMedicineConstants.formType);
        setSpinnerAdapter(instructionsEditMedication_spinner, Constants.AddMedicineConstants.instructions);
        setSpinnerAdapter(recurrencyEditMedication_spinner, Constants.AddMedicineConstants.recurrency);
        setSpinnerAdapter(strenghtEditMedication_spinner, Constants.AddMedicineConstants.strength);
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

        startDatepickerEditmedication_Textview.setOnClickListener(view -> {
            startDatepickerEditmedication_Textview.setClickable(false);

            Toast.makeText(this, "can't edit start date ", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> startDatepickerEditmedication_Textview.setClickable(true), 1000);

        });
        endDateEditPicker_textview.setOnClickListener(view -> {
            endDateEditPicker_textview.setClickable(false);
            DatePickerDialog dialog = new DatePickerDialog(this, endDate,
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
            if (dialog.isShowing())
                new Handler().postDelayed(() -> endDateEditPicker_textview.setClickable(true), 1000);
        });
        timePickerEdit_textview.setOnClickListener(view -> {
            new TimePickerDialog(this,
                    (view1, hourOfDay, minute) -> {
                        timePickerEdit_textview.setClickable(false);

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
                        timePickerEdit_textview.setText(hours + ":" + minute + " " + AM_PM);
                    }, hour, minute, false).show();
            new Handler().postDelayed(() -> timePickerEdit_textview.setClickable(true), 1000);

        });

        editMedication_button.setOnClickListener(view -> {
            if (checkInputMedication()) {
                getMedicationFormValue(medicine);
                editMedicinePresenterInterface.getDosesByMedId(medicine.getMedId());
            }
        });

        recurrencyEditMedication_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = adapterView.getSelectedItem().toString();
                if ((itemSelected.equals("Select Doses") |
                        itemSelected.equals("Every day") |
                        itemSelected.equals("Every 2 days") |
                        itemSelected.equals("Every 3 days") |
                        itemSelected.equals("Every 28 days"))) {
                    datesEditMedication_cardview.setVisibility(View.INVISIBLE);
                } else {
                    datesEditMedication_cardview.setVisibility(View.VISIBLE);
                    recurrencyDaysNumber = mapDosesNameWithNum(recurrencyEditMedication_spinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setInitialValues(Medicine medicine, List<Treatment> treatments) {

        int formArrayPosition = Arrays.asList(Constants.AddMedicineConstants.formType).indexOf(medicine.getMedicineForm());
        int instructionArrayPosition = Arrays.asList(Constants.AddMedicineConstants.instructions).indexOf(medicine.getInstruction());
        int strengthArrayPosition = Arrays.asList(Constants.AddMedicineConstants.strength).indexOf(medicine.getStrengthUnit().toString());
        int selectDosesRecurrencyArrayPosition = Arrays.asList(Constants.AddMedicineConstants.recurrency).indexOf(treatments.get(0).getRecurrency().replace('_', ' '));
        String hourAndMinutesFormat = "hh:mm aa";
        SimpleDateFormat dateHourFormat = new SimpleDateFormat(hourAndMinutesFormat, Locale.US);
        String input = dateHourFormat.format(treatments.get(0).getStartDate());
        Date hourDate = null;
        String outPut = null;
        try {
            hourDate = dateHourFormat.parse(input);
            outPut = dateHourFormat.format(hourDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        nameEditMedication_inputEditText.setText(medicine.getName());
        strengthEditMedication_inputEditText.setText(String.valueOf(medicine.getNumberOfUnits()));
        reasonEdit_inputEditText.setText(String.valueOf(medicine.getReasonOfTaking()));
        strenghtEditMedication_spinner.setSelection(strengthArrayPosition);
        instructionsEditMedication_spinner.setSelection(instructionArrayPosition);
        formEditMedication_spinner.setSelection(formArrayPosition);
        startDatepickerEditmedication_Textview.setText(dateFormat.format(treatments.get(0).getStartDate()));
        endDateEditPicker_textview.setText(dateFormat.format(treatments.get(0).getEndDate()));
        recurrencyEditMedication_spinner.setSelection(selectDosesRecurrencyArrayPosition);
        timePickerEdit_textview.setText(outPut);
        selectedStartDate = new DateTime(treatments.get(0).getStartDate().getTime());
        selectedEndDate = new DateTime(treatments.get(0).getEndDate().getTime());
        showSelectedDays();
    }

    private void showSelectedDays() {
        if (treatments.get(0).getDaysList().size() != 0) {
            selectDaysAlertDialog.setSelectedDays(treatments.get(0).getDaysList());

            StringBuilder builder = new StringBuilder();
            for (DaysOfWeek day : treatments.get(0).getDaysList()) {
                builder.append(day.toString());
                builder.append(",");
            }
            builder.deleteCharAt(builder.lastIndexOf(","));
            selectedDaysEditTextView.setText(builder.toString());
        }


    }

    private void getMedicationFormValue(Medicine medicineToGet) {
        medicineToGet.setName(String.valueOf(nameEditMedication_inputEditText.getText()));
        medicineToGet.setActive(true);
        medicineToGet.setStrengthUnit(StrengthUnit.valueOf(strenghtEditMedication_spinner.getSelectedItem().toString()));
        medicineToGet.setNumberOfUnits(Integer.parseInt(String.valueOf(strengthEditMedication_inputEditText.getText())));
        medicineToGet.setInstruction(instructionsEditMedication_spinner.getSelectedItem().toString());
        medicineToGet.setMedicineForm(formEditMedication_spinner.getSelectedItem().toString());
        medicineToGet.setReasonOfTaking(String.valueOf(reasonEdit_inputEditText.getText()));
        List<DaysOfWeek> daysSelected = selectDaysAlertDialog.getSelectedDaysFromDialog();

        editMedicine(medicineToGet, selectedStartDate, selectedEndDate.plusMinutes(2),
                RecurrencyModel.valueOf(recurrencyEditMedication_spinner.getSelectedItem().toString().replace(' ', '_')),
                daysSelected
        );
    }


    @Override
    public void editMedicine(Medicine medicine, DateTime startDate, DateTime endDate, RecurrencyModel recurrencyModel, List<DaysOfWeek> daysSelected) {
        AlertDialog.Builder builder = TwoButtonsDialog.TwoButtonsDialogBuilder(EditMedsActivity.this,
                getResources().getString(R.string.edit_medication),
                getResources().getString(R.string.doYouWantToSaveMedicine),
                getResources().getString(R.string.edit_medication),
                getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editMedicinePresenterInterface.editMedicineToDB(medicine, startDate, endDate, recurrencyModel, daysSelected);
                        EditMedsActivity.this.finish();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }

        );
        builder.show();
        //Toast.makeText(EditMedsActivity.this, "Medicine Edited", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMedicine(Medicine medicine, List<Treatment> treatments) {
        this.medicine = medicine;
        this.treatments = treatments;
        setInitialValues(medicine, treatments);
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
        startDatepickerEditmedication_Textview.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void setEndDatePickerTimeField() {
        endDateEditPicker_textview.setText(dateFormat.format(myCalendar.getTime()));
    }

    private boolean checkInputMedication() {
        boolean isValid = false;
        if ((Objects.requireNonNull(nameEditMedication_inputEditText.getText()).length() > 0)) {
            isValid = true;
            textInputLayout_editMedication.setErrorEnabled(false);

        } else {
            textInputLayout_editMedication.setErrorEnabled(true);
            textInputLayout_editMedication.setError("enter name");
            return false;
        }
        if (Objects.requireNonNull(strengthEditMedication_inputEditText.getText().toString()).length() > 0) {
            isValid = true;
            strengthMedicationEdit_TextInputEditText.setErrorEnabled(false);
        } else {
            strengthMedicationEdit_TextInputEditText.setErrorEnabled(true);
            strengthMedicationEdit_TextInputEditText.setError("Enter Strength");
            return false;
        }
        if (Objects.requireNonNull(reasonEdit_inputEditText.getText()).length() > 0) {
            isValid = true;
            reasonEdit_TextInputEditText.setErrorEnabled(false);
        } else {
            reasonEdit_TextInputEditText.setErrorEnabled(true);
            reasonEdit_TextInputEditText.setError("Please specify reason");
            return false;
        }
        if (strenghtEditMedication_spinner.getSelectedItemPosition() != strenghtEditMedication_spinner.getItemIdAtPosition(0)) {
            isValid = true;
        } else {
            strenghtEditMedication_spinner.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (formEditMedication_spinner.getSelectedItemPosition() != formEditMedication_spinner.getItemIdAtPosition(0)) {
            isValid = true;
        } else {
            formEditMedication_spinner.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (recurrencyEditMedication_spinner.getSelectedItemPosition() != recurrencyEditMedication_spinner.getItemIdAtPosition(0)) {
            isValid = true;
        } else {
            recurrencyEditMedication_spinner.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (instructionsEditMedication_spinner.getSelectedItemPosition() != instructionsEditMedication_spinner.getItemIdAtPosition(0)) {
            isValid = true;
        } else {
            instructionsEditMedication_spinner.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (!startDatepickerEditmedication_Textview.getText().toString().equals(""))
            isValid = true;
        else {
            startDatepickerEditmedication_Textview.setHintTextColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (!endDateEditPicker_textview.getText().toString().equals(""))
            isValid = true;
        else {
            endDateEditPicker_textview.setHintTextColor(getResources().getColor(R.color.warning));
            return false;
        }
        if (!timePickerEdit_textview.getText().toString().equals(""))
            isValid = true;
        else {
            timePickerEdit_textview.setHintTextColor(getResources().getColor(R.color.warning));
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