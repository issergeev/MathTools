package ru.issergeev.mathtools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RelativeLayout parent;
    Spinner currentNS, newNS;
    RadioButton RBmanual, RBselect;
    RadioGroup inputSelector;
    EditText num, NSText1, NSText2;
    Button convertButton;
    ListView results;

    String number = "";
    int CNS = 2;
    int NNS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_adapter,
                R.id.Text, getResources().getStringArray(R.array.Digits));
        final ArrayAdapter<String> resultArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        parent = (RelativeLayout) findViewById(R.id.ParentLayout);
        num = (EditText) findViewById(R.id.inputNumber);
        currentNS = (Spinner) findViewById(R.id.currentNS);
        newNS = (Spinner) findViewById(R.id.newNS);
        RBmanual = (RadioButton) findViewById(R.id.manualI);
        RBselect = (RadioButton) findViewById(R.id.selectFL);
        NSText1 = (EditText) findViewById(R.id.currentNSText);
        NSText2 = (EditText) findViewById(R.id.newNSText);
        inputSelector = (RadioGroup) findViewById(R.id.Selector);
        convertButton = (Button) findViewById(R.id.translateButton);
        results = (ListView) findViewById(R.id.ResultsList);
        currentNS.setAdapter(arrayAdapter);
        newNS.setAdapter(arrayAdapter);
        results.setAdapter(resultArrayAdapter);

        inputSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.selectFL:
                        currentNS.setVisibility(View.VISIBLE);
                        newNS.setVisibility(View.VISIBLE);
                        NSText1.setVisibility(View.GONE);
                        NSText2.setVisibility(View.GONE);
                        break;
                    case R.id.manualI:
                        currentNS.setVisibility(View.GONE);
                        newNS.setVisibility(View.GONE);
                        NSText1.setVisibility(View.VISIBLE);
                        NSText2.setVisibility(View.VISIBLE);
                        break;
                    default:
                        currentNS.setVisibility(View.VISIBLE);
                        newNS.setVisibility(View.VISIBLE);
                        NSText1.setVisibility(View.GONE);
                        NSText2.setVisibility(View.GONE);
                        break;
                }
            }
        });

        currentNS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    CNS = Integer.valueOf(adapterView.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.inputWarning),
                            Toast.LENGTH_SHORT).show();
                }
            }
        );

        newNS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                NNS = Integer.valueOf(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.inputWarning),
                Toast.LENGTH_SHORT).show();
            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NSText1.getVisibility() == View.VISIBLE){
                    CNS = Integer.valueOf(NSText1.getText().toString());
                    NNS = Integer.valueOf(NSText2.getText().toString());
                }

                number = num.getText().toString().trim().replaceAll(",", ".").toUpperCase();
                resultArrayAdapter.add(convert(number, CNS, NNS).toString());
                Toast.makeText(MainActivity.this, convert(number, CNS, NNS), Toast.LENGTH_LONG).show();
            }
        });
    }

    private StringBuffer convert(String number, int CNS, int NNS) {
        StringBuffer Answer = new StringBuffer("");
        StringBuffer modernResult = new StringBuffer("");
        String Result = number;
        int k = 0;

        if (NNS == CNS)
            return new StringBuffer(number);

        if (checkNumber(number)) {
            if (CNS != 10) {
                number = new StringBuilder(number).reverse().toString();
                int Ppos = finder(number);
                Result = "";
                k = -Ppos;

                for (int c = 0; c < Ppos; c++) {
                    if (Character.isDigit(number.charAt(c))) {
                        Result += Integer.valueOf(String.valueOf(number.charAt(c))) * Math.pow(CNS, k);
                    } else
                        Result += (number.charAt(c) - 55) * Math.pow(CNS, k);
                    k++;
                }

                if (Ppos != 0) {
                    Ppos++;
                }

                for (int i = Ppos; i < number.length(); i++) {
                    if (Character.isDigit(number.charAt(i))) {
                        Result += Integer.valueOf(String.valueOf(number.charAt(i))) * Math.pow(CNS, k);
                    } else
                        Result += (number.charAt(i) - 55) * Math.pow(CNS, k);
                    k++;
                }
            }

            return toNew(Double.valueOf(Result), NNS);
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.incorrectInputWarning), Toast.LENGTH_SHORT).show();
            //Answer.append(getResources().getString(R.string.error));
        }

        return null;
    }

    private StringBuffer toNew(Double number, int NNS) {
        StringBuffer Answer = new StringBuffer("");
        ArrayList<Integer> complexNumber = new ArrayList<Integer>(10);
        double num = number;
        while (num >= NNS) {
            complexNumber.add((int) (num % NNS));
            num = (int) Math.floor(num / NNS);
        }

        complexNumber.add((int) num);

        for (int i = 0; i < complexNumber.size(); i++) {
            if (complexNumber.get(i) > 9) {
                Answer.append(complexNumber.get(i) + 55);
            } else
                Answer.append(complexNumber.get(i));
        }

        return Answer.reverse();
    }

    private int finder(String number) {
        for (int i = 0; i < number.length(); i++){
            if (number.charAt(i) == 46)
                return i;
        }
        return 0;
    }

    private boolean checkNumber(String number){
        char s;
        int counter = 0;


        for (int i = 0; i < number.length(); i++){
            s = number.charAt(i);

            if (s < 44 || s == 45 || s == 47 || s > 57 && s < 65 || s > 90) {
                return false;
            }

            if (s == 46)
                counter++;

            if (counter > 1)
                return false;
        }

        return true;
    }
}