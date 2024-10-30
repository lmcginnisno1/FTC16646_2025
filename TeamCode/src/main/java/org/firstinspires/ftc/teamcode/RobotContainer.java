package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.ftclib.command.Command;
import org.firstinspires.ftc.teamcode.ftclib.command.Robot;
import org.firstinspires.ftc.teamcode.subsystems.*;

public class RobotContainer {
     public boolean m_red = true;
     public Robot m_robot = new Robot();
     public MecanumDriveSubsystem drivetrain;
     public GlobalVariables GlobalVariables;
     public SUB_Odometry m_odometry;
     public SUB_Bucket m_bucket;
     public SUB_SubmersibleIntake m_subIntake;
     public SUB_BucketLift m_bucketLift;
     public SUB_IntakeSubSlide m_intakeSubSlide;
     public SUB_Climb m_climb;

     public RobotContainer(OpMode p_opMode) {
          m_odometry = new SUB_Odometry(p_opMode);
          drivetrain = new MecanumDriveSubsystem(new SampleMecanumDrive(p_opMode.hardwareMap), true);
          GlobalVariables = new GlobalVariables();
          m_bucket = new SUB_Bucket(p_opMode);
          m_subIntake = new SUB_SubmersibleIntake(p_opMode);
          m_bucketLift = new SUB_BucketLift(p_opMode);
          m_intakeSubSlide = new SUB_IntakeSubSlide(p_opMode);
          m_climb = new SUB_Climb(p_opMode);
     };

     public void run() {
          m_robot.run();
     }

     public void reset() {
          m_robot.reset();
     }

     public void schedule(Command... commands) {
          m_robot.schedule(commands);
     }

     public void setRedSide() {
          m_red = true;
     }

     public void setBlueSide() {
          m_red = false;
     }
}

