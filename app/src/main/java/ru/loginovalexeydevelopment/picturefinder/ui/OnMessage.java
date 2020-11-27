package ru.loginovalexeydevelopment.picturefinder.ui;


/**
 * Интерфейс для передачи параметров диалога
 */
public interface OnMessage {
    void showMessage ( String title ,
                       String message,
                       OnDialogResponse onDialogResponse );
}
