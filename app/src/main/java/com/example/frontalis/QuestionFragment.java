package com.example.frontalis;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontalis.Common.Common;
import com.example.frontalis.Interface.IQuestion;
import com.example.frontalis.Model.CurrentQuestion;
import com.example.frontalis.Model.Question;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements IQuestion {

    private TextView txt_question_text;
    private CheckBox c1, c2, c3, c4;
    private Question question;
    private int questionIndex = -1;
    public QuestionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_question, container, false);

        questionIndex = getArguments().getInt("index", -1);
        question = Common.questionList.get(questionIndex);
        if (question != null){
            txt_question_text = itemView.findViewById(R.id.txt_question_text);
            c1 = itemView.findViewById(R.id.checkBox1);
            c2 = itemView.findViewById(R.id.checkBox2);
            c3 = itemView.findViewById(R.id.checkBox3);
            c4 = itemView.findViewById(R.id.checkBox4);
            c1.setText(question.getAnswerA());
            c2.setText(question.getAnswerB());
            c3.setText(question.getAnswerC());
            c4.setText(question.getAnswerD());

            txt_question_text.setText(question.getQuestionText());

            c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                        Common.selected_values.add(c1.getText().toString());
                    else Common.selected_values.remove(c1.getText().toString());
                }
            });
            c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                        Common.selected_values.add(c2.getText().toString());
                    else Common.selected_values.remove(c2.getText().toString());
                }
            });
            c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                        Common.selected_values.add(c3.getText().toString());
                    else Common.selected_values.remove(c3.getText().toString());
                }
            });
            c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                        Common.selected_values.add(c4.getText().toString());
                    else Common.selected_values.remove(c4.getText().toString());
                }
            });
        }
        return itemView;
    }

    @Override
    public CurrentQuestion getSelectedAnswer() {
        CurrentQuestion currentQuestion = new CurrentQuestion(questionIndex, Common.ANSWER_TYPE.NO_ANSWER);
        StringBuilder result = new StringBuilder();
        if (Common.selected_values.size() > 1){
            Object[] arrayAnswer = Common.selected_values.toArray();
            for (int i = 0; i < arrayAnswer.length; i++){
                if (i < arrayAnswer.length - 1)
                    result.append(new StringBuilder((String)arrayAnswer[i]).substring(0, 1)).append(",");
                else
                    result.append(new StringBuilder((String)arrayAnswer[i]).substring(0, 1));
            }
        }
        else if (Common.selected_values.size() == 1){
            Object[] arrayAnswer = Common.selected_values.toArray();
            result.append((String)arrayAnswer[0]).substring(0, 1);
        }
        if (question != null){
            if (!TextUtils.isEmpty(result)){
                if (result.toString().equals(question.getCorrectAnswer()))
                    currentQuestion.setType(Common.ANSWER_TYPE.RIGHT_ANSWER);
                else
                    currentQuestion.setType(Common.ANSWER_TYPE.WRONG_ANSWER);
            }
            else
                currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }else{
            Toast.makeText(getContext(), "Cannot get question", Toast.LENGTH_SHORT).show();
            currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        /*StringBuilder result = new StringBuilder();
        if (Common.selected_values.size() == 1){
            Object[] arrayAnswer = Common.selected_values.toArray();
            result.append((String)arrayAnswer[0]).substring(0, 1);
        }
        if (question != null){
            if (!TextUtils.isEmpty(result)){
                if (result.toString().equals(question.getCorrectAnswer()))
                    currentQuestion.setType(Common.ANSWER_TYPE.RIGHT_ANSWER);
                else
                    currentQuestion.setType(Common.ANSWER_TYPE.WRONG_ANSWER);
            }else{
                currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
            }
        }else{
            Toast.makeText(getContext(), "Cannot get question", Toast.LENGTH_SHORT).show();
            currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);

        }*/
        //Toast.makeText(getContext(), String.valueOf(currentQuestion.getType()), Toast.LENGTH_SHORT).show();
        Common.selected_values.clear();
        return currentQuestion;
    }

    @Override
    public void showCorrectAnswer() {
        String[] correctAnswer = question.getCorrectAnswer().split(",");
        for (String answer: correctAnswer){
            if (answer.equals("A")){
                c1.setTypeface(null, Typeface.BOLD);
                c1.setTextColor(Color.RED);
            }
            if (answer.equals("B")){
                c2.setTypeface(null, Typeface.BOLD);
                c2.setTextColor(Color.RED);
            }
            if (answer.equals("C")){
                c3.setTypeface(null, Typeface.BOLD);
                c3.setTextColor(Color.RED);
            }
            if (answer.equals("D")){
                c4.setTypeface(null, Typeface.BOLD);
                c4.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public void disableAnswer() {
        c1.setEnabled(false);
        c2.setEnabled(false);
        c3.setEnabled(false);
        c4.setEnabled(false);
    }

    @Override
    public void resetQuestion() {

        c1.setEnabled(true);
        c2.setEnabled(true);
        c3.setEnabled(true);
        c4.setEnabled(true);

        c1.setChecked(false);
        c2.setChecked(false);
        c3.setChecked(false);
        c4.setChecked(false);

        c1.setTypeface(null, Typeface.NORMAL);
        c1.setTextColor(Color.BLACK);
        c2.setTypeface(null, Typeface.NORMAL);
        c2.setTextColor(Color.BLACK);
        c3.setTypeface(null, Typeface.NORMAL);
        c3.setTextColor(Color.BLACK);
        c4.setTypeface(null, Typeface.NORMAL);
        c4.setTextColor(Color.BLACK);
    }
}
