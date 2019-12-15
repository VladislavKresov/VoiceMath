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
                    solve(formatLine(charSequence.toString()));
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
            String input = formatLine(commandlist.get(0).toString());
            if (input.contains(")^"))
            et_task.setText("("+input);
            else et_task.setText(input);

        } else Toast.makeText(this, getText(R.string.err_request), Toast.LENGTH_SHORT).show();

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void solve(String input){
        try {
            String[] roots;
            if(input.contains(")^")) {
                roots = Solver.powered(input).split("or");
            } else if(input.contains("x^2")){
                roots = Solver.quadratic(input).split("or");
            } else{
                roots = Solver.linear(input).split("or");
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

    private String formatLine(String line) {

        line = line.replaceAll("((И|и)кс|(X|x))","x");
        line = line.replaceAll("(В|в)с(ё|е) в",")^");
        line = line.replaceAll(" (В|в) ","^");
        line = line.replaceAll("(К|к)вадрат","^2");

        line = line.replaceAll("(П|п)люс","+");
        line = line.replaceAll("(М|м)инус","-");

        line = line.replaceAll("(Н|н)(о|у)л(ь|ю)","0");
        line = line.replaceAll("(О|о)дин","1");
        line = line.replaceAll("(Д|д)ва","2");
        line = line.replaceAll("(Т|т)ри","3");
        line = line.replaceAll("(Ч|ч)етыре","4");
        line = line.replaceAll("(П|п)ять","5");
        line = line.replaceAll("(Ш|ш)есть","6");
        line = line.replaceAll("(С|с)емь","7");
        line = line.replaceAll("(В|в)осемь","8");
        line = line.replaceAll("(Д|д)евять","9");
        line = line.replaceAll("(Р|р)авно"," = ");

        line = line.replaceAll("[a-w]","");
        line = line.replaceAll("[y-z]","");
        line = line.replaceAll("[A-W]","");
        line = line.replaceAll("[Y-Z]","");
        line = line.replaceAll("[а-я]","");
        line = line.replaceAll("[А-Я]","");
        line = line.replaceAll(" ","");
        line = line.replaceAll("(-\\+|\\+-)","-");
        line = line.replaceAll("(\\+\\+|--)","+");
        line = line.replaceAll("\\(","");

        return line;
    }


    private void initViews() {
        tv_answer = findViewById(R.id.tv_result);
        et_task = findViewById(R.id.et_task);
        btn_mic = findViewById(R.id.btn_mic);
    }
}
