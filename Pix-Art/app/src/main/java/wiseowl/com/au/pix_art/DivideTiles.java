package wiseowl.com.au.pix_art;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by anish.patel on 20/10/2016.
 */

public class DivideTiles {

    ConstructBitmapListener listener;
    public DivideTiles(Bitmap map, int size) {


        DivideAsync tiles = new DivideAsync();
        tiles.execute(new DivideParams(map,size));

//        divideInTiles(image, size, titleSize);
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

    public  class DivideAsync extends AsyncTask<DivideParams, Void, String[]> {
        int width = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(DivideParams... params) {
            Bitmap img = params[0].map;
            int tileSize = params[0].size;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 70, stream);

             width = img.getWidth() / tileSize;
            int height = img.getHeight() / tileSize;

            String[] array = new String[width * height]; //The array of tiles
        /* An image can be represented by an array of int like this :

            imagine your image is 2x2 pixel.
            Each pixel has a color represented by an int

            the array representation of that image would be : {top left pixel, topright pixel, bottom left pixel, bottomright pixel}

            basically, each line of pixel in an array of int, and you add each line one by one at the end of the array.
           */
            try {

                int line = 0;
                int column = 0;

                for (int y = 0; y < img.getHeight(); y += tileSize) {
                    for (int x = 0; x < img.getWidth(); x += tileSize) {
                        if (x + tileSize < img.getWidth() && y + tileSize < img.getHeight()) {

                            Bitmap tile = Bitmap.createBitmap(img, x, y, tileSize, tileSize); // This gives a tile of the original image
                            Bitmap newBitmap = Bitmap.createScaledBitmap(tile, 1, 1, true);
                            tile.recycle();

                            String color = String.format("%06X", 0xFFFFFF & newBitmap.getPixel(0, 0));// This gives you the average color and converts to HEX

                                                                  // Free memory

                            int currentIndex = (line * width) + column;
                            if (currentIndex < width * height)

                                array[currentIndex] = color;// Add color to current index in array

                            newBitmap.recycle();

                            column++;
                        }
                    }
                    line++;
                    column = 0;
                }
                return array;
            } catch (Exception e) {
            e.printStackTrace();
        }

            return null;
        }



        @Override
        protected void onPostExecute(String[] ints) {
            super.onPostExecute(ints);
            Bitmap[] array = new Bitmap[ints.length];
            String url = "http://10.0.2.2:8765/color/32/32/";
            for(int i = 0; i < ints.length; i ++){
              LoadImageTileFromServer getTileFromServer = new LoadImageTileFromServer();
                try {
                    array[i] = getTileFromServer.execute(url+ints[i]).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
           if(listener != null) {
               listener.constructBitmapListener(array, width);
           }
        }
    }

    public interface ConstructBitmapListener {
        void constructBitmapListener(Bitmap[] array, int width);
    }

    public void ConstructBitmapListener(ConstructBitmapListener listener){
        this.listener = listener;

    }


    public static class LoadImageTileFromServer extends AsyncTask<String, Void, Bitmap> {

        //        private final WeakReference<ImageView> imageViewReference;
//ImageView imageView
        public LoadImageTileFromServer() {
//            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return downloadBitmap(params[0]);
            } catch (Exception e) {
                // log error
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

//
        }

        private Bitmap downloadBitmap(String myUrl) {
            HttpURLConnection conn = null;
            InputStream is = null;
            try {
                URL url = new URL(myUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
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
