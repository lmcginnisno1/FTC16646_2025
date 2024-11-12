package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.teamcode.ftclib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.ftclib.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.ftclib.geometry.Rotation2d;
import org.firstinspires.ftc.teamcode.visionprocessor.VisionProcessorBase;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;

public class SUB_Vision extends SubsystemBase {

    class FieldAprilTag {
        public int id;
        public double x;
        public double y;
        public double angle;
        FieldAprilTag(int p_id, double p_x, double p_y, double p_angle) {
            id = p_id;
            x = p_x;
            y = p_y;
            angle = p_angle;
        }
    }

    // cameraOffset is the distance in inches from the center of the robot to the camera's
    // location.  Angle is in degrees. A camera facing forward that is 8 inches in front and 6 inches
    // to the left of the robot center has offset (8, 6, 0).  A camera in the center of the robot
    // pointing directly to the left has an offset of (0, 0, 90 degrees).
    double m_cameraOffsetX;
    double m_cameraOffsetY;
    double m_cameraOffsetAngle;

    OpMode m_opMode;
    private String m_cameraName;

    long m_lastFrameID;

    KalmanFilter m_xPosKalman;
    KalmanFilter m_yPosKalman;
    KalmanFilter m_hPosKalman;
    double m_xPosFiltered;
    double m_yPosFiltered;
    double m_hPosFiltered;
    int m_countKalmanPoints;

    private VisionPortal m_visionPortal;               // Used to manage the video source.
    private AprilTagProcessor m_aprilTag;              // Used for managing the AprilTag detection process.
//    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag

    VisionProcessorBase m_autonomousDetectVProcessor;
    // Set id to the apriltag id. X, Y are in ZooBOTix field coordinates (72, 144 is blue non-audience
    // corner). angle is in degrees. A tag's angle is where an arrow shot through the tag's center from
    // within the field would point.  Currently can only be 0 (top), -90 (red side), -180 (audience
    // side), or 90 (blue side).
    double xFrontBackStage = 61.375; // measure to front flat surface
    double xCordinate = xFrontBackStage + 1.5;
    private final FieldAprilTag[] m_aprilTagLocations = {
            new FieldAprilTag(1,xCordinate, 41.75, 0),//left tag blue side
            new FieldAprilTag(2,xCordinate, 35.75, 0),//middle tag blue side
            new FieldAprilTag(3,xCordinate, 29.50, 0),//right tag blue side
            new FieldAprilTag(4,xCordinate, -29.50, 0),///left tag red side
            new FieldAprilTag(5,xCordinate, -35.75, 0),//middle tag red side
            new FieldAprilTag(6,xCordinate, -41.75, 0),//right tag red side
//			new FieldAprilTag(7, 0, -40.5, -180),		// Red, Large
//			new FieldAprilTag(8, 0,-35.5, -180),		// Red, Small
//			new FieldAprilTag(9, 0,35.5, -180),		// Blue, Small
//			new FieldAprilTag(10, 0,40.5, -180)		// Blue, Large

    };

    public SUB_Vision(OpMode p_opMode, final String usbCameraName,
                                             double cameraOffsetX, double cameraOffsetY, double cameraOffsetAngle
            ,VisionProcessorBase p_autoDetectVProcessor) {
        m_opMode = p_opMode;
        m_cameraName = usbCameraName;
        m_cameraOffsetX = cameraOffsetX;
        m_cameraOffsetY = cameraOffsetY;
        m_cameraOffsetAngle = cameraOffsetAngle;
        m_autonomousDetectVProcessor = p_autoDetectVProcessor;
        initAprilTag();
    }

    @Override
    public void periodic() {
//          telemetry();
    }

    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
        m_aprilTag = new AprilTagProcessor.Builder().build();

        // Create the vision portal by using a builder.
        m_visionPortal = new VisionPortal.Builder()
                .setCamera(m_opMode.hardwareMap.get(WebcamName.class, m_cameraName))
                .addProcessor(m_aprilTag)
                .addProcessor(m_autonomousDetectVProcessor)
                .build();

