package in.co.tsystem.pfnps_analyzer1;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


public class TopPerformingFunds extends Activity {
        private GraphicalView mChart;

        private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

        private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        private XYSeries mCurrentSeries;

        private XYSeriesRenderer mCurrentRenderer;

        private void initChart() {
            mCurrentSeries = new XYSeries("Sample Data");
            mDataset.addSeries(mCurrentSeries);
            mCurrentRenderer = new XYSeriesRenderer();
            mRenderer.addSeriesRenderer(mCurrentRenderer);
        }

        private void addSampleData() {
            mCurrentSeries.add(1, 2);
            mCurrentSeries.add(2, 3);
            mCurrentSeries.add(3, 2);
            mCurrentSeries.add(4, 5);
            mCurrentSeries.add(5, 4);
        }

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_top_performing_funds);
        }

        protected void onResume() {
            super.onResume();
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            if (mChart == null) {
                initChart();
                addSampleData();
                mChart = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer, 0.3f);
                layout.addView(mChart);
            } else {
                mChart.repaint();
            }
        }
}