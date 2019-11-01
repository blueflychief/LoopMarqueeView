package com.danikule.marqueeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MarqueeView mvMarquee;
    private Button btSwitch;
    private Button btChange;
    private TextView tvAnimView;
    private TextView tvSpannable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mvMarquee = (MarqueeView) findViewById(R.id.mvMarquee);
        btSwitch = (Button) findViewById(R.id.btSwitch);
        btChange = (Button) findViewById(R.id.btChange);
        tvAnimView = (TextView) findViewById(R.id.tvAnimView);
        tvSpannable = (TextView) findViewById(R.id.tvSpannable);
        btSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvMarquee.isPlaying()) {
                    mvMarquee.stop();
                } else {
                    mvMarquee.start();
                }
            }
        });
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvMarquee.setText("这是超长跑马灯啊啊啊啊啊啊");
//                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_anim);
//                tvAnimView.startAnimation(animation);
//
//                new Thread() {
//                    @Override
//                    public void run() {
//                        long start = System.currentTimeMillis();
//                        compress(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator, "115.gif");
//                        Log.i(TAG, "run耗时: " + (System.currentTimeMillis() - start));
//                    }
//                }.start();


            }
        });

        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        tvSpannable.setText(addSpannable(drawable, "不二小姐dsdz", "66669999999", drawable, "Love"));
    }

    private void compress(String path, String imgName) {
        File file = new File(path, imgName);
        Log.i("compress", "jpg start");
        byte[] bytes = compressBitmapToBytes(file.getPath(), 600, 600, 60, Bitmap.CompressFormat.JPEG);
        File jpg = new File(path, imgName + "compress.jpg");
        createFile(jpg, bytes);
        Log.i("compress", "jpg finish");
        Log.i("compress", "----------------------------------------------------");
        Log.i("compress", "webp start");
        byte[] bytes1 = compressBitmapToBytes(file.getPath(), 600, 600, 60, Bitmap.CompressFormat.WEBP);//分别是图片路径，宽度高度，质量，和图片类型，重点在这里。
        File webp = new File(path, imgName + "compress.webp");
        createFile(webp, bytes1);
        Log.i("compress", "webp finish");
    }

    //将byte数组写入文件
    public void createFile(File file, byte[] content) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(content);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] compressBitmapToBytes(String filePath, int reqWidth, int reqHeight,
                                               int quality, Bitmap.CompressFormat format) {
        Bitmap bitmap = getSmallBitmap(filePath, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);
        byte[] bytes = baos.toByteArray();
        bitmap.recycle();
        Log.i(TAG, "Bitmap compressed success, size: " + bytes.length);
        return bytes;
    }

    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (!new File(filePath).exists()) {
            Log.i(TAG, "文件不存在");
            return null;
        }
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
//      options.inPreferQualityOverSpeed = true;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int h = options.outHeight;
        int w = options.outWidth;
        int inSampleSize = 0;
        if (h > reqHeight || w > reqWidth) {
            float ratioW = (float) w / reqWidth;
            float ratioH = (float) h / reqHeight;
            inSampleSize = (int) Math.min(ratioH, ratioW);
        }
        inSampleSize = Math.max(1, inSampleSize);
        return inSampleSize;
    }


    private SpannableString addSpannable(Drawable avatarDrawable, String name, String gift, Drawable giftDrawable, String countString) {
        StringBuilder s = new StringBuilder(20);
        if (avatarDrawable != null) {  //头像
            s.append(" ");
        }
        if (!TextUtils.isEmpty(name)) { //不二小姐
            s.append(name);
        }

        if (!TextUtils.isEmpty(gift)) {  //送了666666
            s.append(" 送了 ").append(gift);
        }

        if (giftDrawable != null) { //礼物图片
            s.append(" ");
        }

        if (countString != null) {  //礼物数量
            s.append(countString);
        }

        SpannableString spanString = new SpannableString(s.toString());//第一个是空格，用于显示图片用
        int length = spanString.length();
        Log.i(TAG, "SpannableString length: " + length);
        //头像
        int strLength = 0;
        if (avatarDrawable != null) {
            strLength += 1;
            avatarDrawable.setBounds(0, 0, dip2px(this, 24), dip2px(this, 24));
            CenterImageSpan imageSpan = new CenterImageSpan(avatarDrawable);
            spanString.setSpan(imageSpan, 0, strLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (!TextUtils.isEmpty(name)) {
            //文字大小
            int end = strLength + name.length();
            AbsoluteSizeSpan nicknameSizeSpan = new AbsoluteSizeSpan(28);
            spanString.setSpan(nicknameSizeSpan, strLength, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            //昵称文字颜色
            ForegroundColorSpan nicknameColorSpan = new ForegroundColorSpan(Color.GREEN);
            spanString.setSpan(nicknameColorSpan, strLength, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            strLength = end;
        }

        if (!TextUtils.isEmpty(gift)) {
            int end = strLength + gift.length() + 4;
            //礼物文字大小
            AbsoluteSizeSpan giftSizeSpan = new AbsoluteSizeSpan(24);
            spanString.setSpan(giftSizeSpan, strLength, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            //礼物文字颜色
            ForegroundColorSpan giftColorSpan = new ForegroundColorSpan(Color.WHITE);
            spanString.setSpan(giftColorSpan, strLength, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            strLength = end;
        }

        if (giftDrawable != null) {
            giftDrawable.setBounds(0, 0, dip2px(this, 24), dip2px(this, 24));
            CenterImageSpan imageSpan = new CenterImageSpan(giftDrawable);
            spanString.setSpan(imageSpan, strLength, strLength + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            strLength += 1;
        }

        if (!TextUtils.isEmpty(countString)) {
            int end = strLength + countString.length();
            //礼物文字大小
            AbsoluteSizeSpan giftSizeSpan = new AbsoluteSizeSpan(24);
            spanString.setSpan(giftSizeSpan, strLength, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            //礼物文字颜色
            ForegroundColorSpan giftColorSpan = new ForegroundColorSpan(Color.YELLOW);
            spanString.setSpan(giftColorSpan, strLength, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            strLength = end;
        }

        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.RED);
        spanString.setSpan(backgroundColorSpan, 0, strLength - (TextUtils.isEmpty(countString) ? 0 : countString.length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    /**
     * dp convert to px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
