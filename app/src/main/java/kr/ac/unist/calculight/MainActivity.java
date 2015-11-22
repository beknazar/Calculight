package kr.ac.unist.calculight;

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.jeval.*;

public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "Calculight";
    private String rezultat = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        ClipboardManager clipboard = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);
        String pasteData = "0";

//        TextView mExpr = (TextView)findViewById(R.id.expr);
//        TextView mAnswer = (TextView)findViewById(R.id.answer);
//
//
//        // If it does contain data, decide if you can handle the data.
//        if (!(clipboard.hasPrimaryClip())) {
//            mExpr.setText("Clipboard is empty");
//        } else {
//
//            //since the clipboard contains plain text.
//            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
//
//            // Gets the clipboard as text.
//            pasteData = item.getText().toString();
//            mExpr.setText(pasteData);
//
//            Evaluator evaluator = new Evaluator();
//            try {
//                rezultat = evaluator.evaluate(pasteData);
//            } catch (net.sourceforge.jeval.EvaluationException e) {
//                Toast.makeText(this, "Nu merge :( ", Toast.LENGTH_SHORT).show();
//                Log.d(APP_NAME, e.getMessage());
//            }
//            mAnswer.setText(rezultat);
//        }
//
        Intent intent1 = new Intent(this, CalculightMonitor.class);
        this.startService(intent1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
