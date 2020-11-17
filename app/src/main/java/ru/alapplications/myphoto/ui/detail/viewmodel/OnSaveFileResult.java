package ru.alapplications.myphoto.ui.detail.viewmodel;

/**
 * Колбэк для обработки результатов операции с файлом
 */
interface OnSaveFileResult {
    void onOk();
    void onError();
}
