//TODO: 2016/7/6 付款码界面
package com.lzctbyong.www.meituan;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class FkmActivity extends AppCompatActivity {
    private final String content = "181751020521670578";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fkm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView imgtext = (TextView) findViewById(R.id.content_fkm_imgtext);
        ImageView img = (ImageView) findViewById(R.id.content_fkm_img1);
        ImageView img2 = (ImageView) findViewById(R.id.content_fkm_img2);
        toolbar.setTitle("付款码");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bitmap mQRCode = EncodingUtils.createQRCode(content, 500, 500, null);
        img2.setImageBitmap(mQRCode);

        try {
            Bitmap mBitmap = EncodingUtils.CreateOneDCode(content, 600, 100, false, this);
            img.setImageBitmap(mBitmap);
            String text;
            for (int i = 0; i < content.length() / 4; ++i) {

            }
            imgtext.setText(content);
        } catch (WriterException mE) {
            mE.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fkm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
