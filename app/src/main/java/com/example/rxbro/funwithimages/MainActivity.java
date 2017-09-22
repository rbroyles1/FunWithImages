package com.example.rxbro.funwithimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    Button btnCrop;
    Button btnResize;
    Button btnFunny;
    Button btnRotate;
    Button btnFit;
    Button btnPlaceHolder;
    Context context;
    private String imgUrl = "http://i.imgur.com/DvpvklR.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }
    private void initView() {
        imageView = (ImageView)findViewById(R.id.ivImage);
        // or here?
        Picasso.with(context).load(imgUrl).into(imageView);
        btnPlaceHolder = (Button)findViewById(R.id.btnPlaceHolder);
        btnCrop = (Button)findViewById(R.id.btnCrop);
        btnFit = (Button)findViewById(R.id.btnFit);
        btnResize = (Button)findViewById(R.id.btnResize);
        btnRotate = (Button)findViewById(R.id.btnRotate);
        btnFunny = (Button)findViewById(R.id.btnFunny);
        btnPlaceHolder.setOnClickListener(this);
        btnFunny.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        btnResize.setOnClickListener(this);
        btnFit.setOnClickListener(this);
        btnCrop.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlaceHolder:

            case R.id.btnCrop:
                Picasso.with(context).load(imgUrl).centerCrop().into(imageView);
                break;
            case R.id.btnResize:
                Picasso.with(context).load(imgUrl).resize(100, 100).into(imageView);
                break;
            case R.id.btnRotate:
                Picasso.with(context).load(imgUrl).rotate(-90f).into(imageView);
                break;
            case R.id.btnFit:
                Picasso.with(context).load(imgUrl).fit().centerInside().into(imageView);
                break;
            case R.id.btnFunny:
                Picasso.with(context).load(imgUrl).transform(new BlurTransformation(this)).into(imageView);
                break;
        }

    }
    // Helper class to blur image with the funny button
    // This class is from https://futurestud.io/tutorials/picasso-image-rotation-and-transformation
    // Thanks!
    public class BlurTransformation implements Transformation {
        RenderScript rs;

        public BlurTransformation(Context context) {
            super();
            rs = RenderScript.create(context);
        }
        @Override
        public Bitmap transform(Bitmap bitmap) {
            Bitmap blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);
            script.setRadius(10);
            script.forEach(output);
            output.copyTo(blurredBitmap);
            bitmap.recycle();
            return blurredBitmap;

        }
        @Override
        public String key() {
            return "blur";
        }
    }

}
