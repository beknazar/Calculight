package kr.ac.unist.calculight;

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
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
    private int fontSize, colorSetIndex, maxChild = 4;
    private String[] colorSet = {"#737373", "#8c8c8c", "#a6a6a6", "#b3b3b3", "#cccccc"};

    private static boolean stringContainsItemFromList(String inputString, String[] items)
    {
        for(int i =0; i < items.length; i++)
        {
            if(inputString.contains(items[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface dinFont = Typeface.createFromAsset(getAssets(), "fonts/din.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(dinFont);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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

        Intent intent1 = new Intent(this, CalculightMonitor.class);
        this.startService(intent1);

        ImageView mDonationButton = (ImageView) findViewById(R.id.donationButton);
        // Button buttonFadeOut = (Button)findViewById(R.id.fadeout);

        TextView mShareText = (TextView) findViewById(R.id.shareText);
        TextView mDonateText = (TextView) findViewById(R.id.donateText);
        final TextView mExpressionInput = (TextView) findViewById(R.id.expressionInput);

        mShareText.setTypeface(dinFont);
        mDonateText.setTypeface(dinFont);
        mExpressionInput.setTypeface(dinFont);

        final FrameLayout mExpressionInputLayout = (FrameLayout) findViewById(R.id.expressionInputLayout);
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
                        mHeartLayout.startAnimation(animationFadeOut);
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


        /* Typing clear button appear */
        final TextView mCleanX = (TextView) findViewById(R.id.cleanX);
        mCleanX.setVisibility(TextView.INVISIBLE);

        mExpressionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Log.i(APP_NAME, "onTextChanged: " + s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Log.i(APP_NAME, "beforeTextChanged: " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String in = s.toString();

                if (in.equals("")) {
                    mCleanX.setVisibility(TextView.INVISIBLE);
                } else if (mCleanX.getVisibility() == TextView.INVISIBLE) {
                    // mCleanX.setVisibility(TextView.VISIBLE);
                    mCleanX.startAnimation(animationFadeIn);
                    mCleanX.setVisibility(TextView.VISIBLE);
                }

                Log.i(APP_NAME, "afterTextChanged: " + s.toString());
            }
        });

        mCleanX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpressionInput.setText("");
            }
        });

        /* Show result when Key pressed */
        mExpressionInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String in = mExpressionInput.getText().toString();

                    // If it does contain data, decide if you can handle the data.
                    if (!in.equals("")) {
                        Evaluator evaluator = new Evaluator();
                        try {
                            rezultat = evaluator.evaluate(in);
                        } catch (net.sourceforge.jeval.EvaluationException e) {
                            // Toast.makeText(getApplicationContext(), "Nu merge :( ", Toast.LENGTH_SHORT).show();
                            Log.d(APP_NAME, e.getMessage());
                            return false;
                        }
                        // Toast.makeText(getApplicationContext(), in, Toast.LENGTH_LONG).show();

                        ViewGroup mAnswerContainer = (ViewGroup) findViewById(R.id.answerContainer);

                        LinearLayout newAnswer = new LinearLayout(getApplicationContext());

                        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        newAnswer.setLayoutParams(parms);
                        newAnswer.setOrientation(LinearLayout.VERTICAL);

                        TextView mNewAnswer = new TextView(getApplicationContext());

                        String[] arr = {"==", "!=", "&&", "||", "<", ">", "<=", ">="};
                        String rezultatToShow = "0";
                        if (stringContainsItemFromList(in, arr) && (rezultat.equals("1.0") || rezultat.equals("0.0"))) {
                            rezultatToShow = rezultat.equals("1.0") ? "TRUE" : "FALSE";
                        }
                        else {
                            try {
                                rezultatToShow = String.format("%.2f", evaluator.getNumberResult(in));
                            } catch (net.sourceforge.jeval.EvaluationException e) {
                                Log.d(APP_NAME, e.getMessage());
                            }
                        }
                        // Save answer to clipboard
                        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cb.setText(rezultatToShow);

                        mNewAnswer.setText(Html.fromHtml("<font color='#ff1a75'>[</font>" + rezultatToShow + "<font color='#ff1a75'>]</font>"));
                        mNewAnswer.setTextSize(30);
                        mNewAnswer.setTextColor(Color.BLACK);

                        Log.i(APP_NAME, "Children: " + String.valueOf(mAnswerContainer.getChildCount()));

                        fontSize = 25;
                        colorSetIndex = 0;
                        int childCount = mAnswerContainer.getChildCount();
                        for (int i = 0; i < Math.min(maxChild, childCount); ++i) {
                            LinearLayout lastAddedLayout = (LinearLayout) mAnswerContainer.getChildAt(i);
                            TextView lastAdded = (TextView) lastAddedLayout.getChildAt(0);
                            lastAdded.setTextSize(fontSize);
                            if (i == 0) {
                                String tempStr = lastAdded.getText().toString();
                                int tempStrLen = tempStr.length();
                                lastAdded.setText(tempStr.substring(1, tempStrLen - 1));
                            }
                            fontSize -= 3;
                            lastAdded.setTextColor(Color.parseColor(colorSet[colorSetIndex++]));
                        }

                        newAnswer.addView(mNewAnswer);

                        mAnswerContainer.addView(newAnswer, 0);
                    }
                    return true;
                }
                return false;
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
        if (id == R.id.menu_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
