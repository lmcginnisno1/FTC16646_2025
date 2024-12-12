package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.ftclib.command.SubsystemBase;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

public class SUB_Odometry extends SubsystemBase {
     OpMode m_opMode;
     SparkFunOTOS OTOS;
     public SUB_Odometry(OpMode p_opMode){
          m_opMode = p_opMode;
          OTOS = m_opMode.hardwareMap.get(SparkFunOTOS.class, "OTOS");
          //set linear/angular units
          OTOS.setLinearUnit(DistanceUnit.INCH);
          OTOS.setAngularUnit(AngleUnit.RADIANS);
          //sensor offset from center of bot
          OTOS.setOffset(new SparkFunOTOS.Pose2D(0, -.85, Math.toRadians(-90)));
          //scalar to correct for overshoot/undershoot
          OTOS.setLinearScalar(1.03);//1.036
          OTOS.setAngularScalar(0.994);
          //calibrate and reset position
          OTOS.calibrateImu();
          OTOS.resetTracking();
          OTOS.setPosition(new SparkFunOTOS.Pose2D(0, 0, Math.toRadians(180)));
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
}
