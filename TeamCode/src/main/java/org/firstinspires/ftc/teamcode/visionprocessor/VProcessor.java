package org.firstinspires.ftc.teamcode.visionprocessors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.visionprocessor.VisionProcessorBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class VProcessor extends VisionProcessorBase {
    //X + width must be less than 640, the current max width of the camera
    public Rect rectLeft = new Rect(0,300,50, 60);// y: 250
    public Rect rectMiddle = new Rect(282, 280, 50, 60);//y: 230
    public Rect rectRight = new Rect(590, 300, 50, 60);//y:245, x:560
    Selected selection = Selected.NONE;

    Mat submat = new Mat();
    Mat matYCrCb = new Mat();
    Mat matChannel = new Mat();

    private int channel = 2; // default to the red alliance

    @Override
    public void init(int width, int height, CameraCalibration calibration) {}

    @Override
    public Object processFrame(Mat matInput, long captureTimeNanos) {
//        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);
        Imgproc.cvtColor(matInput, matYCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(matYCrCb, matChannel, channel);

        double satRectLeft = getAvgSaturation(matChannel, rectLeft);
        double satRectMiddle = getAvgSaturation(matChannel, rectMiddle);
        double satRectRight = getAvgSaturation(matChannel, rectRight);

        if ((satRectLeft < satRectMiddle) && (satRectLeft < satRectRight)) {
            return Selected.LEFT;
        } else if ((satRectMiddle < satRectLeft) && (satRectMiddle < satRectRight)) {
            return Selected.MIDDLE;
        }
        return Selected.RIGHT;
    }

    protected double getAvgSaturation(Mat input, Rect rect) {
//        submat = input.submat(rect);
        Scalar color = Core.mean(input.submat(rect));
        return color.val[0];
    }

    private android.graphics.Rect makeGraphicsRect(Rect rect, float scaleBmpPxToCanvasPx) {
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);

        return new android.graphics.Rect(left, top, right, bottom);
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight,
                            float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.RED);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);

        Paint nonSelectedPaint = new Paint(selectedPaint);
        nonSelectedPaint.setColor(Color.GREEN);

        android.graphics.Rect drawRectangleLeft = makeGraphicsRect(rectLeft, scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleMiddle = makeGraphicsRect(rectMiddle, scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleRight = makeGraphicsRect(rectRight, scaleBmpPxToCanvasPx);

        selection = (Selected) userContext;
        switch (selection) {
            case LEFT:
                canvas.drawRect(drawRectangleLeft, selectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;
            case MIDDLE:
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, selectedPaint);
                canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;
            case RIGHT:
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                canvas.drawRect(drawRectangleRight, selectedPaint);
                break;
            case NONE:
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;
        }
    }

    @Override
    public int getSelected() {
        return selection.ordinal();
    }

    public enum Selected {
        NONE,
        LEFT,
        MIDDLE,
        RIGHT
    }

    @Override
    public void setRedAlliance (){ channel=2;}
    @Override
    public void setBlueAlliance (){ channel=1;}

}