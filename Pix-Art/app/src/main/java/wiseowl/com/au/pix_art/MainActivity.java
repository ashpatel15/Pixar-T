package wiseowl.com.au.pix_art;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.plattysoft.leonids.ParticleSystem;


public class MainActivity extends AppCompatActivity implements DivideTiles.ConstructBitmapListener {
    private static int RESULT_LOAD_IMAGE = 1;
    public int TILE_SIZE = 32;
    private Bitmap image;
    private ImageView iv;
    private ParticleSystem particleSystem;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.imageView);


        Drawable myDrawable = ContextCompat.getDrawable(this, R.drawable.one);
        image = ((BitmapDrawable) myDrawable).getBitmap();
//        updateProgress();

//        DivideTiles divide = new DivideTiles(this, image, TILE_SIZE);
//        divide.ConstructBitmapListener(this);

        iv.setImageBitmap(image);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        btn = (Button) findViewById(R.id.btnRender);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRain();

                DivideTiles divide = new DivideTiles( image, TILE_SIZE);
                divide.ConstructBitmapListener(MainActivity.this);
            }
        });
    }


    @Override
    public void constructBitmapListener(Bitmap bitmap) {

        iv.setImageBitmap(bitmap);
        stopProgress();
    }

    //Start partical
    public void startRain() {
        particleSystem = new ParticleSystem(MainActivity.this, 80, R.drawable.drop, 10000);
        particleSystem.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f);
        particleSystem.setAcceleration(0.00005f, 90);
        particleSystem.emitWithGravity(findViewById(R.id.rain_emitter), Gravity.BOTTOM, 8);
    }

    //Finish partical
    public void stopProgress() {
        if (particleSystem != null) {
            particleSystem.stopEmitting();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pickImage) {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            image = BitmapFactory.decodeFile(picturePath);
            iv.setImageBitmap(image);

        }


    }
}
