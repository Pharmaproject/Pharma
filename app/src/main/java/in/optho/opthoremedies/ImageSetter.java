package in.optho.opthoremedies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import static android.R.attr.bitmap;

@GlideModule
public class ImageSetter extends AppGlideModule {


    public void SetImage(final ImageView view, byte[] data, final Context context) {


        Glide.with(context.getApplicationContext())
                .load(data)
                .apply(new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .encodeFormat(Bitmap.CompressFormat.PNG))
                .into(view);
        view.setElevation(5);


 /*       Glide.with(context.getApplicationContext())
                .load(data)
                .apply(new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .encodeFormat(Bitmap.CompressFormat.PNG))
               .into(new BaseTarget<Drawable>() {
                   @Override
                   public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                       Bitmap bi = ((BitmapDrawable) resource).getBitmap();
                       final Bitmap shadows = imgshadow(bi, bi.getHeight(), bi.getWidth(), Color.BLACK, 3, 1, 3);
                       view.setImageBitmap(shadows);
                   }

                   @Override
                   public void getSize(SizeReadyCallback cb) {
                       cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL);

                   }

                   @Override
                   public void removeCallback(SizeReadyCallback cb) {

                   }
               });
*/






        //          .into(view);



    }

    private Bitmap imgshadow(final Bitmap bm, final int dstHeight, final int dstWidth, int color, int size, float dx, float dy) {
        final Bitmap mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8);

        final Matrix scaleToFit = new Matrix();
        final RectF src = new RectF(0, 0, bm.getWidth(), bm.getHeight());
        final RectF dst = new RectF(0, 0, dstWidth - dx, dstHeight - dy);
        scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        final Matrix dropShadow = new Matrix(scaleToFit);
        dropShadow.postTranslate(dx, dy);

        final Canvas maskCanvas = new Canvas(mask);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskCanvas.drawBitmap(bm, scaleToFit, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        maskCanvas.drawBitmap(bm, dropShadow, paint);

        final BlurMaskFilter filter = new BlurMaskFilter(size, BlurMaskFilter.Blur.NORMAL);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setMaskFilter(filter);
        paint.setFilterBitmap(true);

        final Bitmap ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
        final Canvas retCanvas = new Canvas(ret);
        retCanvas.drawBitmap(mask, 0,  0, paint);
        retCanvas.drawBitmap(bm, scaleToFit, null);
        mask.recycle();
        return ret;
    }

}