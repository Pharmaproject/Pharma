package in.optho.opthoremedies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


public class Bitmapview {

    public  void Bitmapview(ImageView view, byte[] data, Context context) {

        RequestOptions ro = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_backspace)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .dontAnimate()
                .fitCenter();

        Glide.with(context)
                .load(data)
                .apply(ro)

                .into(view);




     /*   BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
  //      options.inSampleSize = 20;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeByteArray(data,0,data.length,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 500, 500);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        view.setImageBitmap(bitmap);
        options.inJustDecodeBounds = false;
*/
        }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }

}