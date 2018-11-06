package com.lawyerhub.Utiles;

import android.widget.ImageView;

import com.lawyerhub.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Class is used to download images as a service in backgroud and also caches the images
 * Class uses Bitmap Transform class to compress images while downloading
 */
public class DownloadImageWithPicassa {
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    private final ImageView mImageViewContext;
    private static String mUrl;
    private final PicassoSuccessfulCallInterface mListener; // listener once image is downloaded / failed
    private final int mSize = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));


    // constructor to be invoked to initialise values
    public DownloadImageWithPicassa(ImageView imageViewContext, String url, PicassoSuccessfulCallInterface listener) {
        mImageViewContext = imageViewContext;
        mUrl = url;
        mListener = listener;
    }

    public DownloadImageWithPicassa(ImageView imageViewContext){
        mImageViewContext = imageViewContext;
        mListener = null;
    }

    /**
     * Method to download image
     */
    public void downloadImagesWithPicasso() {
        try {
            Utility.logData("========== URL TO BE DOWNLOADED BY PICASSO ============" + mUrl);
            //todo : remove indicator enabled

            // get image cached url if cached already
            Picasso picasso = Picasso.get();
            if(mUrl != null && !mUrl.equalsIgnoreCase(AppStringConstants.BLANK_STRING_CONST)) {
                picasso.load(mUrl).resize(mSize, mSize).error(R.drawable.judge).placeholder(R.drawable.judge).transform(new BitmapTransform(mSize, mSize)).centerInside()
                        .into(mImageViewContext, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (mListener != null)
                                    mListener.onPicassonCallDone();
                            }

                            @Override
                            public void onError(Exception e) {
                                Utility.logData("-------------- Picasso error from url--------------" + "\n");
                                Utility.logExceptionData(e);
                                if (mListener != null)
                                    mListener.onPicassonCallDone();
                            }

                        });
            }else {
                Picasso.get().load(R.drawable.judge).transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                        .resize(mSize, mSize).centerInside()
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(mImageViewContext , new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (mListener != null)
                                    mListener.onPicassonCallDone();
                            }

                            @Override
                            public void onError(Exception e) {
                                Utility.logData("-------------- Picasso error from url--------------" + "\n");
                                Utility.logExceptionData(e);
                                if (mListener != null)
                                    mListener.onPicassonCallDone();
                            }

                        });
            }
        } catch (Exception e) {
            Utility.logExceptionData(e);
        }
    }

    /**
     * Method is used to set an image in imageview
     * @param file
     */
    public void setImageInImageViewUsingPicasso(final File file){

        Picasso picasso =  Picasso.get();
        picasso.load(file)
                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .resize(mSize, mSize)
                .centerInside()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(mImageViewContext, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Utility.logData("-------------- Picasso error  in placing to image view -------------- " + "\n");
                Utility.logExceptionData(e);
            }

        });


    }
    /**
     * Interface to notify the listener that an image has been doenloaded or failed to download
     */
    public interface PicassoSuccessfulCallInterface {
        void onPicassonCallDone();
    }
}
