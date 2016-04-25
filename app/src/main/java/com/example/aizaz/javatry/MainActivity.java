package com.example.aizaz.javatry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class MainActivity extends AppCompatActivity {

    private String[] states;
    Spinner sp;
    Button train,predict;
    TextView textView,textView2,textView3;
    Instances data = null;
    Classifier cls ;
    Evaluation eval = null;
    Random rand = new Random(1);  // using seed = 1
    int folds = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        train= (Button) findViewById(R.id.button);
        textView= (TextView) findViewById(R.id.textView);
        textView2= (TextView) findViewById(R.id.textView2);
        textView3= (TextView) findViewById(R.id.textView3);

        sp= (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.brew_array,
                        android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Apply the adapter to the spinner
        sp.setAdapter(staticAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String Text = sp.getSelectedItem().toString();
               // Toast.makeText(getBaseContext(),"++++"+Text,Toast.LENGTH_SHORT).show();
                if(Text.equals("SVM")){
                    cls = new LibSVM();
                }
                else if(Text.equals("RandomForest")){
                    cls = new RandomForest();
                }
                else{
                    cls = new J48();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new FileReader("/sdcard/features.arff"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            data = new Instances(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // setting class attribute
        data.setClassIndex(data.numAttributes() - 1);

        try {
            eval = new Evaluation(data);
        } catch (Exception e) {
            e.printStackTrace();
        }



        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    eval.fMeasure(1);

                    eval.crossValidateModel(cls, data, folds, rand);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    textView.setText(eval.toMatrixString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Log.e("matrix", " "+ eval.toMatrixString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textView2.setText(eval.toSummaryString());
                    textView3.setText(eval.toClassDetailsString());
                    //textView3.setText((int) eval.avgCost());
                  //  textView3.setText((int) eval.weightedFMeasure());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Log.e("matrix2", " "+ eval.toSummaryString());

                } catch (Exception e) {
                    e.printStackTrace();
                }



                Toast.makeText(getApplicationContext(), "Classifier Build", Toast.LENGTH_SHORT).show();
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
