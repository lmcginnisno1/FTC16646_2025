package org.firstinspires.ftc.teamcode.visionprocessor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Vision Processor to detect colored blocks (Red, Blue, Yellow) and determine their offset from the robot.
 */
public class VProcessorDetectBlock extends VisionProcessorBase {

    /**
     * Enum to represent target colors.
     */
    public enum TargetColor {
        RED,
        BLUE,
        YELLOW
    }

    private TargetColor targetColor = TargetColor.RED; // Default target color

    // Camera image dimensions
    private int imageWidth = 640;
    private int imageHeight = 480;

    // To store detected bounding rectangle and offset
    private Rect detectedRect = null;
    private Point offset = new Point(0, 0);

    // Define the target aspect ratio and allowable tolerance
    private double targetAspectRatio = 1.5 / 3.5; // Width to Height ratio (1.5:3.5)
    private double aspectRatioTolerance = 0.1; // Allowable deviation (10%)

    // Parameters for camera position relative to robot center (in inches or cm)
    private double cameraOffsetX = 2.0; // Camera is 2 inches to the right of the robot center
    private double cameraOffsetY = 5.0; // Camera is 5 inches in front of the robot center
    private double cameraAngle = 0.0; // Camera is perfectly aligned forward (0 degrees)

    // Define HSV color thresholds for each target color
    // These ranges may need to be adjusted based on your camera and lighting conditions
    private Scalar lowerRed1 = new Scalar(0, 100, 100);
    private Scalar upperRed1 = new Scalar(10, 255, 255);
    private Scalar lowerRed2 = new Scalar(160, 100, 100);
    private Scalar upperRed2 = new Scalar(179, 255, 255);

    private Scalar lowerBlue = new Scalar(100, 150, 0);
    private Scalar upperBlue = new Scalar(140, 255, 255);

    private Scalar lowerYellow = new Scalar(20, 100, 100);
    private Scalar upperYellow = new Scalar(30, 255, 255);

    private volatile DetectionResult lastDetectionResult = null;

