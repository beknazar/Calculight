package kr.ac.unist.calculight;

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.jeval.*;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "Calculight";
    private String rezultat = "0";
    private String donationURL = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=CVHGYACZ3E4MQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        ImageView mDonationButton = (ImageView) findViewById(R.id.donationButton);
        // Button buttonFadeOut = (Button)findViewById(R.id.fadeout);

        Typeface dinFont = Typeface.createFromAsset(getAssets(), "fonts/din.ttf");
        TextView mShareText = (TextView) findViewById(R.id.shareText);
        TextView mDonateText = (TextView) findViewById(R.id.donateText);
        TextView mExpressionInput = (TextView) findViewById(R.id.expressionInput);

        mShareText.setTypeface(dinFont);
        mDonateText.setTypeface(dinFont);
        mExpressionInput.setTypeface(dinFont);

        final LinearLayout mHeartLayout = (LinearLayout) findViewById(R.id.heartLayout);
        mHeartLayout.setVisibility(LinearLayout.INVISIBLE);

        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        mDonationButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Log.i(APP_NAME, "Button clicked");
                mHeartLayout.startAnimation(animationFadeIn);
                mHeartLayout.setVisibility(LinearLayout.VISIBLE);
                mHeartLayout.postDelayed(new Runnable() {
                    public void run() {
                        Log.i(APP_NAME, "delayed part");
                        mHeartLayout.setVisibility(LinearLayout.INVISIBLE);
                    }
                }, 5000);
            }
        });

        mDonateText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(donationURL));
                startActivity(intent);
            }
        });

        mShareText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Try Calculight! It's an instant calculation app.");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download from http://calculight.io/");
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

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
