package com.example.calculatorpro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;
import java.util.Arrays;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity {
    ImageButton backspace;
    Button all_clear,percentage,divide,multiply,minus,add,equalsTo,point,zero,add_minus,one,two,three,four,five,six,seven,eight,nine;
    TextView history, result;
    String equation, answer, final_answer;
    DecimalFormat df;
    double aDouble;
    boolean check;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        check =false;
        df =new DecimalFormat("#.##");

        history =findViewById(R.id.history);

        result =findViewById(R.id.result);
        equation = "";

        backspace =findViewById(R.id.backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!equation.isEmpty()){
                    char[] charArray = equation.toCharArray();
                    char[] newCharArray = Arrays.copyOfRange(charArray, 0, charArray.length - 1);
                    equation = new String(newCharArray);
                    if(equation.isEmpty()){
                        result.setText("0");
                    }
                    else{
                        result.setText(equation);
                    }

                    Log.d("button", "onClick: backspace");
                }
            }
        });

        all_clear =findViewById(R.id.all_clear);
        all_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation = "";
                result.setText("0");
                history.setText("");
                Log.d("button", "onClick: all clear");
            }
        });

        percentage =findViewById(R.id.percentage);
        percentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!equation.isEmpty()){
                    set_equation("%");
                }
                Log.d("button", "onClick: percentage");
            }
        });

        divide =findViewById(R.id.divide);
        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!equation.isEmpty()){
                    set_equation("÷");
                }
                Log.d("button", "onClick: divide");
            }
        });

        multiply =findViewById(R.id.multiply);
        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!equation.isEmpty()){
                    set_equation("×");
                }
                Log.d("button", "onClick: multiply");
            }
        });

        minus =findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!equation.isEmpty()){
                    set_equation("-");
                }
                Log.d("button", "onClick: minus");
            }
        });

        add =findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!equation.isEmpty()){
                    set_equation("+");
                }
                Log.d("button", "onClick: add");
            }
        });

        point =findViewById(R.id.point);
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation(".");
                Log.d("button", "onClick: point");
            }
        });

        zero =findViewById(R.id.zero);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("0");
            }
        });

        one =findViewById(R.id.one);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("1");
            }
        });

        two =findViewById(R.id.two);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("2");
            }
        });

        three =findViewById(R.id.three);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("3");
            }
        });

        four =findViewById(R.id.four);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("4");
            }
        });

        five =findViewById(R.id.five);
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("5");
            }
        });

        six =findViewById(R.id.six);
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("6");
            }
        });

        seven =findViewById(R.id.seven);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("7");
            }
        });

        eight =findViewById(R.id.eight);
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("8");
            }
        });

        nine =findViewById(R.id.nine);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_equation("9");
            }
        });


        add_minus=findViewById(R.id.add_minus);
        add_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!equation.isEmpty()){
                    char firstChar = equation.charAt(0);
                    if(firstChar == '-'){
                        equation= equation.substring(0,0) + equation.substring(1);
                        result.setText(equation);
                    }
                    if(firstChar != '-'){
                        equation= "-" + equation;
                        result.setText(equation);
                    }
                }
                else{
                    equation= "-" + equation;
                    result.setText(equation);
                }
            }
        });


        equalsTo =findViewById(R.id.equalTo);
        equalsTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!equation.isEmpty()){
                    check = CheckEquation(equation);
                    if(check){
                        history.setText(equation);
                        equation = equation.replaceAll("%", "/100");
                        equation = equation.replaceAll("×","*");
                        equation = equation.replaceAll("÷", "/");
                        Context rhino = Context.enter();
                        rhino.setOptimizationLevel(-1);
                        Scriptable scriptable = rhino.initStandardObjects();
                        answer ="";
                        answer = rhino.evaluateString(scriptable, equation, "Javascript", 1, null).toString();
                        aDouble = Double.parseDouble(answer);
                        final_answer= df.format(aDouble);
                        result.setText(final_answer);
                        equation="";
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Invalid Equation", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    result.setText("0");
                }
            }
        });

    }

    private void set_equation(String element){
        equation = equation + element;
        result.setText(equation);
    }

    private boolean CheckEquation(String equation){
        String lastChar = equation.substring(equation.length() - 1);
        char[] charArray = equation.toCharArray();
        if(lastChar.equals("÷")){
            return false;
        }
        if(lastChar.equals("-")){
            return false;
        }
        if(lastChar.equals("+")){
            return false;
        }
        if(lastChar.equals("×")){
            return false;
        }
        return true;
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(MainActivity.this, "Landscape Mode", Toast.LENGTH_SHORT).show();
//        }
//        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(MainActivity.this, "Portrait Mode", Toast.LENGTH_SHORT).show();
//        }
//    }

}