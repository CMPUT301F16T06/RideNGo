package assignment1.ridengo;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class UserVehicleInfoActivity extends AppCompatActivity {

    final List<String> colorList = Arrays.asList("Black","White","Silver","Brown","Grey","Red","Blue","Yellow","Green","Other");
    protected String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_vehicle_info);

        username = getIntent().getStringExtra("username");
        final User user = UserController.getUserList().getUserByUsername(username);

        final Spinner yearSpinner = (Spinner) findViewById(R.id.YearSpinner);
        final Spinner colorSpinner = (Spinner) findViewById(R.id.ColorSpinner);
        final EditText makeText = (EditText) findViewById(R.id.MakeEditText);
        final EditText modelText = (EditText) findViewById(R.id.ModelEditText);
        final EditText pNumText = (EditText) findViewById(R.id.PNumEditText);
        Button confirmButton = (Button) findViewById(R.id.confirmButton);

        if(user.haveVehicle()){
            Vehicle userVehicle = user.getVehicle();
            yearSpinner.setSelection(2017-userVehicle.getYear());
            colorSpinner.setSelection(colorList.indexOf(userVehicle.getColor()));
            makeText.setText(userVehicle.getMake());
            modelText.setText(userVehicle.getModel());
            pNumText.setText(userVehicle.getPlateNum());
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String year = yearSpinner.getSelectedItem().toString();
                int y;
                if(year.equals("2005/before")){
                    y = 2005;
                }
                else{
                    y = Integer.parseInt(year);
                }

                String color = colorSpinner.getSelectedItem().toString();
                String make = makeText.getText().toString().trim();
                String model = modelText.getText().toString().trim();
                String pNum = pNumText.getText().toString().trim();

                Vehicle v = new Vehicle(pNum,y,make,model,color);
                user.setVehicle(v);

                Intent intent = new Intent(UserVehicleInfoActivity.this, UserInfoActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Are you sure to get back to the previous page? Information updated may be lost.");
        adb.setCancelable(true);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(UserVehicleInfoActivity.this,UserInfoActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        adb.show();
    }
}