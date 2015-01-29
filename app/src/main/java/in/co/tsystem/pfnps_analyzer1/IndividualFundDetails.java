package in.co.tsystem.pfnps_analyzer1;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class IndividualFundDetails extends ActionBarActivity {

    private GraphicalView mChart;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private XYSeries mCurrentSeries;

    private XYSeriesRenderer mCurrentRenderer;
    private String csv;
    private String response;

    private static final String SOAP_ACTION = "urn:productlist#getProd";
    private static final String METHOD_NAME = "getProd";
    private static final String NAMESPACE = "urn:productlist";
    private static final String URL = "http://10.0.0.13/php/productlist.php?WSDL";

    myAsyncTask myRequest;

    private void initChart() {
        mCurrentSeries = new XYSeries("Sample Data");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        mRenderer.setPanEnabled(false, false);
    }

    private void addSampleData(String val) {
        // set data points from string
        //1,10;2,20;3,10;4,40;
        String arr[] = val.split(";");

        for (int i=0; i<arr.length; i++) {
            String d[] = arr[i].split(",");
            int x = Integer.parseInt(d[0]);
            int y = Integer.parseInt(d[1]);
            mCurrentSeries.add(x, y);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_fund_details);

        myRequest = new myAsyncTask();
        myRequest.execute();
    }


    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class myAsyncTask extends AsyncTask<Void, Void, Void>    {


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            if (mChart == null) {
                initChart();
                addSampleData(csv);
                XYSeriesRenderer.FillOutsideLine fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                fill.setColor(Color.BLUE);
                mCurrentRenderer.addFillOutsideLine(fill);
                mChart = ChartFactory.getCubeLineChartView(IndividualFundDetails.this, mDataset, mRenderer, 0.3f);
                layout.addView(mChart);
            } else {
                mChart.repaint();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            boolean is_result = false;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            //request.addProperty("iTopN", "5");
            request.addProperty("category", "data");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(URL);

            httpTransport.debug = true;
            Log.d("WEBSRV", "place 5");
            try {
                httpTransport.call(SOAP_ACTION, envelope);
            } catch (HttpResponseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("WEBSRV", "place 20");
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("WEBSRV", "place 21");
                return null;
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("WEBSRV", "place 22");
                return null;
            } //send request
            Log.d("WEBSRV", "place 4");

            // copy from http://stackoverflow.com/questions/19336099/ksoap-unable-to-casting-data-recovered-from-webservice
            //SoapPrimitive response1 = (SoapPrimitive) envelope
            //        .bodyIn;
            //response = response1.toString();
            //this is need to work with my internal setup
            response = envelope.bodyIn.toString();

            // return string looks like ...{return=1,10;2,20;}
            int csvstart = response.indexOf('=');
            int csvend = response.lastIndexOf(';');
            csv = response.substring(csvstart+1,csvend+1);

            return null;
        }
    }
}
