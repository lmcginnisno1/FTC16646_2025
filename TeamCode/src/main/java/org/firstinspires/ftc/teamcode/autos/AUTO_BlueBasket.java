package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.Robot_Auto;
import org.firstinspires.ftc.teamcode.commands.*;
import org.firstinspires.ftc.teamcode.ftclib.command.*;

@Autonomous(name = "Blue Basket", preselectTeleOp = "Teleop Blue", group = "Auto Blue")
public class AUTO_BlueBasket extends Robot_Auto {
    Trajectory moveToBasket;

    // First Sample
    Trajectory driveIntoSecondSample;
    Trajectory scoreSecondSample;

    // Second Sample
    Trajectory lineUpThirdSample;
    Trajectory driveIntoThirdSample;
    Trajectory scoreThirdSample;

    // Third Sample
    Trajectory lineUpFourthSample;
    Trajectory driveIntoFourthSample;
    Trajectory scoreFourthSample;
    @Override
    public void prebuildTasks() {
        setStartingPose(new Pose2d(-41, -63.625, Math.toRadians(180)));
    }

    @Override
    public SequentialCommandGroup buildTasks() {
        SequentialCommandGroup completeTasks = new SequentialCommandGroup();
        completeTasks.addCommands(placePreloadBasket());
        m_robot.schedule(completeTasks);
        return completeTasks;
    }

    private SequentialCommandGroup placePreloadBasket() {
        // Define trajectories
        moveToBasket = m_robot.drivetrain.trajectoryBuilder(getStartingPose(), false)
                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(-135)))
                .build();

        // Second Sample
        driveIntoSecondSample = m_robot.drivetrain.trajectoryBuilder(new Pose2d(moveToBasket.end().getX(), moveToBasket.end().getY(), Math.toRadians(-100)), true)
                .lineToConstantHeading(new Vector2d(-50, -44))
                .build();
        scoreSecondSample = m_robot.drivetrain.trajectoryBuilder(driveIntoSecondSample.end(), false)
                .lineToLinearHeading(new Pose2d(-57, -56.5, Math.toRadians(-135)))
                .build();

        // Second Sample
        lineUpThirdSample = m_robot.drivetrain.trajectoryBuilder(new Pose2d(scoreSecondSample.end().getX(),
                        scoreSecondSample.end().getY(), Math.toRadians(-90)), true)
                .lineToLinearHeading(new Pose2d(-60, -52, Math.toRadians(-90)))
                .build();
        driveIntoThirdSample = m_robot.drivetrain.trajectoryBuilder(lineUpThirdSample.end(), true)
                .lineToLinearHeading(new Pose2d(-60, -45, Math.toRadians(-90)))
                .build();
        scoreThirdSample = m_robot.drivetrain.trajectoryBuilder(driveIntoThirdSample.end(), false)
                .lineToLinearHeading(new Pose2d(-56.75, -56.75, Math.toRadians(-135)))
                .build();

        //Fourth Sample
        lineUpFourthSample = m_robot.drivetrain.trajectoryBuilder(scoreThirdSample.end(), true)
                .lineToLinearHeading(new Pose2d(-39, -35, Math.toRadians(-13)))
                .build();
        driveIntoFourthSample = m_robot.drivetrain.trajectoryBuilder(lineUpFourthSample.end(), true)
                .lineToLinearHeading(new Pose2d(-50, -32, Math.toRadians(-13)))
                .build();
        scoreFourthSample = m_robot.drivetrain.trajectoryBuilder(driveIntoFourthSample.end(), false)
                .lineToLinearHeading(new Pose2d(-55.25, -55.25, Math.toRadians(-135)))
                .build();

        SequentialCommandGroup cmds = new SequentialCommandGroup();
        cmds.addCommands(
                new InstantCommand(()-> GlobalVariables.bucketAuto = true),
                placePreload(),
                intakeAndScoreFirstSample(),
                intakeAndScoreSecondSample(),
                intakeAndScoreThirdSample(),
                new CMD_ResetToHome(m_robot.GlobalVariables, m_robot.m_bucketLift, m_robot.m_intakeSubSlide,
                        m_robot.m_bucket, m_robot.m_subIntake)
        );
        return cmds;
    }

    private SequentialCommandGroup placePreload() {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, moveToBasket)
                ),
                new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    private SequentialCommandGroup intakeAndScoreFirstSample() {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(35)),
                        new InstantCommand(() -> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome)),
                        new InstantCommand(() -> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome)),
                        new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
                ),
                new InstantCommand(() -> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn)),
                new InstantCommand(() -> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake)),
                new ParallelCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, driveIntoSecondSample),
                        new InstantCommand(() -> m_robot.m_intakeSubSlide.setTargetPosition(1000)),
                        new SequentialCommandGroup(
                                new WaitCommand(1500),
                                new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                        )
                ),
                new WaitCommand(1000),
                new ParallelCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, scoreSecondSample),
                        new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                ),
                new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    private SequentialCommandGroup intakeAndScoreSecondSample() {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(() -> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome)),
                        new InstantCommand(() -> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome)),
                        new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake),
                        new InstantCommand(() -> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn)),
                        new SequentialCommandGroup(
                                new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(45)),
                                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, lineUpThirdSample)
                        )
                ),
                new InstantCommand(() -> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake)),
                new ParallelCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, driveIntoThirdSample),
                        new InstantCommand(() -> m_robot.m_intakeSubSlide.setTargetPosition(1000))
                ),
                new WaitCommand(500),
                new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                new WaitCommand(1000),
                new ParallelCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, scoreThirdSample),
                        new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                ),
                new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    private SequentialCommandGroup intakeAndScoreThirdSample() {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(() -> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome)),
                        new InstantCommand(() -> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome)),
                        new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake),
                        new InstantCommand(() -> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn)),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, lineUpFourthSample)
                ),
                new InstantCommand(() -> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake)),
                new ParallelCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, driveIntoFourthSample),
                        new InstantCommand(() -> m_robot.m_intakeSubSlide.setTargetPosition(1000))
                ),
                new WaitCommand(500),
                new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, scoreFourthSample),
                        new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                ),
                new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }
}