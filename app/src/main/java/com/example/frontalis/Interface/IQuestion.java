package com.example.frontalis.Interface;

import com.example.frontalis.Model.CurrentQuestion;

public interface IQuestion {
    CurrentQuestion getSelectedAnswer();
    void showCorrectAnswer();
    void disableAnswer();
    void resetQuestion();

}
