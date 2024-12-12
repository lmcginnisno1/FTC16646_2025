package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;


import com.acmerobotics.roadrunner.geometry.Pose2d;

import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class Robot_Auto extends LinearOpMode {

     public RobotContainer m_robot;

     private boolean m_redAlliance;

     private Pose2d m_startingPose = new Pose2d(0, 0, 0);
     SequentialCommandGroup tasks;

     private ElapsedTime m_runTime = new ElapsedTime();

     public void initialize() {
          telemetry.clearAll();
          telemetry.addData("init complete", "BaseRobot");
     }

     @Override
     public void runOpMode() throws InterruptedException {
          initializeSubsystems();

          prebuildTasks();
          // waitForStart();
          while (!opModeIsActive() && !isStopRequested()) {
               m_robot.run(); // run the scheduler
               telemetry.addData("Analysis: ", 1);
               telemetry.update();
          }

//          m_robot.m_camera.resumeStreaming();

          buildTasks();

          m_runTime.reset();

          while (!isStopRequested() && opModeIsActive()) {
               m_robot.run(); // run the scheduler
               m_robot.drivetrain.update();
               Pose2d poseEstimate = m_robot.drivetrain.getPoseEstimate();
               GlobalVariables.m_autonomousEndPose = poseEstimate;
               telemetry.addData("ODM","x[%3.2f] y[%3.2f] heading(%3.2f)", poseEstimate.getX(), poseEstimate.getY(), Math.toDegrees(poseEstimate.getHeading()));
               telemetry.update();
          }

          endOfOpMode();
          m_robot.reset();
     }

     public void endOfOpMode() {
     }

     public void initializeSubsystems() {
          m_robot = new RobotContainer(this);
     }

     public void setStartingPose(Pose2d p_pose) {
          m_startingPose = p_pose;
          m_robot.drivetrain.setPoseEstimate(m_startingPose);
          m_robot.m_odometry.resetPosition(new SparkFunOTOS.Pose2D(m_startingPose.getX(),
                  m_startingPose.getY(), m_startingPose.getHeading()));
     }

     public Pose2d getStartingPose() {
          return m_startingPose;
     }

     public abstract SequentialCommandGroup buildTasks();
     public abstract void prebuildTasks();

}