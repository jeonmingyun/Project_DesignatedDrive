package com.mx.designateddrive.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahrong on 2018-01-04.
 */

public class PhotoUtil {

    private final int CAMERA_REQUEST_CODE = 8;
    private final int GALLERY_REQUEST_CODE = 9;

    private String mImagePath;
    private Uri photoUri;
    private String currentPhotoPath;//실제 사진 파일 경로
    private String mImageCaptureName;//이미지 이름

    private Activity mActivity;

    public PhotoUtil(Activity activity) {
        this.mActivity = activity;
    }

    /** 촬영해서 사진 가져오기 */
    public String selectPhoto() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    currentPhotoPath = photoFile.getAbsolutePath();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    mActivity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }

        }
        return currentPhotoPath;
    }


    // 사진촬영 경로 생성
    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/path/"

                + mImageCaptureName);

        return storageDir;
    }


    /** 갤러리 사진 가져오기 */
    public void selectGalleyPhoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        mActivity.startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    // 1. 사진의 절대경로와 사진의 회전값을 찾는다.
    // 2. 찾은 사진을 이미지뷰에 출력한다.
    public String sendPicture(Intent data) {
        Uri imageUri = data.getData();
        String imagePath = getRealPathFromURI(imageUri);
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        mImagePath = imagePath;
        int mImageOreantation = exifDegree;
        Log.d("imagePath", mImagePath);

        return mImagePath;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
