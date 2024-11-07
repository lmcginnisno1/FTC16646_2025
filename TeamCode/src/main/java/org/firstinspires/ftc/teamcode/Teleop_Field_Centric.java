package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.*;

import org.firstinspires.ftc.teamcode.ftclib.command.ConditionalCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.button.GamepadButton;
import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.ftclib.command.Command;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Teleop Red", group ="Teleop Red")
public class Teleop_Field_Centric extends LinearOpMode {

     public RobotContainer m_robot;
     private GamepadEx m_driverOp;
     private GamepadEx m_toolOp;
     private boolean m_setFieldCentric = false;
     double m_startHeadingOffset = Math.toRadians(90);

     private static ElapsedTime m_runTime = new ElapsedTime();
     private ElapsedTime m_releaseTimeout = new ElapsedTime();

     public void initialize() {
          telemetry.clearAll();
          telemetry.addData("init complete", true);

          m_runTime.reset();
     }

     @Override
     public void runOpMode() throws InterruptedException {
          initializeSubsystems();
          // waitForStart();
          while (!opModeIsActive() && !isStopRequested()) {
               telemetry.update();
          }

          m_runTime.reset();
          while (!isStopRequested() && opModeIsActive()) {
               m_robot.run(); // run the scheduler

               m_robot.drivetrain.update();
               Pose2d poseEstimate = m_robot.drivetrain.getPoseEstimate();
               telemetry.addData("ODM:","x[%3.2f] y[%3.2f] heading(%3.2f)", poseEstimate.getX(), poseEstimate.getY(), Math.toDegrees(poseEstimate.getHeading()));
               telemetry.addData("RobotState", m_robot.GlobalVariables.getRobotState().name());
               telemetry.addData("intake mode", m_robot.GlobalVariables.getIntakeState().name());
               telemetry.update();
          }

          //
          endOfOpMode();
          m_robot.reset();
     }

     public void endOfOpMode() {

     }

     public void initializeSubsystems() {
          m_robot = new RobotContainer(this);
          m_driverOp = new GamepadEx(gamepad1);
          m_toolOp = new GamepadEx(gamepad2);

          setSide();

          //drivetrain initialization
          m_robot.drivetrain.setFieldCentric(true);
          m_robot.drivetrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
          m_robot.drivetrain.setDefaultCommand(new RR_MecanumDriveDefault(m_robot.drivetrain, m_driverOp,0.0,0.01, m_robot.GlobalVariables));
          m_robot.m_subIntake.setDefaultCommand(new CMD_SubIntakeDefault(m_driverOp, m_robot.m_subIntake, m_robot.GlobalVariables));
          //button bindings and global variables initialization
//          m_robot.m_intakeSubSlide.setDefaultCommand(new CMD_SubSlideDefault(m_driverOp, m_robot.GlobalVariables, m_robot.m_intakeSubSlide));
          configureButtonBindings();
     }

     public void configureButtonBindings() {
          AddButtonCommand(m_driverOp, GamepadKeys.Button.LEFT_BUMPER, new CMD_HandleReadyToIntake(m_robot));
          AddButtonCommand(m_driverOp, GamepadKeys.Button.RIGHT_BUMPER, new CMD_HandleReadyToDeploy(m_robot));
          AddButtonCommand(m_driverOp, GamepadKeys.Button.BACK, new CMD_ResetToHome(m_robot.GlobalVariables,
                  m_robot.m_bucketLift, m_robot.m_intakeSubSlide, m_robot.m_bucket, m_robot.m_subIntake));

          AddButtonCommand(m_driverOp, GamepadKeys.Button.A, new ConditionalCommand(
               new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOff))
               ,new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
               ,()-> m_robot.m_subIntake.isIntaking()
          ));

          AddButtonCommand(m_driverOp, GamepadKeys.Button.B, new ConditionalCommand(
                  new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketReadyToIntake))
                  ,new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
                  ,()-> m_robot.m_subIntake.isBucketIntake()
          ));

          AddButtonCommand(m_driverOp, GamepadKeys.Button.X, new SequentialCommandGroup(
                  new InstantCommand(()-> m_robot.GlobalVariables.setIntakeState(GlobalVariables.IntakeState.SUBMERSIBLE))
                  ,new CMD_HandleReadyToIntake(m_robot)
          ));
          AddButtonCommand(m_driverOp, GamepadKeys.Button.Y, new SequentialCommandGroup(
                  new InstantCommand(()-> m_robot.GlobalVariables.setIntakeState(GlobalVariables.IntakeState.WALL))
                  ,new CMD_HandleReadyToIntake(m_robot)
          ));

          AddButtonCommand(m_driverOp, GamepadKeys.Button.DPAD_LEFT, new ConditionalCommand(
             new InstantCommand(()-> m_robot.m_intakeSubSlide.extend())
             ,new InstantCommand()
             ,()-> m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.READY_TO_INTAKE)
                  && m_robot.GlobalVariables.isIntakeState(GlobalVariables.IntakeState.SUBMERSIBLE)
          ));

          AddButtonCommand(m_driverOp, GamepadKeys.Button.DPAD_RIGHT, new ConditionalCommand(
                  new InstantCommand(()-> m_robot.m_intakeSubSlide.retract())
                  ,new InstantCommand()
                  ,()-> m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.READY_TO_INTAKE)
                  && m_robot.GlobalVariables.isIntakeState(GlobalVariables.IntakeState.SUBMERSIBLE)
          ));

          //toolOp
          AddButtonCommand(m_toolOp, GamepadKeys.Button.A, new CMD_Climb(m_robot.GlobalVariables, m_robot.m_climb));
          AddButtonCommand(m_toolOp, GamepadKeys.Button.B, new CMD_ResetToHome(m_robot.GlobalVariables,
                  m_robot.m_bucketLift, m_robot.m_intakeSubSlide, m_robot.m_bucket, m_robot.m_subIntake));
          AddButtonCommand(m_toolOp, GamepadKeys.Button.X, new CMD_HandleException(m_robot));
          AddButtonCommand(m_toolOp, GamepadKeys.Button.Y, new ConditionalCommand(
               new InstantCommand(()-> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketDeploy))
               ,new InstantCommand(()-> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
               ,()-> m_robot.m_bucket.isBucketHome()
          ));
          AddButtonCommand(m_toolOp, GamepadKeys.Button.BACK, new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(-1)));
          AddButtonCommand(m_toolOp, GamepadKeys.Button.START, new InstantCommand(()-> m_robot.m_climb.reset()));
          AddButtonCommand(m_toolOp, GamepadKeys.Button.LEFT_BUMPER, new InstantCommand(()-> m_robot.drivetrain.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(90)))));
     }

     public void setSide() {
          if(GlobalVariables.bucketAuto){
               m_robot.drivetrain.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(135)));
          }else{
               m_robot.drivetrain.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(180)));
          }
          m_robot.setRedSide();
     }

     public void AddButtonCommand(GamepadEx gamepad, GamepadKeys.Button button, Command command) {
          (new GamepadButton(gamepad, button)).whenPressed(command);
     }

     public void AddButtonCommandNoInt(GamepadEx gamepad, GamepadKeys.Button button, Command command) {
          (new GamepadButton(gamepad, button)).whenPressed(command, false);
     }
}