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
    private Button btnSort;
    private static final int MAX_LENGTH = 10000;
    private int[] numbers = new int[MAX_LENGTH];
    SimpleAsyncTask sat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvMessage = findViewById(R.id.txvMessage);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sat.cancel(true);
            }
        });
        btnCancel.setVisibility(View.INVISIBLE);
        btnSort = findViewById(R.id.btnSort);

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
        //execWithThread();
        //OPCION 3: ASYNCTASK
        sat = new SimpleAsyncTask();
        sat.execute();
    }

    private void execWithThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bubbleSort();
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
     */
    private void bubbleSort() {
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
    private class  SimpleAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onCancelled() {
            super.onCancelled();
            btnCancel.setVisibility(View.INVISIBLE);
            btnSort.setEnabled(true);
            txvMessage.setText("Operación cancelada");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnCancel.setVisibility(View.VISIBLE);
            btnSort.setEnabled(false);
        }

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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            txvMessage.setText(values[0] + "%");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            btnCancel.setVisibility(View.INVISIBLE);
            btnSort.setEnabled(true);
            txvMessage.setText("Operación terminada");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sat != null && sat.getStatus() == AsyncTask.Status.RUNNING)
            sat.cancel(true);
    }
}
