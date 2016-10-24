package wiseowl.com.au.pix_art;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements DivideTiles.ConstructBitmapListener {
    public int TILE_SIZE = 32;
    Bitmap image;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iv = (ImageView) findViewById(R.id.imageView);

        Drawable myDrawable = ContextCompat.getDrawable(this, R.drawable.one);
        image = ((BitmapDrawable) myDrawable).getBitmap();
        DivideTiles divide = new DivideTiles(image, TILE_SIZE);
        divide.ConstructBitmapListener(this);
        iv.setImageBitmap(image);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bitmap tiledImage = new DivideTiles().divideInTiles(image,TILE_SIZE); //divideInTiles(image, TILE_SIZE);    // This generates a small sized tiled image

//                if(tiledImage !=null) {
//                    Bitmap resized = generateScaledBitmap(tiledImage, image.getWidth(), image.getHeight()); //This resizes the tiled image to the match first image size
//                    iv.setImageBitmap(resized);
//                }

            }
        });

    }


    @Override
    public void constructBitmapListener(Bitmap[] array, int width) {
        Log.i("ash", "in listner");
        Bitmap bmp = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

        for (int i = 0; i < array.length; i++) {
            int y = i / width;
            int x = i - (y * width);
            canvas.drawBitmap(array[i], x * TILE_SIZE, y * TILE_SIZE, paint);
        }

        iv.setImageBitmap(bmp);
        Log.i("ash", "done");
    }


}
