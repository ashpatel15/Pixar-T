//package wiseowl.com.au.pix_art;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
///**
// * Created by anish.patel on 24/10/2016.
// */
//
//public class ConstructBitmap extends AsyncTask<ConstructParams, Void, Integer> {
//
////    public ConstructBitmap(ConstructParams params) {
////        BuildImage async = new BuildImage();
////        async.execute(params);
////    }
//
////    public ConstructBitmap(DivideTiles.ConstructParams test) {
////    }
//
////    public class ConstructParams {
////        Canvas canvas;
////        Paint paint;
////        String url;
////        int width;
////
////        ConstructParams(Canvas canvas, Paint paint, String url, int width) {
////            this.canvas = canvas;
////            this.paint = paint;
////            this.url = url;
////            this.width = width;
////
////        }
////    }
//
//
////    private class BuildImage extends AsyncTask<ConstructParams, Void, Bitmap> {
////
////    @Override
////    protected Bitmap doInBackground(String... params) {
////        return downloadBitmap(params[0]);
////    }
//
//        @Override
//        protected Integer doInBackground(ConstructParams... params) {
//            int y = params[0].pos / params[0].width;
//            int x = params[0].pos - (y * params[0].width);
//            params[0].canvas.drawBitmap(downloadBitmap(params[0].url), x,y,params[0].paint);
//
//            return params[0].pos;
//        }
//
//        @Override
//        protected void onPostExecute(Integer pos) {
//            super.onPostExecute(pos);
//
//        }
//
//        private Bitmap downloadBitmap(String myUrl) {
//            HttpURLConnection conn = null;
//            InputStream is = null;
//            try {
//                URL url = new URL(myUrl);
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                // Starts the query
//                conn.connect();
//                is = conn.getInputStream();
//                if (is != null) {
//                    Bitmap bitmap = BitmapFactory.decodeStream(is);
//                    return bitmap;
//                }
//            } catch (Exception e) {
//                conn.disconnect();
//                Log.w("ImageDownloader", "Error downloading image from " + myUrl);
//            } finally {
//                if (conn != null) {
//                    conn.disconnect();
//                }
//            }
//            return null;
//        }
////    public interface ConstructBitmapListener {
////        void constructBitmapListener(Bitmap img);
////    }
////
////    public void ConstructBitmapListener(DivideTiles.ConstructBitmapListener listener) {
////        this.listener = listener;
////
////    }
//
////    @Override
////    public void constructBitmapListener(Bitmap[] array) {
////
////    }
////    }
//}
