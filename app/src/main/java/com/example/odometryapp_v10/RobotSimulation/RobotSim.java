package com.example.odometryapp_v10.RobotSimulation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.widget.ImageView;
import com.example.odometryapp_v10.Main.Coordinate;
import com.example.odometryapp_v10.R;
import com.example.odometryapp_v10.RobotSimulation.Skystone.MecanumDrivetrain;
import com.example.odometryapp_v10.RobotSimulation.Structure.Odometry;
import com.example.odometryapp_v10.RobotSimulation.Structure.Pose;
import java.util.ArrayList;


public class RobotSim {
    private static Context context;
    private static View view;
    private static Activity activity;
    private static ArrayList<Coordinate> allCoordinates;
    private static Paint paint;

    public RobotSim(Context context, View view, Pose startingPosition, Activity activity, ArrayList<Coordinate> allCoordinates) {
        this.context = context;
        this.view = view;
        this.activity = activity;
        odometry = new Odometry(startingPosition);
        this.startingPosition = startingPosition;
        this.allCoordinates = allCoordinates;
    }

    private static Odometry odometry;
    public static Pose startingPosition;

    static Canvas canvas = new Canvas();

    public static void setPosition(Pose position, float heading) {
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.field_image2, myOptions);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(255, 153, 0));
        paint.setStrokeWidth(2);

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        final Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(mutableBitmap);

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotimage, myOptions);
        Bitmap wrkBmp = Bitmap.createBitmap(bmp);
        final Bitmap mtbleBmp = wrkBmp.copy(Bitmap.Config.ARGB_8888, true);

        for (int i = 0; i < allCoordinates.size(); i++) {
            double x = allCoordinates.get(i).x * 584 / 144;
            double y = allCoordinates.get(i).y * 584 / 144;
            y = 584 - y;
            canvas.drawCircle((float) x, (float) y, 7, paint);

            if (allCoordinates.size() > 1 && i >= 1) {
                double previousX = allCoordinates.get(i - 1).x * 584 / 144;
                double previousY = allCoordinates.get(i - 1).y * 584 / 144;
                previousY = 584 - previousY;
                canvas.drawLine((float) previousX, (float) previousY, (float) x, (float) y, paint);
            }
        }
        canvas.drawPath(new Path(), paint);

        double x = position.x * 584 / 144;
        double y = position.y * 584 / 144;
        y = (584 - 40) - y;
        x = x - 40;
        Matrix rotator = new Matrix();

        float rad = -heading + (float) (Math.PI / 2);
        rotator.postRotate((float) Math.toDegrees(rad), mtbleBmp.getWidth() / 2, mtbleBmp.getHeight() / 2);
        rotator.postTranslate((float) x, (float) y);

        canvas.drawBitmap(Bitmap.createBitmap(mtbleBmp), rotator, null);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ImageView imageView = view.findViewById(R.id.fieldView);
                imageView.setAdjustViewBounds(true);
                imageView.setImageBitmap(mutableBitmap);
            }
        });
    }

    public void startMovement(ArrayList<MovementPose> movementCoordinates) {
        BackCalculation.timeStampLeft = BackCalculation.timeStampCenter = BackCalculation.timeStampRight = System.currentTimeMillis();
        BackCalculation.setBackLeftPower(0.0);
        BackCalculation.setFrontLeftPower(0.0);
        BackCalculation.setBackRightPower(0.0);
        BackCalculation.setFrontRightPower(0.0);
        odometry.startBackgroundPositionUpdates();

        MecanumDrivetrain.startBackgroundPositionUpdates(movementCoordinates, 0.2, startingPosition);
    }
}
