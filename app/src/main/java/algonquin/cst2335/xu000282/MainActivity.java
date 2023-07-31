package algonquin.cst2335.xu000282;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import algonquin.cst2335.xu000282.databinding.ActivityMainBinding;

/**
 * @author Longpeng Xu
 * @version 1.0
 * The MainActivity class represents the main activity of the Android application.
 */
public class MainActivity extends AppCompatActivity {


    //for sending network requests:
    RequestQueue queue = null;

    protected String cityName;
    protected Bitmap image;
    /**
     * The onCreate method is called when the activity is being created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//do what the parent does

        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater() );


        //This part goes at the top of the onCreate function:
        queue = Volley.newRequestQueue(this); //like a constructor

        //loads the screen
        setContentView( binding.getRoot() );


        binding.forecastButton.setOnClickListener(click -> {

            cityName = binding.cityTextField.getText().toString();

            //server name and parameters:       name = value&name2=value2&name3=value3
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" +
                    URLEncoder.encode(cityName) //replace spaces, &. = with other characters
                    +  "&appid=314767a062e0590014a8d49c6bf9d528&units=metric";

            //this goes in the button click handler:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (successfulResponse) -> {
                        try {
                            JSONArray weatherArray = successfulResponse.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);

                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");

                            JSONObject main = null;
                            main = successfulResponse.getJSONObject("main");
                            double temp = main.getDouble("temp");
                            double min = main.getDouble("temp_min");
                            double max = main.getDouble("temp_max");
                            int humidity = main.getInt("humidity");
                            //String description = main.getString("description");
                            runOnUiThread( (  )  -> {
                                binding.temp.setText("The current temperature is " + temp + " degrees");
                                binding.temp.setVisibility(View.VISIBLE);

                                binding.maxTemp.setText("The max temperature is " + max + " degrees");
                                binding.maxTemp.setVisibility(View.VISIBLE);

                                binding.minTemp.setText("The min temperature is " + min + " degrees");
                                binding.minTemp.setVisibility(View.VISIBLE);

                                binding.humidity.setText("The humidity is: " + humidity + "%");
                                binding.humidity.setVisibility(View.VISIBLE);


                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);
                            });

                            String pictureURL = "http://openweathermap.org/img/w/" + iconName + ".png";

                            String pathname = getFilesDir() + "/" +iconName + ".png";
                            File file  = new File(pathname);
                            if(file.exists()){
                                image = BitmapFactory.decodeFile(pathname);
                                binding.icon.setImageBitmap(image);
                                binding.icon.setVisibility(View.VISIBLE);
                            }
                            else {
                                ImageRequest imgReq = new ImageRequest(pictureURL, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        int i = 0;
                                        binding.icon.setImageBitmap(bitmap);
                                        binding.icon.setVisibility(View.VISIBLE);


                                        FileOutputStream fOut = null;
                                        try {
                                            fOut = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);

                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                            fOut.flush();
                                            fOut.close();

                                            image = bitmap;
                                            image.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput
                                                    (iconName + ".png", Activity.MODE_PRIVATE));

                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();

                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }

                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                        (error) -> {
                                            int i = 0;

                                        });

                                queue.add(imgReq);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }, //gets called if it is successful
                    (vError) -> {
                        int i = 0;
                    }  ); //gets called if there is an error

            queue.add(request); //run the web query

        });




    }

}