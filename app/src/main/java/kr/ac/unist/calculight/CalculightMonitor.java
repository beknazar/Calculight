package kr.ac.unist.calculight;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.jeval.Evaluator;

public class CalculightMonitor extends Service {


    private final String TAG = "CALCULIGHT";
    private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            performClipboardCheck();
        }
    };
    private Evaluator evaluator = new Evaluator();

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
    public void onCreate() {
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("CALCULIGHT", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void performClipboardCheck() {
        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {

//            ClipData cd = cb.getPrimaryClip();
            ClipData.Item item = cb.getPrimaryClip().getItemAt(0);

            // Gets the clipboard as text.
            String pasteData = item.getText().toString(); // getText() returns CharSequence
            Log.i("CALCULIGHT", "Rabotaet clipboard");

            if (pasteData.matches("[0-9 .]+")) {
                Log.i("CALCULIGHT", "Only number");
                return;
            }

            try {
                String rezultat = evaluator.evaluate(pasteData);
                if (rezultat.equals(pasteData)) {
                    Log.i("CALCULIGHT", "rezultat.equals(pasteData)");
                    return;
                }

                String[] arr = {"==", "!=", "&&", "||", "<", ">", "<=", ">="};
                String rezultatToShow = "0";
                if (stringContainsItemFromList(pasteData, arr) && (rezultat.equals("1.0") || rezultat.equals("0.0"))) {
                    rezultatToShow = rezultat.equals("1.0") ? "TRUE" : "FALSE";
                }
                else {
                    rezultatToShow = String.format("%.2f", evaluator.getNumberResult(pasteData));
                }

                LayoutInflater inflater =  LayoutInflater.from(this);
                // Inflate the Layout
                View layout = inflater.inflate(R.layout.result_toast, null);

                TextView text = (TextView) layout.findViewById(R.id.textToShow);

                Typeface dinFont = Typeface.createFromAsset(getAssets(), "fonts/din.ttf");
                text.setTypeface(dinFont); // setFont to beautiful font

                // Set the Text to show in TextView
                text.setText(Html.fromHtml("<font color='#ff1a75'>[</font>" + rezultatToShow + "<font color='#ff1a75'>]</font>"));

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

                cb.setText(rezultatToShow);


            } catch (net.sourceforge.jeval.EvaluationException e) {
                Toast.makeText(this, "Nu merge :( ", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.getMessage());
            }



        }
    }
}
