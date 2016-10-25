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

        DivideAsync tiles = new DivideAsync();
        tiles.execute(new DivideParams(map, size));
    }


    private class DivideParams {
        Bitmap map;
        int size;

        DivideParams(Bitmap map, int size) {
            this.map = map;
            this.size = size;

        }
    }

//    public Bitmap divideInTiles(Bitmap image, int titleSize) {
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//
//        int width = image.getWidth() / titleSize;
//        int height = image.getHeight() / titleSize;
//
//        int[] array = new int[width * height]; //The array of tiles
//        /* An image can be represented by an array of int like this :
//
//            imagine your image is 2x2 pixel.
//            Each pixel has a color represented by an int
//
//            the array representation of that image would be : {top left pixel, topright pixel, bottom left pixel, bottomright pixel}
//
//            basically, each line of pixel in an array of int, and you add each line one by one at the end of the array.
//           */
//        try {
////            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(stream.toByteArray(), 0, stream.size(), false);
//
//            int line = 0;
//            int column = 0;
//
//            for (int y = 0; y < image.getHeight(); y += titleSize) {
//                for (int x = 0; x < image.getWidth(); x += titleSize) {
//                    if (x + titleSize < image.getWidth() && y + titleSize < image.getHeight()) {
//
//                        Bitmap tile = Bitmap.createBitmap(image, x, y, titleSize, titleSize); // This gives a tile of the original image
//                        int colour = getAverageColor(tile);             // This gives you the average color
////                        Log.i("anish", "colour = " + String.format("%06X", 0xFFFFFF & colour));   //This gives you the hex code
//                        tile.recycle();                                             // Free memory
//
//                        int currentIndex = (line * width) + column;
//                        if (currentIndex < width * height)
//                            array[currentIndex] = colour;                            // Add color to current index in array
//
//                        column++;
//                    }
//                }
//                line++;
//                column = 0;
//            }
//            return generateBitmapFromArray(array, width, height);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

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

            bmp = Bitmap.createBitmap(imgwidth, imgHeight, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bmp);
            paint = new Paint(Paint.FILTER_BITMAP_FLAG);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 30, stream);



            width = imgwidth / tileSize;
            ArrayList<String> array = new ArrayList<>();

            try {

                for (int y = 0; y < img.getHeight(); y += tileSize) {
                    for (int x = 0; x < img.getWidth(); x += tileSize) {
                        if (x + tileSize <= img.getWidth() && y + tileSize <= img.getHeight()) {

                            Bitmap tile = Bitmap.createBitmap(img, x, y, tileSize, tileSize); // This gives a tile of the original image
                            Bitmap newBitmap = Bitmap.createScaledBitmap(tile, 1, 1, true);
                            tile.recycle();

                            String color = String.format("%06X", 0xFFFFFF & newBitmap.getPixel(0, 0));// This gives you the average color and converts to HEX
                            array.add(color);
                            // Free memory
                            newBitmap.recycle();

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
                    LoadImageTileFromServer getTileFromServer = new LoadImageTileFromServer();
                    ConstructParams test = new ConstructParams((url + urlInis), i);

                    getTileFromServer.execute(test);
                    i++;
                }

            }
        }
    }

    public interface ConstructBitmapListener {
        void constructBitmapListener(Bitmap img);
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

                canvas.drawBitmap(downloadBitmap(params[0].url), x * tileSize, y * tileSize, paint);
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
