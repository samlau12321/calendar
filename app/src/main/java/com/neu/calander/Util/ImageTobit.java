package com.neu.calander.Util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImageTobit {


    public static boolean verifyStoragePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_PERMISSION_STORAGE = 100;
            String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            for (String str : permissions) {
                if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(permissions, REQUEST_CODE_PERMISSION_STORAGE);
                    return true;
                }
            }
        }
        return false;
    }

    private static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    private static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    public static Bitmap StringToBitmap(String s) throws IOException {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(s, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    public static Bitmap readStream(String imagepath) throws Exception {
        Bitmap rawBitmap1 = BitmapFactory.decodeFile(imagepath, null);
        return rawBitmap1;
    }

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    static private Bitmap compressImage(Bitmap beforeBitmap) {

        // 可以捕获内存缓冲区的数据，转换成字节数组。
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (beforeBitmap != null) {
            // 第一个参数：图片压缩的格式；第二个参数：压缩的比率；第三个参数：压缩的数据存放到bos中
            beforeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            // 循环判断压缩后的图片大小是否满足要求，这里限制100kb，若不满足则继续压缩，每次递减10%压缩
            int options = 100;
            while (bos.toByteArray().length / 1024 > 2048) {
                bos.reset();// 置为空
                beforeBitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
                options -= 10;
            }

            // 从bos中将数据读出来 转换成图片
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            Bitmap afterBitmap = BitmapFactory.decodeStream(bis);
            return afterBitmap;
        }
        return null;
    }

}
