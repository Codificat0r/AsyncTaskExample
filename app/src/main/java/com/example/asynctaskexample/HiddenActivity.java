package com.example.asynctaskexample;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HiddenActivity extends AppCompatActivity implements HiddenFragment.TaskCallbacks {

    private TextView txvMessage;
    private Button btnCancel;
    private Button btnSort;
    private HiddenFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden);
        txvMessage = findViewById(R.id.txvMessage);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.INVISIBLE);
        btnSort = findViewById(R.id.btnSort);
        fragment = new HiddenFragment();
    }

    public void onClickSort(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(fragment, "HiddenFragment");
        ft.commit();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int i) {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute() {

    }
}
