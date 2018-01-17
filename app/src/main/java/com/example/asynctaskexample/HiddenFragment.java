package com.example.asynctaskexample;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class HiddenFragment extends Fragment {

    private TaskCallbacks callback;
    private static final int MAX_LENGTH = 10000;
    private int[] numbers = new int[MAX_LENGTH];

    interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int i);
        void onCancelled();
        void onPostExecute();
    }

    private void generateNumbers() {
        Random random = new Random();
        for (int i = 0; i < MAX_LENGTH; i++) {
            numbers[i] = random.nextInt();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hacemos que se guarde el estado automaticamente
        setRetainInstance(true);
        //Generamos los numeros
        generateNumbers();
        //Se inicia la tarea
        ProgressBarTask progressBarTask = new ProgressBarTask();
        progressBarTask.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TaskCallbacks) {
            callback = (TaskCallbacks) activity;
        } else {
            throw new ClassCastException(activity.getLocalClassName() + " debe implementar TaskCallbacks");
        }
    }

    public class ProgressBarTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            int aux;
            int vecesARecorrer = numbers.length;
            for (int i = 0; i < numbers.length; i++) {
                if (!isCancelled())
                {
                    int percent = (int)((((float)(i)) / vecesARecorrer) * 100);
                    publishProgress(percent);
                } else {
                    break;
                }
                for (int j=i; j < numbers.length-1; j++) {
                    if (numbers[i] > numbers[j]) {
                        aux = numbers[i];
                        numbers[i] = numbers[j];
                        numbers[j] = aux;
                    }
                }
            }
            //Si no se cancela se usará la barra de progreso

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (callback != null) {
                callback.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);
            if (callback != null)
                callback.onPostExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (callback != null)
                callback.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (callback != null)
                callback.onProgressUpdate(values[0]);
        }
    }
}
