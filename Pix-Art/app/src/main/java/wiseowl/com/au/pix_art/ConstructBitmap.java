//package wiseowl.com.au.pix_art;import static android.R.attr.y;

///**
// * Created by anish.patel on 24/10/2016.
// */
//
//public class ConstructBitmap {
//
//    public ConstructBitmap(ConstructParams params) {
//
//    }
//
//    ArrayList<Bitmap> imageChunksStorageList ;
//    private void splitImage(ImageView image, int rows, int columns)
//    {
////For height and width of the small image chunks
//        int chunkHeight,chunkWidth;
//        Bitmap image; // use to store image chunks
//        int chunkNumbers=rows*column;
////To store all the small image chunks in bitmap format in this list
//        bitmapStorageList = new ArrayList<Bitmap>(chunkNumbers);
////Getting the BitmapDrawable from the imageview where your image is displayed
//        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
////setting the height and width of each pieces according to the rows and columns
//        chunkHeight = bitmap.getHeight()/rows;
//        chunkWidth = bitmap.getWidth()/columns;
////x and y are the pixel positions of the image chunks
//        int yCoord = 0;
//        for(int i=0; i<rows; i++)
//        {
//            int x = 0;
//            for(int j=0; j<columns; j++)
//            {
//                image=Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight);
//                imageChunksStorageList.add(image);
//                x=x+chunkWidth;
//            }
//            y=y+chunkHeight;
//        }
/* Once, you are done with this split task then you can show your arraylist into the gridview */
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