        m_lastFrameID = -1;
    }

    // See https://benwinding.github.io/kalmanjs-examples/examples/demo2-vue.html for an
    // interactive example of setting the R/Q/A/B/C values.
    public void getRobotPoseStart() {
        m_xPosKalman = new KalmanFilter(0.01, 10, 1, 0, 1);
        m_yPosKalman = new KalmanFilter(0.01, 10, 1, 0, 1);
        m_hPosKalman = new KalmanFilter(0.01, 10, 1, 0, 1);
        m_xPosFiltered = Double.NaN;
        m_yPosFiltered = Double.NaN;
        m_hPosFiltered = Double.NaN;
        m_countKalmanPoints = 0;
    }

    public void getRobotPoseUpdate(boolean offset, com.acmerobotics.roadrunner.geometry.Pose2d p_currentPose) {
        List<AprilTagDetection> currentDetections = m_aprilTag.getDetections();

        // Wait until the vision portal processes a new frame--otherwise we are just returning the identical results
        // over and over.
        if (currentDetections.size() > 0 && currentDetections.get(0).frameAcquisitionNanoTime == m_lastFrameID)
            return;

        // find the nearest tag
        AprilTagDetection closestTag = null;
        double distanceToClosestTag = Double.MAX_VALUE;
        for (AprilTagDetection detection : m_aprilTag.getDetections()) {
            if (detection.metadata != null) {
                double distance = detection.ftcPose.range;
                if (distance < distanceToClosestTag) {
                    distanceToClosestTag = distance;
                    closestTag = detection;
                }
            }
        }

        // if the nearest tag is found, save information
        if (closestTag != null) {
            Pose2d pose = getRobotPoseFromAprilTagPose(closestTag.id, closestTag.ftcPose, offset);
            if (pose != null) {
                m_xPosFiltered = m_xPosKalman.filter(pose.getX());
                m_yPosFiltered = m_yPosKalman.filter(pose.getY());
                m_hPosFiltered = m_hPosKalman.filter(pose.getHeading());
                m_countKalmanPoints++;
                m_opMode.telemetry.addData("aprilTag upd", "new (%.2f, %.2f, %.2f) filtered (%.2f, %.2f, %.2f) count %d",
                        pose.getX(), pose.getY(), Math.toDegrees(pose.getHeading()), m_xPosFiltered, m_yPosFiltered, Math.toDegrees(m_hPosFiltered), m_countKalmanPoints);
                m_lastFrameID = closestTag.frameAcquisitionNanoTime;
            }
        }

    }

    public Pose2d getRobotPoseFiltered() {
        if (m_countKalmanPoints > 0)
            return new Pose2d(m_xPosFiltered, m_yPosFiltered, new Rotation2d(angleWrap(m_hPosFiltered)));
        else
            return null;
    }

    public Pose2d getRobotPose(boolean offset) {

        List<AprilTagDetection> currentDetections = m_aprilTag.getDetections();
        AprilTagDetection closestTag= null;

        double distanceToClosestTag = Double.MAX_VALUE;
        for (AprilTagDetection detection : m_aprilTag.getDetections()) {
            if (detection.metadata != null) {
                double distance = detection.ftcPose.range;
                if (distance < distanceToClosestTag) {
                    distanceToClosestTag = distance;
                    closestTag = detection;
                }
            }
        }

        if (closestTag != null) {
            return getRobotPoseFromAprilTagPose(closestTag.id, closestTag.ftcPose, offset);
        }

        return null;
    }

    private Pose2d getRobotPoseFromAprilTagPose(int id, AprilTagPoseFtc ftcPose, boolean offset) {
        int index = 0;
        while (index < m_aprilTagLocations.length) {
            if (m_aprilTagLocations[index].id == id)
                break;
            index++;
        }
        if (index >= m_aprilTagLocations.length)
            return null;

        FieldAprilTag tag = m_aprilTagLocations[index];

        double angle = angleWrap(Math.toRadians(ftcPose.bearing - ftcPose.yaw));
        double x = ftcPose.range * Math.cos(angle);
        double y = ftcPose.range * Math.sin(angle);
        if (tag.angle == 90) {
            x = tag.x + x;
            y = tag.y - y;
            angle = angleWrap(angle + Math.toRadians(90));
        }
        else if (tag.angle == 0) {
            x = tag.x - x;
            y = tag.y - y;
        }
        else if (tag.angle == -90) {
            x = tag.x - x;
            y = tag.y + y;
            angle = angleWrap(angle + Math.toRadians(-90));
        }
        else if (tag.angle == -180) {
            x = tag.x + x;
            y = tag.y + y;
            angle = angleWrap(angle - Math.toRadians(180));
        }
        // from camera position
//          m_opMode.telemetry.addData("XYH", "%6.1f %6.1f %6.1f", x, y, Math.toDegrees(angle));

        // Find the robot's heading in field coordinates.  (Non-audience side is zero, blue side is 90).
        double heading = angleWrap(Math.toRadians(tag.angle + -ftcPose.yaw));

        // The camera usually isn't directly in the middle of the robot. Adjust the pose to
        // include the offset of the camera from the center of the robot.  This only depends on the
        // direction the robot is facing--the aprilTag position isn't needed.
        if (offset) {
            double xVertOffset = Math.abs(m_cameraOffsetX * Math.cos(heading)) * Math.signum(m_cameraOffsetX);
            double xHorzOffset = Math.abs(m_cameraOffsetY * Math.sin(heading)) * Math.signum(m_cameraOffsetY);
            double yVertOffset = Math.abs(m_cameraOffsetX * Math.sin(heading)) * Math.signum(m_cameraOffsetX);
            double yHorzOffset = Math.abs(m_cameraOffsetY * Math.cos(heading)) * Math.signum(m_cameraOffsetY);

            // Adjust robot heading based on the direction the camera is facing.
            heading = angleWrap(heading + -Math.toRadians(m_cameraOffsetAngle));

            // Quad 1 (upper left)
            if (heading >= Math.toRadians(0) && heading <= Math.toRadians(90)) {
                return new Pose2d(x - xVertOffset - xHorzOffset, y - yVertOffset + yHorzOffset, new Rotation2d(heading));
            }
            // Quad 2 (lower left)
            else if (heading > Math.toRadians(90) && heading <= Math.toRadians(180)) {
                return new Pose2d(x + xVertOffset - xHorzOffset, y - yVertOffset - yHorzOffset, new Rotation2d(heading));
            }
            // Quad 3 (lower right)
            else if (heading <= Math.toRadians(-90) && heading >= Math.toRadians(-180)) {
                return new Pose2d(x + xVertOffset + xHorzOffset, y + yVertOffset - yHorzOffset, new Rotation2d(heading));
            }
            // Quad 4 (upper right)
            else {
                return new Pose2d(x - xVertOffset + xHorzOffset, y + yVertOffset + yHorzOffset, new Rotation2d(heading));
            }
        }
        heading = angleWrap(heading);
        return new Pose2d(x, y, new Rotation2d(heading));
    }

    public void telemetry() {
        Pose2d pose = getRobotPose(false);
        if (pose != null)
            m_opMode.telemetry.addData("pose", "XY (%.1f,%.1f) Heading(%.1f)",
                    pose.getX(), pose.getY(), Math.toDegrees(pose.getHeading()));
        pose = getRobotPose(true);
        if (pose != null)
            m_opMode.telemetry.addData("pose+offset", "XY (%.1f,%.1f) Heading(%.1f)",
                    pose.getX(), pose.getY(), Math.toDegrees(pose.getHeading()));

        List<AprilTagDetection> currentDetections = m_aprilTag.getDetections();
        m_opMode.telemetry.addData("# AprilTags Detected", currentDetections.size());

        m_opMode.telemetry.addData("april tag detection", getTagsDetected());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                m_opMode.telemetry.addData("\n==== ID:", "%d %s", detection.id, detection.metadata.name);
                m_opMode.telemetry.addData("XYZ", "%6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z);
                m_opMode.telemetry.addData("PRY", "%6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw);
                m_opMode.telemetry.addData("RBE", "%6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation);
            } else {
                m_opMode.telemetry.addData("\n==== ID:", "%d Unknown", detection.id);
                m_opMode.telemetry.addData("Center", "%6.0f %6.0f   (pixels)", detection.center.x, detection.center.y);
            }
        }   // end for() loop

        // Add "key" information to telemetry
//		m_opMode.telemetry.addData("\nkey", "\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
//		m_opMode.telemetry.addData("PRY", "Pitch, Roll & Yaw (XYZ Rotation)");
//		m_opMode.telemetry.addData("RBE", "Range, Bearing & Elevation");
    }

    private static double angleWrap(double angle) {
        if (angle > 0)
            return ((angle + Math.PI) % (Math.PI * 2)) - Math.PI;
        else
            return ((angle - Math.PI) % (Math.PI * 2)) + Math.PI;

    }

    public void setProcessorDisabled(VisionProcessor p_vp) {
        m_visionPortal.setProcessorEnabled(p_vp,false);
    }

    public void setProcessorEnabled(VisionProcessor p_vp){
        m_visionPortal.setProcessorEnabled(p_vp, true);
    }

    public List<AprilTagDetection> getTagsDetected(){
        return m_aprilTag.getDetections();
    }

    public FieldAprilTag getTagPosition(int tagId){
        return m_aprilTagLocations[tagId];
    }

    public int findClosestTag(com.acmerobotics.roadrunner.geometry.Pose2d p_currentPose){
        int closestTagID = -1;
        double distanceToClosestTag = Double.MAX_VALUE;
        for (AprilTagDetection detection : m_aprilTag.getDetections()) {
            double distance = Math.abs(detection.ftcPose.y - p_currentPose.getY());
            if(distance < distanceToClosestTag){
                distanceToClosestTag = distance;
                closestTagID = detection.id;
            }
        }
        return closestTagID;
    }

    class KalmanFilter {

        private double A = 1;
        private double B = 0;
        private double C = 1;

        private double R;
        private double Q;

        private double cov = Double.NaN;
        private double x = Double.NaN;

        /**
         * Constructor
         *
         * @param R Process noise
         * @param Q Measurement noise
         * @param A State vector
         * @param B Control vector
         * @param C Measurement vector
         */
        public KalmanFilter(double R, double Q, double A, double B , double C){
            this.R = R;
            this.Q = Q;

            this.A = A;
            this.B = B;
            this.C = C;

            this.cov = Double.NaN;
            this.x = Double.NaN; // estimated signal without noise
        }

        /**
         * Constructor
         *
         * @param R Process noise
         * @param Q Measurement noise
         */
        public KalmanFilter(double R, double Q){
            this.R = R;
            this.Q = Q;

        }


        /**
         * Filters a measurement
         *
         * @param measurement The measurement value to be filtered
         * @param u The controlled input value
         * @return The filtered value
         */
        public double filter(double measurement, double u){

            if (Double.isNaN(this.x)) {
                this.x = (1 / this.C) * measurement;
                this.cov = (1 / this.C) * this.Q * (1 / this.C);
            }else {
                double predX = (this.A * this.x) + (this.B * u);
                double predCov = ((this.A * this.cov) * this.A) + this.R;

                // Kalman gain
                double K = predCov * this.C * (1 / ((this.C * predCov * this.C) + this.Q));

                // Correction
                this.x = predX + K * (measurement - (this.C * predX));
                this.cov = predCov - (K * this.C * predCov);
            }
            return this.x;
        }

        /**
         * Filters a measurement
         *
         * @param measurement The measurement value to be filtered
         * @return The filtered value
         */
        public double filter(double measurement){
            double u = 0;
            if (Double.isNaN(this.x)) {
                this.x = (1 / this.C) * measurement;
                this.cov = (1 / this.C) * this.Q * (1 / this.C);
            }else {
                double predX = (this.A * this.x) + (this.B * u);
                double predCov = ((this.A * this.cov) * this.A) + this.R;

                // Kalman gain
                double K = predCov * this.C * (1 / ((this.C * predCov * this.C) + this.Q));

                // Correction
                this.x = predX + K * (measurement - (this.C * predX));
                this.cov = predCov - (K * this.C * predCov);
            }
            return this.x;
        }


        /**
         * Set the last measurement.
         * @return The last measurement fed into the filter
         */
        public double lastMeasurement(){
            return this.x;
        }

        /**
         * Sets measurement noise
         *
         * @param noise The new measurement noise
         */
        public void setMeasurementNoise(double noise){
            this.Q = noise;
        }

        /**
         * Sets process noise
         *
         * @param noise The new process noise
         */
        public void setProcessNoise(double noise){
            this.R = noise;
        }
    }

    public void resumeStreaming(){
        m_visionPortal.resumeStreaming();
    }

    public void stopStreaming(){
        m_visionPortal.stopStreaming();
    }
}