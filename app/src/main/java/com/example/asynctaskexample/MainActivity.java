package com.example.asynctaskexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView txvMessage;
    private ProgressBar progressBar;
    private Button btnCancel;
    private static final int MAX_LENGTH = 20000;
    private int[] numbers = new int[MAX_LENGTH];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvMessage = findViewById(R.id.txvMessage);

        generateNumbers();

    }

    /**
     * Método que se ejecuta al hacer click en el boton de ordenar
     * @param v
     */
    public void onClickSort(View v) {
        /*OPCION 1. SE OBTIENE EL MENSAJE DE ERROR ANR (Application Not Responding) (Con varios clicks)...
        AHORA LO HAREMOS CON HILOS.
        bubbleSort(numbers);
        txvMessage.setText("Operación terminada");*/
        //OPCION 2. CREAR UN HILO PARA LA EJECUCION DE LA ORDENACION POR BURBUJA
        execWithThread();
    }

    private void execWithThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bubbleSort(numbers);
                //Para actualizar la interfaz desde un hilo debemos crear otro:
                //OPCION 1:
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txvMessage.setText("Operación terminada");
                    }
                });*/
                //OPCION 2:
                txvMessage.post(new Runnable() {
                    @Override
                    public void run() {
                        txvMessage.setText("Operación terminada");
                    }
                });
            }
        }).start();
    }

    private void generateNumbers() {
        Random random = new Random();
        for (int i = 0; i < MAX_LENGTH; i++) {
            numbers[i] = random.nextInt();
        }
    }

    /**
     * Método que ordena un array mediante el algoritmo de la burbuja
     * @param numbers
     */
    private void bubbleSort(int[] numbers) {
        int aux;
        for (int i = 0; i < numbers.length; i++) {
            for (int j=i+1; j < numbers.length-1; j++) {
                if (numbers[i] > numbers[j]) {
                    aux = numbers[i];
                    numbers[i] = numbers[j];
                    numbers[j] = aux;
                }
            }
        }
    }

    //Todos los metodos se ejecutan en el hilo de la interfaz grafica meno doInBackground
    private class  SimpleAsyncTask extends AsyncTask<> {

    }
}
