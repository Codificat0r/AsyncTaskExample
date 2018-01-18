package com.example.asynctaskexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HiddenActivity extends AppCompatActivity implements HiddenFragment.TaskCallbacks {

    private TextView txvMessage;
    private Button btnCancel;
    private Button btnSort;
    private HiddenFragment fragment;
    private ProgressBar progressBar;
    private HiddenFragment.ProgressBarTask actualTask;
    private static final String FRAGMENT = "fragment";
    private static final String TXV = "txv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden);
        txvMessage = findViewById(R.id.txvMessage);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.INVISIBLE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actualTask != null) {
                    actualTask.cancel(true);
                }
            }
        });
        btnSort = findViewById(R.id.btnSort);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TXV, txvMessage.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txvMessage.setText(savedInstanceState.getString(TXV));
        if (fragment == null) {
            try {
                fragment = (HiddenFragment) getFragmentManager().findFragmentByTag(FRAGMENT);
                //Si ha conseguido el fragment porque ya se habia metido uno no dara excepcion
                //al buscar por tags y entonces podrá ejecutar lo de abajo. Si no se ha añadido
                //ningun fragment todavia, no se ejecuta estas demas lineas por lo que no
                //intentará obtener cosas del fragment nulo. Por eso no hace falta otro if.
                actualTask = fragment.getProgressBarTask();
                if (fragment.getProgressBarTask().getStatus() == AsyncTask.Status.RUNNING) {
                    btnCancel.setVisibility(View.VISIBLE);
                    btnSort.setEnabled(false);
                }
            } catch (Exception e) {

            }
        }
    }


    public void onClickSort(View v) {
        fragment = new HiddenFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(fragment, FRAGMENT);
        ft.commit();
    }

    @Override
    public void onPreExecute(HiddenFragment.ProgressBarTask task) {
        btnCancel.setVisibility(View.VISIBLE);
        btnSort.setEnabled(false);
        txvMessage.setText("");
        actualTask = task;
    }

    @Override
    public void onProgressUpdate(int i) {
        progressBar.setProgress(i);
    }

    @Override
    public void onCancelled() {
        btnCancel.setVisibility(View.INVISIBLE);
        btnSort.setEnabled(true);
        progressBar.setProgress(0);
        txvMessage.setText("Operación cancelada");
    }

    @Override
    public void onPostExecute() {
        btnCancel.setVisibility(View.INVISIBLE);
        btnSort.setEnabled(true);
        txvMessage.setText("Operación terminada");
        actualTask = null;
    }
}
