package com.neu.calander.Util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.neu.calander.Pojo.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Get_image {
    public Get_image() {
    }

    public static Bitmap get_image(String imagePat) {
        return null;
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    public static Bitmap saveimage(Context context, Uri uri) throws Exception {
        String path = getRealPathFromUri(context, uri);
        Bitmap bitmap = ImageTobit.readStream(path);
        bitmap = compressImage(bitmap);
        String outline = ImageTobit.bitmapToString(bitmap) + ":" + User.getSingle_instance().getId()+":image";
        new Thread(){
            @Override
            public void run() {
                URL url = null;
                HttpURLConnection connection = null;
                String result = null;
                try {
                    url = new URL("https://lkcmacauweb.top/Server/android/update_user");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setDoOutput(true);
                    connection.connect();
                    OutputStream out = connection.getOutputStream();
                    out.write(outline.getBytes(),0,outline.getBytes().length);

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String s;
                    while ((s=bufferedReader.readLine())!=null) {
                        result += s;
                    }
                    out.flush();
                    out.close();
                    bufferedReader.close();
                    connection.disconnect();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    connection.disconnect();
                }
            }
        }.start();
        User.getSingle_instance().setIcon(ImageTobit.bitmapToString(bitmap));
        return bitmap;
    }
    static public Bitmap compressImage(Bitmap beforeBitmap) {
        Bitmap newBitmap = beforeBitmap;
        int width = beforeBitmap.getWidth();//读取旧图的宽度
        int height = beforeBitmap.getHeight();//读取旧图的高度
        float scaleWidth;//缩放宽度
        float scaleHeight;//缩放高度
        int number = 100;//缩放比例,原图的百分比
        Matrix matrix = new Matrix();
        int newWidth = width;//新图的宽度，初始取原图
        int newHeight = height;//新图的高度，初始取原图
        while (newWidth > 1080 || newHeight > 1920) {
            scaleWidth = (float) ((number * width * 0.01) / width);//按百分比进行缩放
            scaleHeight = (float) ((number * height * 0.01) / height);//同上
            matrix.postScale(scaleWidth, scaleHeight);
            newBitmap = Bitmap.createBitmap(beforeBitmap, 0, 0, width, height, matrix, true);//根据缩放比例获取新图
            newWidth = newBitmap.getWidth();//获取新图的宽度
            newHeight = newBitmap.getHeight();//获取新图的高度
            number -= 10;//缩放比例减小
        }
        Log.i("bitMap", "图片比例：" + newHeight + "x" + newWidth);
        return newBitmap;

    }
    public static Bitmap select_image(Context context, String path) throws Exception {
        Bitmap bitmap = ImageTobit.readStream(path);
        bitmap = compressImage(bitmap);
        return bitmap;
    }
}
