package com.example.voiceinputmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1111;
    private TextView et_task;
    private TextView tv_answer;
    private Button btn_mic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();

    }

    private void initListeners() {
        btn_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeak();
            }
        });
        et_task.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()) {
                    solve(Formatter.formatLine(charSequence.toString()));
                } else {
                    tv_answer.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_task.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_task.getRight() - et_task.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        et_task.setText("");
                        tv_answer.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void startSpeak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Произнесите уравнение, например 'икс квадрат равно ноль'");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK){
            ArrayList commandlist = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String input = Formatter.formatLine(commandlist.get(0).toString());
            if (input.contains(")^"))
            et_task.setText("("+input);
            else et_task.setText(input);

        } else Toast.makeText(this, getText(R.string.err_request), Toast.LENGTH_SHORT).show();

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void solve(String input){
        try {
            Solver solver = new Solver(input);
            String[] roots;
            if(input.contains(")^")) {
                roots = solver.powered(input).split("or");
            } else if(input.contains("x^2")){
                roots = solver.quadratic(input).split("or");
            } else{
                roots = solver.linear(input).split("or");
            }
            if(roots[0]=="")
                throw new IllegalArgumentException();
            if(!roots[0].contains("No")){
                String answer;
                if(roots[0].contains("Inf"))
                    answer = getText(R.string.inf_roots).toString();
                else if(roots.length == 1)
                        answer = getText(R.string.one_root).toString()+"\n"+roots[0];
                    else {
                        answer = getText(R.string.more_roots).toString();
                        for (int i = 0; i < roots.length; i++) {
                        answer+="\n"+"X"+(i+1)+" = "+roots[i];
                        }
                    }
                    tv_answer.setText(answer);
            }else {
                tv_answer.setText(getText(R.string.no_roots));
            }
        } catch (Exception e){
            tv_answer.setText(getText(R.string.unsolveble));
        }
    }




    private void initViews() {
        tv_answer = findViewById(R.id.tv_result);
        et_task = findViewById(R.id.et_task);
        btn_mic = findViewById(R.id.btn_mic);
    }
}
