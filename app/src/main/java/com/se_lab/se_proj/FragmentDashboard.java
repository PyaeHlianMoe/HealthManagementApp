package com.se_lab.se_proj;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.os.AsyncTask;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;
import android.support.annotation.NonNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import android.graphics.Color;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import java.util.ArrayList;
import com.se_lab.se_proj.model.Suggestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Random;
import android.os.Handler;


public class FragmentDashboard extends Fragment implements SensorEventListener {

    SensorManager sensorManager;
    boolean running = false;
    TextView tv_steps,tv_distance,tv_calories;
    View view;
    float stepsCount;
    TextView tv_meal;
    FirebaseFirestore db;

    private static final String TAG = "Dashboard";
    private ArrayList<Suggestion> suggestionList = new ArrayList<>();
    Suggestion s;


    public static float evsteps;
    public static int cont = 0;
    /**
     * Maximum value for each data series in the {@link DecoView}. This can be different for each
     * data series, in this example we are applying the same all data series
     */
    public static float mSeriesMax = 0f;

    /**
     * DecoView animated arc based chart
     */
    private DecoView mDecoView;
    /**
     * Data series index used for controlling animation of {@link DecoView}. These are set when
     * the data series is created then used in {@link #createEvents} to specify what series to
     * apply a given event to
     */
    private int mBackIndex;
    private int mSeries1Index;
    private TextView textView;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tv_steps = (TextView) view.findViewById(R.id.tv_steps);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_calories = (TextView) view.findViewById(R.id.tv_calories);

        tv_meal = (TextView) view.findViewById(R.id.tv_meal);


        db = FirebaseFirestore.getInstance();
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        //new RetrieveFeedTask().execute();


        mSeriesMax = 7000;

        mDecoView = (DecoView) view.findViewById(R.id.dynamicArcView);

        Log.d("mSeries out", (String.valueOf(mSeriesMax)));
        if (mSeriesMax > 0) {
            Log.d("mSeries out in", (String.valueOf(mSeriesMax)));
            // Create required data series on the DecoView
            createBackSeries();
            createDataSeries1();

            // Setup events to be fired on a schedule
            createEvents();
        }

        getSuggestion();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Integer randomInt = randomInteger(1, suggestionList.size());
                Log.d(TAG, String.valueOf(suggestionList.size()));
                Log.d(TAG, String.valueOf(randomInt));

                tv_meal.setText(suggestionList.get(randomInt-1).getSuggestion());
                //tv_category.setText("");
                //tv_instructions.setText("");
            }
        }, 5000);

        return view;
    }

    public static int randomInteger(int min, int max) {
        Random rd = new Random();
        return rd.nextInt((max - min) + 1) + min;
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getActivity(), "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            /*stepsCount = event.values[0];
            tv_steps.setText(String.valueOf(stepsCount));
            Integer progress = Math.round(stepsCount / 7000);
            progressBar.setProgress(50);//progress);*/

            textView = (TextView) view.findViewById(R.id.textRemaining);
            textView.setText(String.valueOf(Math.round(event.values[0])));
            evsteps = event.values[0];


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        Log.d("mSeries Data1", (String.valueOf(mSeriesMax)));

        final TextView textPercentage = (TextView) view.findViewById(R.id.textPercentage);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
                tv_distance.setText(String.valueOf(Math.round(currentPosition * 0.000762)) + " KM");
                tv_calories.setText(String.valueOf(Math.round(currentPosition * 0.023)) + " CAL");
                tv_steps.setText(String.valueOf(Math.round(currentPosition)) + " Steps");
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
        final TextView textToGo = (TextView) view.findViewById(R.id.textRemaining);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textToGo.setText(String.format("%d Steps to goal", (int) (seriesItem.getMaxValue() - currentPosition)));

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        //final TextView textActivity1 = (TextView) view.findViewById(R.id.textActivity1);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                //textActivity1.setText(String.format("%.0f Steps", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createEvents() {
        cont++;
        mDecoView.executeReset();

        if (cont == 1) {
            resetText();
            mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                    .setIndex(mSeries1Index)
                    .setDelay(0)
                    .setDuration(3000)
                    .setDisplayText("")
                    .setListener(new DecoEvent.ExecuteEventListener() {
                        @Override
                        public void onEventStart(DecoEvent decoEvent) {

                        }

                        @Override
                        public void onEventEnd(DecoEvent decoEvent) {
                            createEvents();
                        }
                    })
                    .build());
        }
        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(3000)
                .setDelay(100)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(evsteps)
                .setIndex(mSeries1Index)
                .setDelay(3250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(120000)
                .setDuration(3000)
                .setDisplayText("")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        createEvents();
                    }
                })
                .build());

    }
    private void resetText() {
        ((TextView) view.findViewById(R.id.textPercentage)).setText("");
        ((TextView) view.findViewById(R.id.textRemaining)).setText("");
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


    private void getSuggestion() {
        //Log.d(TAG, goal);
        db.collection("suggestionList")
                //.whereEqualTo("module", module)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId());
                        suggestionList.add(document.toObject(Suggestion.class));
                    }
                } else {
                    Log.d(TAG, "Failed to retrieve");
                }
            }
        });
    }
}