    /**
     * Initialization method where camera parameters can be set.
     */
    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        this.imageWidth = width;
        this.imageHeight = height;
    }

    /**
     * Processes each frame to detect the target colored block and compute its offset.
     *
     * @param matInput         The input frame from the camera.
     * @param captureTimeNanos Timestamp of the frame capture.
     * @return DetectionResult containing the bounding rectangle and offset.
     */
    @Override
    public Object processFrame(Mat matInput, long captureTimeNanos) {
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(matInput, hsvMat, Imgproc.COLOR_RGB2HSV); // Convert to HSV color space

        Mat mask = new Mat();

        // Create mask based on the target color
        switch (targetColor) {
            case RED:
                Mat mask1 = new Mat();
                Mat mask2 = new Mat();
                Core.inRange(hsvMat, lowerRed1, upperRed1, mask1); // Lower red range
                Core.inRange(hsvMat, lowerRed2, upperRed2, mask2); // Upper red range
                Core.add(mask1, mask2, mask); // Combine masks
                mask1.release();
                mask2.release();
                break;
            case BLUE:
                Core.inRange(hsvMat, lowerBlue, upperBlue, mask);
                break;
            case YELLOW:
                Core.inRange(hsvMat, lowerYellow, upperYellow, mask);
                break;
        }

        // Apply morphological operations to reduce noise
        Imgproc.erode(mask, mask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(5, 5)));
        Imgproc.dilate(mask, mask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(5, 5)));

        // Find contours in the mask
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        detectedRect = null;
        offset = new Point(0, 0);

        if (!contours.isEmpty()) {
            // Identify the largest contour that meets the aspect ratio criteria
            double maxArea = 0;
            MatOfPoint largestValidContour = null;
            for (MatOfPoint contour : contours) {
                double area = Imgproc.contourArea(contour);
                if (area > maxArea) {
                    Rect boundingRect = Imgproc.boundingRect(contour);

                    // Calculate aspect ratio of the bounding rectangle
                    double aspectRatio = boundingRect.width < boundingRect.height ? (double) boundingRect.width / boundingRect.height : (double) boundingRect.height / boundingRect.width;

                    // Check if the aspect ratio is within the acceptable range
                    if (Math.abs(aspectRatio - targetAspectRatio) <= aspectRatioTolerance) {
                        maxArea = area;
                        largestValidContour = contour;
                    }
                }
            }

            // If a valid contour is found, compute its bounding rectangle and offset
            if (largestValidContour != null && maxArea > 500) { // Adjust the area threshold as needed
                detectedRect = Imgproc.boundingRect(largestValidContour);

                // Compute center of detected rectangle
                double rectCenterX = detectedRect.x + detectedRect.width / 2.0;
                double rectCenterY = detectedRect.y + detectedRect.height / 2.0;

                double imageCenterX = imageWidth / 2.0;
                double imageCenterY = imageHeight / 2.0;

                double offsetX = rectCenterX - imageCenterX;
                double offsetY = rectCenterY - imageCenterY;

                // Transform the camera offset into robot-relative coordinates
                Point cameraFrameOffset = new Point(offsetX, offsetY);
                Point robotRelativeOffset = adjustForCameraPosition(cameraFrameOffset);

                offset = robotRelativeOffset; // Use the adjusted offset

                lastDetectionResult = new DetectionResult();
                lastDetectionResult.boundingRect = detectedRect;
                lastDetectionResult.offset = offset;
            }
            else{
                lastDetectionResult = null;
            }
        }

        // Release temporary Mats to free memory
        hsvMat.release();
        mask.release();
        for (MatOfPoint contour : contours) {
            contour.release();
        }

        return lastDetectionResult;
    }

    /**
     * Converts OpenCV Rect to Android graphics Rect for drawing.
     *
     * @param rect                    The OpenCV rectangle.
     * @param scaleBmpPxToCanvasPx Scaling factor from bitmap pixels to canvas pixels.
     * @return Android graphics Rect.
     */
    private android.graphics.Rect makeGraphicsRect(Rect rect, float scaleBmpPxToCanvasPx) {
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);

        return new android.graphics.Rect(left, top, right, bottom);
    }

    /**
     * Converts OpenCV Rect to Android graphics Rect for drawing.
     *
     * @param cameraOffset  The current camera frame offset to adjust based on where the camera is mounted.
     * @return Point representing the adjusted offset.
     */
    public Point adjustForCameraPosition(Point cameraOffset) {
        // Convert camera angle to radians for calculations
        double angleRad = Math.toRadians(cameraAngle);
    
        // Rotate the camera offset by the camera's angle
        double adjustedOffsetX = cameraOffset.x * Math.cos(angleRad) - cameraOffset.y * Math.sin(angleRad);
        double adjustedOffsetY = cameraOffset.x * Math.sin(angleRad) + cameraOffset.y * Math.cos(angleRad);
    
        // Add camera's physical position offsets
        adjustedOffsetX += cameraOffsetX;
        adjustedOffsetY += cameraOffsetY;
    
        return new Point(adjustedOffsetX, adjustedOffsetY);
    }

    /**
     * Draws the detection results on the canvas for visual feedback.
     *
     * @param canvas                 The canvas to draw on.
     * @param onscreenWidth          Width of the on-screen display.
     * @param onscreenHeight         Height of the on-screen display.
     * @param scaleBmpPxToCanvasPx   Scaling factor from bitmap pixels to canvas pixels.
     * @param scaleCanvasDensity     Scaling factor for canvas density.
     * @param userContext            The detection result containing bounding rectangle and offset.
     */
    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight,
                            float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

        Paint detectedPaint = new Paint();
        detectedPaint.setColor(Color.RED); // You can change this based on target color if needed
        detectedPaint.setStyle(Paint.Style.STROKE);
        detectedPaint.setStrokeWidth(scaleCanvasDensity * 4);

        Paint centerPaint = new Paint();
        centerPaint.setColor(Color.BLUE);
        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setStrokeWidth(scaleCanvasDensity * 4);

        DetectionResult result = (DetectionResult) userContext;
        if (result.boundingRect != null) {
            android.graphics.Rect drawRect = makeGraphicsRect(result.boundingRect, scaleBmpPxToCanvasPx);
            canvas.drawRect(drawRect, detectedPaint);

            // Draw a circle at the center of the detected block
            float centerX = drawRect.left + drawRect.width() / 2.0f;
            float centerY = drawRect.top + drawRect.height() / 2.0f;
            canvas.drawCircle(centerX, centerY, 10, centerPaint);
        }

        // Draw the center of the image
        float imageCenterX = onscreenWidth / 2.0f;
        float imageCenterY = onscreenHeight / 2.0f;
        Paint imageCenterPaint = new Paint();
        imageCenterPaint.setColor(Color.GREEN);
        imageCenterPaint.setStyle(Paint.Style.FILL);
        imageCenterPaint.setStrokeWidth(scaleCanvasDensity * 4);
        canvas.drawCircle(imageCenterX, imageCenterY, 10, imageCenterPaint);

        // Draw a line indicating the offset from image center to block center
        if (result.boundingRect != null) {
            float blockCenterX = imageCenterX + (float) result.offset.x * scaleBmpPxToCanvasPx;
            float blockCenterY = imageCenterY + (float) result.offset.y * scaleBmpPxToCanvasPx;
            Paint linePaint = new Paint();
            linePaint.setColor(Color.YELLOW);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(scaleCanvasDensity * 2);
            canvas.drawLine(imageCenterX, imageCenterY, blockCenterX, blockCenterY, linePaint);
        }
    }

    /**
     * Retrieves the detection result.
     *
     * @return The ordinal value of the selected state (not used in this implementation).
     */
    @Override
    public int getSelected() {
        // Not applicable in the new implementation
        return 0;
    }

    /**
     * Enum retained for compatibility (not used in the new implementation).
     */
    public enum Selected {
        NONE,
        LEFT,
        MIDDLE,
        RIGHT
    }

    /**
     * Sets the target color to Red.
     */
    @Override
    public void setRedAlliance() {
        targetColor = TargetColor.RED;
    }

    /**
     * Sets the target color to Blue.
     */
    @Override
    public void setBlueAlliance() {
        targetColor = TargetColor.BLUE;
    }

    /**
     * Sets the target color to Yellow.
     */
    public void setYellowAlliance() {
        targetColor = TargetColor.YELLOW;
    }

    /**
     * Class to hold the detection results.
     */
    public static class DetectionResult {
        public Rect boundingRect;
        public Point offset;
    }

    public DetectionResult getLastDetectionResult() {
        return lastDetectionResult;
    }
}