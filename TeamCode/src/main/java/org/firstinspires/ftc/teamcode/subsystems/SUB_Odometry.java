package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.SparkFunOTOS;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class SUB_Odometry {
     OpMode m_opMode;
     SparkFunOTOS OTOS;
     public SUB_Odometry(OpMode p_opMode){
          m_opMode = p_opMode;
          OTOS = m_opMode.hardwareMap.get(SparkFunOTOS.class, "OTOS");
          //set linear/angular units
          OTOS.setLinearUnit(DistanceUnit.INCH);
          OTOS.setAngularUnit(AngleUnit.RADIANS);
          //sensor offset from center of bot
          OTOS.setOffset(new SparkFunOTOS.Pose2D(0, -.85, 0));
          //scalar to correct for overshoot/undershoot
          OTOS.setLinearScalar(1.03);//1.036
          OTOS.setAngularScalar(0.994);
          //calibrate and reset position
          OTOS.calibrateImu();
          OTOS.resetTracking();
          SparkFunOTOS.Pose2D currentPosition = new SparkFunOTOS.Pose2D(0, 0, 0);
          OTOS.setPosition(currentPosition);
     }

     public void resetPosition(SparkFunOTOS.Pose2D p_pose){
          OTOS.resetTracking();
          OTOS.setPosition(p_pose);
     }

     public Pose2d getPosition(){
          SparkFunOTOS.Pose2D OTOSpose = OTOS.getPosition();
          return new Pose2d(OTOSpose.x, OTOSpose.y);
     }

     public double getHeadingRad(){
          return OTOS.getPosition().h;
     }

     public double getRotationRateRad(){
          return OTOS.getVelocity().h;
     }
}
