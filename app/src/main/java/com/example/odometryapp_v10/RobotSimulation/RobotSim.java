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
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.odometryapp_v10.Main.CanvasRobotDrawer;
import com.example.odometryapp_v10.Main.Coordinate;
import com.example.odometryapp_v10.R;
import com.example.odometryapp_v10.RobotSimulation.Skystone.MecanumDrivetrain;
import com.example.odometryapp_v10.RobotSimulation.Structure.Odometry;
import com.example.odometryapp_v10.RobotSimulation.Structure.Pose;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;


public class RobotSim {
    private static Context context;
    private static View view;
    private static Activity activity;

    public RobotSim(Context context, View view, Pose startingPosition, Activity activity) {
        this.context = context;
        this.view = view;
        this.activity = activity;
        odometry = new Odometry(startingPosition);
        this.startingPosition = startingPosition;
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

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        final Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(mutableBitmap);

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.robotimage, myOptions);
        Bitmap wrkBmp = Bitmap.createBitmap(bmp);
        final Bitmap mtbleBmp = wrkBmp.copy(Bitmap.Config.ARGB_8888, true);

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

        MecanumDrivetrain.startBackgroundPositionUpdates(movementCoordinates, 0.2);
    }
}
