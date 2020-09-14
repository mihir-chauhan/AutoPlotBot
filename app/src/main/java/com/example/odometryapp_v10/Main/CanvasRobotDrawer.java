package com.example.odometryapp_v10.Main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.widget.ImageView;
import com.example.odometryapp_v10.R;
import java.util.ArrayList;

public class CanvasRobotDrawer {
    private Context context;
    private View view;
    private Canvas canvas;
    private Paint paint;
    private Activity activity;

    public CanvasRobotDrawer(Context context, View view, Activity activity) {
        this.context = context;
        this.view = view;
        this.activity = activity;
    }


    public void drawPointAt(ArrayList<Coordinate> coordinates) {
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.field_image3, myOptions);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(255, 153, 0));
        paint.setStrokeWidth(2);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        final Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(mutableBitmap);

        for (int i = 0; i < coordinates.size(); i++) {
            double x = coordinates.get(i).x * 584 / 144;
            double y = coordinates.get(i).y * 584 / 144;
            y = 584 - y;
            canvas.drawCircle((float) x, (float) y, 7, paint);

            if (coordinates.size() > 1 && i >= 1) {
                double previousX = coordinates.get(i - 1).x * 584 / 144;
                double previousY = coordinates.get(i - 1).y * 584 / 144;
                previousY = 584 - previousY;
                canvas.drawLine((float) previousX, (float) previousY, (float) x, (float) y, paint);
            }
        }
        canvas.drawPath(new Path(), paint);

        final ImageView imageView = view.findViewById(R.id.fieldView);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);
    }
}
