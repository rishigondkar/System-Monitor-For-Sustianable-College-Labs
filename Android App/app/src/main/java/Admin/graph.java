package Admin;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.system_stats.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Calendar;

public class graph extends AppCompatActivity {

    PointsGraphSeries<DataPoint> xySeries;

    LineGraphSeries<DataPoint> xySeries2;

    private Handler handler = new Handler();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference lab = db.collection("LAB").document("LAB");

    private DocumentReference pc = lab.collection("lab7").document("pc2");


    GraphView graphView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        graphView = findViewById(R.id.graph);

        runnable.run();
        pc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                    Toast.makeText(graph.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                else {
                    if (documentSnapshot != null && documentSnapshot.exists()) {

//                        int hours = (Integer.valueOf((String) documentSnapshot.get("upTime"))) / 3600;
//                        int minutes = (Integer.valueOf((String) documentSnapshot.get("upTime")) % 3600) / 60;
//                        int seconds = Integer.valueOf((String) documentSnapshot.get("upTime")) % 60;
//                        Toast.makeText(graph.this, hours + "Hr " + minutes + "min " + seconds + "sec", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(graph.this, Integer.parseInt(String.valueOf(documentSnapshot.get("upTime")))+"+"+Integer.valueOf(String.valueOf(documentSnapshot.get("upTime"))), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(graph.this,Float.parseFloat(String.valueOf(documentSnapshot.get("upTime")))+"+"+Float.parseFloat(String.valueOf(documentSnapshot.get("Used_memory"))), Toast.LENGTH_SHORT).show();
//                        plot2(Float.parseFloat(String.valueOf(documentSnapshot.get("upTime"))),Float.parseFloat(String.valueOf(documentSnapshot.get("Used_memory"))));
//                        plot2(Integer.valueOf(String.valueOf(documentSnapshot.get("upTime"))), Float.valueOf(String.valueOf(documentSnapshot.get("Used_memory"))));
                    }

                }
            }
        });

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Calendar cal = Calendar.getInstance();
            final String crntHr = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
            final String crntMin = String.valueOf(cal.get(Calendar.MINUTE));
            String crntSec = String.valueOf(cal.get(Calendar.SECOND));
            pc.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            plot2(Integer.valueOf(String.valueOf(documentSnapshot.get("upTime"))), Float.valueOf(String.valueOf(documentSnapshot.get("Used_memory"))));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(graph.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            handler.postDelayed(this, 5000);
        }
    };

//    private void plot1() {
//        ArrayList<String> xAxes = new ArrayList<>(Arrays.asList("13", "14", "15",
//                "16", "17", "18",
//                "19", "20", "21",
//                "22"));
//        ArrayList<String> yAxes = new ArrayList<>(Arrays.asList("2.6542", "3.0948", "2.4905", "3.7311", "3.3191", "4.5125", "5.7827", "3.8885",
//                "3.9115", "1.9625"));
//
//        LineChart lineChart;
//        lineChart = findViewById(R.id.lineChart);
//
//
//        ArrayList<Entry> graph = new ArrayList<>();
//        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
//        for (int i = 0; i < xAxes.size(); i++) {
//            graph.add(new Entry(Float.parseFloat(xAxes.get(i)), Float.parseFloat(yAxes.get(i))));
//        }
//
//        LineDataSet lineDataSets1 = new LineDataSet(graph, "CPU Usage");
//        lineDataSets1.setDrawCircles(false);
//        lineDataSets1.setColor(Color.BLUE);
//
//        lineDataSets.add(lineDataSets1);
//
//        lineChart.setData(new LineData(lineDataSets));
//        lineChart.setVisibleXRangeMaximum(1000);
//        lineChart.setVerticalScrollBarEnabled(true);
//        lineChart.fitScreen();
//    }

    private void plot2(int x, float y) {
//        xySeries = new PointsGraphSeries<>();
//        xySeries.appendData(new DataPoint(x,y),true,1000);
//
//        xySeries.setShape(PointsGraphSeries.Shape.POINT);
//        xySeries.setColor(Color.BLUE);
//        xySeries.setSize(10f);
//
//        graphView.getViewport().setScalable(true);
//        graphView.getViewport().setScalableY(true);
//        graphView.getViewport().setScrollable(true);
//        graphView.getViewport().setScrollableY(true);

//        graphView.setFitsSystemWindows(true);
//        //set manual x bounds
//        graphView.getViewport().setYAxisBoundsManual(true);
//        graphView.getViewport().setMaxY(8);
//        graphView.getViewport().setMinY(0);
//
//        //set manual y bounds
//        graphView.getViewport().setXAxisBoundsManual(true);
//        graphView.getViewport().setMaxX(103000);
//        graphView.getViewport().setMinX(101000);
        xySeries2 = new LineGraphSeries<>();
        xySeries2.appendData(new DataPoint(x, y), true, 1000);

        xySeries2.setTitle("Random Curve 1");
        xySeries2.setColor(Color.BLUE);
        xySeries2.setDataPointsRadius(10);
        xySeries2.setThickness(8);
//        xySeries2.setDrawAsPath(true);

        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScrollableY(true);
        graphView.setVerticalScrollBarEnabled(true);
        graphView.getGridLabelRenderer().setLabelVerticalWidth(8);
        graphView.getGridLabelRenderer().setLabelVerticalWidth(5);
        graphView.getGridLabelRenderer().setVerticalLabelsVisible(true);
        graphView.setHorizontalScrollBarEnabled(true);


        graphView.getViewport().setMaxY(8);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().scrollToEnd();
        graphView.setFitsSystemWindows(true);

// custom paint to make a dotted line
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
//        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        xySeries2.setCustomPaint(paint);

        graphView.addSeries(xySeries2);
        xySeries2.setDrawDataPoints(true);

    }

}
