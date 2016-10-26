package wiseowl.com.au.pix_art;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by anish.patel on 20/10/2016.
 */

public class DivideTiles {
    ConstructBitmapListener listener;
    Bitmap bmp;
    Canvas canvas;
    Paint paint;

    int imgwidth;
    int imgHeight;
    int width;
    int arraySize;
    int tileSize;

    public DivideTiles(Bitmap map, int size) {

        DivideAsync tiles = new DivideAsync();                                                      //Start the tile devision in async task
        tiles.execute(new DivideParams(map, size));                                                 //Pass params to Async 'new DivideParams(map, size)'
    }


    private class DivideParams {
        Bitmap map;
        int size;

        DivideParams(Bitmap map, int size) {
            this.map = map;
            this.size = size;

        }
    }


    public class DivideAsync extends AsyncTask<DivideParams, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<String> doInBackground(DivideParams... params) {
            Bitmap img = params[0].map;
            tileSize = params[0].size;
            imgwidth = img.getWidth();
            imgHeight = img.getHeight();

            bmp = Bitmap.createBitmap(imgwidth, imgHeight, Bitmap.Config.ARGB_8888);                //Setup new global bitmap
            canvas = new Canvas(bmp);                                                               //Add Bitmap to canvas so it can modified when server returns a tile
            paint = new Paint(Paint.FILTER_BITMAP_FLAG);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 30, stream);



            width = imgwidth / tileSize;
            ArrayList<String> array = new ArrayList<>();

            try {

                for (int y = 0; y < img.getHeight(); y += tileSize) {
                    for (int x = 0; x < img.getWidth(); x += tileSize) {
                        if (x + tileSize <= img.getWidth() && y + tileSize <= img.getHeight()) {

                            Bitmap tile = Bitmap.createBitmap(img, x, y, tileSize, tileSize);       // This gives a tile of the original image
                            Bitmap newBitmap = Bitmap.createScaledBitmap(tile, 1, 1, true);

                            tile.recycle();                                                         //Free memory

                            String color = String.format("%06X", 0xFFFFFF & newBitmap.getPixel(0, 0));// This gives you the average color and converts to HEX
                            array.add(color);

                            newBitmap.recycle();                                                    // Free memory

                        }
                    }
                }
                return array;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<String> ints) {
            super.onPostExecute(ints);
            String url = "http://10.0.2.2:8765/color/"+tileSize+"/"+tileSize+"/";
            arraySize = ints.size();

            int i = 0;

            for (String urlInis : ints) {
                if (urlInis != null) {
                    ConstructParams constructParams = new ConstructParams((url + urlInis), i);      //Create params for pulling from the server and reconstructing the bitmap

                    LoadImageTileFromServer getTileFromServer = new LoadImageTileFromServer();
                    getTileFromServer.execute(constructParams);
                    i++;
                }

            }
        }
    }

    public interface ConstructBitmapListener {                                                      //Listner that is setup in MainActivity. When the Bitmap is finished it will
        void constructBitmapListener(Bitmap img);                                                   //pass it to the activity ready to be rendered on screen.
    }

    public void ConstructBitmapListener(ConstructBitmapListener listener) {
        this.listener = listener;

    }


    public class LoadImageTileFromServer extends AsyncTask<ConstructParams, Void, Integer> {

        public LoadImageTileFromServer() {}

        int pos = 0;

        @Override
        protected Integer doInBackground(ConstructParams... params) {
            try {
                pos = params[0].pos;

                int y = pos / width;
                int x = pos - (y * width);
                canvas.drawBitmap(downloadBitmap(params[0].url), x * tileSize, y * tileSize, paint);//Draw each tile to the global canvas with correct X & Y pos
                return pos;
            } catch (Exception e) {
                // log error
                e.getLocalizedMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer done) {
            if (done == (arraySize - 1) && listener != null) {
                listener.constructBitmapListener(bmp);
            }
        }

        //Get the PNG from the Node server Using HttpURLConnection
        private Bitmap downloadBitmap(String myUrl) {
            HttpURLConnection conn = null;
            InputStream is = null;

            try {
                URL url = new URL(myUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(500);
                conn.setConnectTimeout(1000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                is = conn.getInputStream();

                if (is != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    return bitmap;
                }
            } catch (Exception e) {
                conn.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + myUrl);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }
    }

}
