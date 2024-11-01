package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.Robot_Auto;
import org.firstinspires.ftc.teamcode.commands.CMD_BucketLiftReset;
import org.firstinspires.ftc.teamcode.commands.CMD_DeployBucket;
import org.firstinspires.ftc.teamcode.commands.CMD_HomeBucket;
import org.firstinspires.ftc.teamcode.commands.CMD_IntakeSub;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToDeployBucket;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToIntakeSub;
import org.firstinspires.ftc.teamcode.commands.CMD_ResetToHome;
import org.firstinspires.ftc.teamcode.commands.CMD_StowSub;
import org.firstinspires.ftc.teamcode.commands.RR_TrajectoryFollowerCommand;
import org.firstinspires.ftc.teamcode.commands.RR_TurnCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;
import org.opencv.core.Mat;

@Autonomous(name="Blue Basket", preselectTeleOp="Teleop Blue", group="Auto Blue")
public class AUTO_BlueBasket extends Robot_Auto {

    @Override
    public void prebuildTasks() {
        setStartingPose(new Pose2d(41, 63.625, Math.toRadians(0)));
    }

    @Override
    public SequentialCommandGroup buildTasks() {
        SequentialCommandGroup completeTasks = new SequentialCommandGroup();
        completeTasks.addCommands(
                placePreloadBasket()
        );
        m_robot.schedule(completeTasks);
        return completeTasks;
    }

    SequentialCommandGroup placePreloadBasket(){
        Trajectory strafeOffWall = m_robot.drivetrain.trajectoryBuilder(getStartingPose(), false)
                .strafeRight(12)
                .build();

        Trajectory moveToBasket = m_robot.drivetrain.trajectoryBuilder(strafeOffWall.end(), false)
                .lineToLinearHeading(new Pose2d(58.5, 58.5, Math.toRadians(45)))
                .build();

        Trajectory driveIntoSample = m_robot.drivetrain.trajectoryBuilder(new Pose2d(moveToBasket.end().getX(), moveToBasket.end().getY(), Math.toRadians(80)), true)
                .lineToConstantHeading(new Vector2d(48, 40))
                .build();

        Trajectory scoreSecondSample = m_robot.drivetrain.trajectoryBuilder(driveIntoSample.end(), false)
                .lineToLinearHeading(new Pose2d(57, 57, Math.toRadians(45)))
                .build();

        Trajectory lineUpThirdSample = m_robot.drivetrain.trajectoryBuilder(scoreSecondSample.end(), true)
                .lineToLinearHeading(new Pose2d(60, 52, Math.toRadians(90)))
                .build();

        Trajectory driveIntoThirdSample = m_robot.drivetrain.trajectoryBuilder(lineUpThirdSample.end(), true)
                .lineToLinearHeading(new Pose2d(62, 41, Math.toRadians(90)))
                .build();

        Trajectory scoreThirdSample = m_robot.drivetrain.trajectoryBuilder(driveIntoThirdSample.end(), false)
                .lineToLinearHeading(new Pose2d(56.5, 56.5, Math.toRadians(45)))
                .build();

        Trajectory lineUpFourthSample = m_robot.drivetrain.trajectoryBuilder(scoreThirdSample.end(), true)
                .lineToLinearHeading(new Pose2d(40, 34, Math.toRadians(165)))
                .build();

        Trajectory driveIntoFourthSample = m_robot.drivetrain.trajectoryBuilder(lineUpFourthSample.end(), true)
                .lineToLinearHeading(new Pose2d(51, 31, Math.toRadians(165)))
                .build();

        Trajectory scoreFourthSample = m_robot.drivetrain.trajectoryBuilder(driveIntoFourthSample.end(), false)
                .lineToLinearHeading(new Pose2d(56, 56, Math.toRadians(45)))
                .build();

        SequentialCommandGroup cmds = new SequentialCommandGroup();
        cmds.addCommands(
                new ParallelCommandGroup(
                        new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                        ,new SequentialCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, strafeOffWall)
                        ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, moveToBasket)
                )
                )
                ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
                ,new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(35))
                ,new ParallelCommandGroup(
                        new InstantCommand(()-> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
                        ,new InstantCommand(()-> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome))
                        ,new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
                )
                ,new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
                ,new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
                ,new ParallelCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, driveIntoSample)
                        ,new InstantCommand(()-> m_robot.m_intakeSubSlide.setTargetPosition(1000))
                        ,new SequentialCommandGroup(
                        new WaitCommand(1500)
                        ,new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                )
                )
                ,new WaitCommand(500)
                ,new ParallelCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                        ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, scoreSecondSample)
                        ,new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                )
                ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
                ,new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(45))
//            ,new ParallelCommandGroup(
                ,new InstantCommand(()-> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
                ,new InstantCommand(()-> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome))
//                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, lineUpThirdSample)
//            )
                ,new ParallelCommandGroup(
                        new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
                        ,new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
                )
                ,new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
                ,new ParallelCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, driveIntoThirdSample)
                        ,new InstantCommand(()-> m_robot.m_intakeSubSlide.setTargetPosition(1000))
                )
                ,new WaitCommand(500)
                ,new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                ,new WaitCommand(500)
                ,new ParallelCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                        ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, scoreThirdSample)
                        ,new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                )
                ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
                ,new ParallelCommandGroup(
                        new InstantCommand(()-> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
                        ,new InstantCommand(()-> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome))
                        ,new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
                        ,new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
                        ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, lineUpFourthSample)
                )
                ,new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
                ,new ParallelCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, driveIntoFourthSample)
                        ,new InstantCommand(()-> m_robot.m_intakeSubSlide.setTargetPosition(1000))
                )
                ,new WaitCommand(500)
                ,new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                ,new WaitCommand(500)
                ,new ParallelCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                        ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, scoreFourthSample)
                        ,new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                )
                ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
                ,new CMD_ResetToHome(m_robot.GlobalVariables, m_robot.m_bucketLift, m_robot.m_intakeSubSlide, m_robot.m_bucket, m_robot.m_subIntake)
        );
        return cmds;
    }
}
