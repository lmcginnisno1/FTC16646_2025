package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Robot_Auto;
import org.firstinspires.ftc.teamcode.commands.*;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;

@Autonomous(name = "Red Basket", preselectTeleOp = "Teleop Red", group = "Auto Red")
public class AUTO_RedBasket extends Robot_Auto {

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
        Trajectory strafeOffWall = createStrafeOffWallTrajectory();
        Trajectory moveToBasket = createMoveToBasketTrajectory(strafeOffWall.end());

        // First Sample
        Trajectory driveIntoFirstSample = createDriveIntoFirstSampleTrajectory(moveToBasket.end());
        Trajectory scoreFirstSample = createScoreFirstSampleTrajectory(driveIntoFirstSample.end());

        // Second Sample
        Trajectory lineUpSecondSample = createLineUpSecondSampleTrajectory(scoreFirstSample.end());
        Trajectory driveIntoSecondSample = createDriveIntoSecondSampleTrajectory(lineUpSecondSample.end());
        Trajectory scoreSecondSample = createScoreSecondSampleTrajectory(driveIntoSecondSample.end());

        // Third Sample
        Trajectory lineUpThirdSample = createLineUpThirdSampleTrajectory(scoreSecondSample.end());
        Trajectory driveIntoThirdSample = createDriveIntoThirdSampleTrajectory(lineUpThirdSample.end());
        Trajectory scoreThirdSample = createScoreThirdSampleTrajectory(driveIntoThirdSample.end());

        SequentialCommandGroup cmds = new SequentialCommandGroup();
        cmds.addCommands(
                placePreload(strafeOffWall, moveToBasket),
                intakeAndScoreFirstSample(driveIntoFirstSample, scoreFirstSample),
                intakeAndScoreSecondSample(lineUpSecondSample, driveIntoSecondSample, scoreSecondSample),
                intakeAndScoreThirdSample(lineUpThirdSample, driveIntoThirdSample, scoreThirdSample),
                new CMD_ResetToHome(m_robot.GlobalVariables, m_robot.m_bucketLift, m_robot.m_intakeSubSlide,
                        m_robot.m_bucket, m_robot.m_subIntake)
        );
        return cmds;
    }

    private SequentialCommandGroup placePreload(Trajectory strafeOffWall, Trajectory moveToBasket) {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift),
                        new SequentialCommandGroup(
                                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, strafeOffWall),
                                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, moveToBasket)
                        )
                ),
                new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket),
                new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(35))
        );
    }

    private SequentialCommandGroup intakeAndScoreFirstSample(Trajectory driveIntoSample, Trajectory scoreSample) {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(() -> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome)),
                        new InstantCommand(() -> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome)),
                        new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
                ),
                new InstantCommand(() -> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn)),
                new InstantCommand(() -> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake)),
                new ParallelCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, driveIntoSample),
                        new InstantCommand(() -> m_robot.m_intakeSubSlide.setTargetPosition(1000)),
                        new SequentialCommandGroup(
                                new WaitCommand(1500),
                                new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                        )
                ),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, scoreSample),
                        new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                ),
                new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket),
                new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(45))
        );
    }

    private SequentialCommandGroup intakeAndScoreSecondSample(Trajectory lineUpSample, Trajectory driveIntoSample, Trajectory scoreSample) {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(() -> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome)),
                        new InstantCommand(() -> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome)),
                        new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake),
                        new InstantCommand(() -> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn)),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, lineUpSample)
                ),
                new InstantCommand(() -> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake)),
                new ParallelCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, driveIntoSample),
                        new InstantCommand(() -> m_robot.m_intakeSubSlide.setTargetPosition(1000))
                ),
                new WaitCommand(500),
                new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, scoreSample),
                        new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                ),
                new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    private SequentialCommandGroup intakeAndScoreThirdSample(Trajectory lineUpSample, Trajectory driveIntoSample, Trajectory scoreSample) {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(() -> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome)),
                        new InstantCommand(() -> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome)),
                        new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake),
                        new InstantCommand(() -> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn)),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, lineUpSample)
                ),
                new InstantCommand(() -> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake)),
                new ParallelCommandGroup(
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, driveIntoSample),
                        new InstantCommand(() -> m_robot.m_intakeSubSlide.setTargetPosition(1000))
                ),
                new WaitCommand(500),
                new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                        new RR_TrajectoryFollowerCommand(m_robot.drivetrain, scoreSample),
                        new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                ),
                new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    // Trajectory creation methods
    private Trajectory createStrafeOffWallTrajectory() {
        return m_robot.drivetrain.trajectoryBuilder(getStartingPose(), false)
                .strafeRight(12)
                .build();
    }

    private Trajectory createMoveToBasketTrajectory(Pose2d startPose) {
        return m_robot.drivetrain.trajectoryBuilder(startPose, false)
                .lineToLinearHeading(new Pose2d(-58, -58, Math.toRadians(-135)))
                .build();
    }

    private Trajectory createDriveIntoFirstSampleTrajectory(Pose2d startPose) {
        return m_robot.drivetrain.trajectoryBuilder(
                        new Pose2d(startPose.getX(), startPose.getY(), Math.toRadians(-100)), true)
                .lineToConstantHeading(new Vector2d(-48, -40))
                .build();
    }

    private Trajectory createScoreFirstSampleTrajectory(Pose2d endPose) {
        return m_robot.drivetrain.trajectoryBuilder(endPose, false)
                .lineToLinearHeading(new Pose2d(-56, -56, Math.toRadians(-135)))
                .build();
    }

    private Trajectory createLineUpSecondSampleTrajectory(Pose2d endPose) {
        return m_robot.drivetrain.trajectoryBuilder(endPose, true)
                .lineToLinearHeading(new Pose2d(-60, -52, Math.toRadians(-90)))
                .build();
    }

    private Trajectory createDriveIntoSecondSampleTrajectory(Pose2d startPose) {
        return m_robot.drivetrain.trajectoryBuilder(startPose, true)
                .lineToLinearHeading(new Pose2d(-61.5, -41, Math.toRadians(-90)))
                .build();
    }

    private Trajectory createScoreSecondSampleTrajectory(Pose2d endPose) {
        return m_robot.drivetrain.trajectoryBuilder(endPose, false)
                .lineToLinearHeading(new Pose2d(-56, -56, Math.toRadians(-135)))
                .build();
    }

    private Trajectory createLineUpThirdSampleTrajectory(Pose2d endPose) {
        return m_robot.drivetrain.trajectoryBuilder(endPose, true)
                .lineToLinearHeading(new Pose2d(-40, -34, Math.toRadians(-20)))
                .build();
    }

    private Trajectory createDriveIntoThirdSampleTrajectory(Pose2d startPose) {
        return m_robot.drivetrain.trajectoryBuilder(startPose, true)
                .lineToLinearHeading(new Pose2d(-51, -31, Math.toRadians(-20)))
                .build();
    }

    private Trajectory createScoreThirdSampleTrajectory(Pose2d endPose) {
        return m_robot.drivetrain.trajectoryBuilder(endPose, false)
                .lineToLinearHeading(new Pose2d(-56, -56, Math.toRadians(-135)))
                .build();
    }
}
