package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.Robot_Auto;
import org.firstinspires.ftc.teamcode.commands.CMD_DeployBucket;
import org.firstinspires.ftc.teamcode.commands.CMD_IntakeSub;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToDeployBucket;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToIntakeSub;
import org.firstinspires.ftc.teamcode.commands.CMD_StowSub;
import org.firstinspires.ftc.teamcode.commands.RR_TrajectoryFollowerCommand;
import org.firstinspires.ftc.teamcode.commands.RR_TurnCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;

@Autonomous(name="Red Basket Fast", preselectTeleOp="Teleop Red", group="Auto Red")
public class AUTO_RedBasketFast extends Robot_Auto {
    Trajectory m_moveToBasket;
    Trajectory m_intakeSecondSample;
    Trajectory m_scoreSecondSample;
    Trajectory m_intakeThirdSample;
    Trajectory m_scoreThirdSample;
    Trajectory m_lineUpFourthSample;
    Trajectory m_intakeFourthSample;
    Trajectory m_scoreFourthSample;

    @Override
    public void prebuildTasks() {
        //start pose
        setStartingPose(new Pose2d(-41, -63.625, Math.toRadians(180)));
        //trajectory compilation, runs in init to avoid lag at start button press
        m_moveToBasket = m_robot.drivetrain.trajectoryBuilder(getStartingPose(), false)
                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(-135)))
                .build();

        m_intakeSecondSample = m_robot.drivetrain.trajectoryBuilder(new Pose2d(m_moveToBasket.end().getX(),
                m_moveToBasket.end().getY(), Math.toRadians(-102.5)), true)
                .back(12)
                .build();

        m_scoreSecondSample = m_robot.drivetrain.trajectoryBuilder(m_intakeSecondSample.end(), false)
                .lineToLinearHeading(new Pose2d(-57, -56.5, Math.toRadians(-135)))
                .build();

        m_intakeThirdSample = m_robot.drivetrain.trajectoryBuilder(new Pose2d(m_scoreSecondSample.end().getX(),
                m_scoreSecondSample.end().getY(), Math.toRadians(-85)),true)
                .back(10)
                .build();

        m_scoreThirdSample = m_robot.drivetrain.trajectoryBuilder(m_intakeThirdSample.end(), false)
                .lineToLinearHeading(new Pose2d(-56.75, -56.75, Math.toRadians(-135)))
                .build();

        m_lineUpFourthSample = m_robot.drivetrain.trajectoryBuilder(m_scoreThirdSample.end(), true)
                .lineToLinearHeading(new Pose2d(-39, -35, Math.toRadians(-14)))
                .build();
        m_intakeFourthSample = m_robot.drivetrain.trajectoryBuilder(m_lineUpFourthSample.end(), true)
                .lineToLinearHeading(new Pose2d(-50, -32, Math.toRadians(-14)))
                .build();
        m_scoreFourthSample = m_robot.drivetrain.trajectoryBuilder(m_intakeFourthSample.end(), false)
                .lineToLinearHeading(new Pose2d(-53, -55.25, Math.toRadians(-135)))
                .build();
    }

    @Override
    public SequentialCommandGroup buildTasks() {
        SequentialCommandGroup completeTasks = new SequentialCommandGroup();
        completeTasks.addCommands(
            new InstantCommand(()-> GlobalVariables.bucketAuto = true)
            ,preload()
            ,secondSample()
            ,thirdSample()
            ,fourthSample()
        );
        m_robot.schedule(completeTasks);
        return completeTasks;
    }

    SequentialCommandGroup preload(){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift),
                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_moveToBasket)
            ),
            new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    SequentialCommandGroup secondSample(){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(32.5))
                ,new InstantCommand(() -> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
                ,new InstantCommand(() -> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome))
                ,new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
            )
            ,new InstantCommand(() -> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
            ,new InstantCommand(() -> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
            ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_intakeSecondSample)
            ,new WaitCommand(500)
            ,new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
            ,new WaitCommand(500)
            ,new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
            ,new ParallelCommandGroup(
                new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_scoreSecondSample)
            )
            ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    SequentialCommandGroup thirdSample(){
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(50))
                        ,new InstantCommand(() -> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
                        ,new InstantCommand(() -> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome))
                        ,new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
                )
                ,new InstantCommand(() -> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
                ,new InstantCommand(() -> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_intakeThirdSample)
                ,new WaitCommand(500)
                ,new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                ,new WaitCommand(500)
                ,new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                ,new ParallelCommandGroup(
                new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_scoreThirdSample)
        )
                ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    SequentialCommandGroup fourthSample(){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                new InstantCommand(() -> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome)),
                new InstantCommand(() -> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftHome)),
                new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake),
                new InstantCommand(() -> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn)),
                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_lineUpFourthSample)
            ),
            new InstantCommand(() -> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake)),
            new ParallelCommandGroup(
                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_intakeFourthSample),
                new InstantCommand(() -> m_robot.m_intakeSubSlide.setTargetPosition(1000))
            ),
            new WaitCommand(500),
            new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
            new WaitCommand(500),
            new ParallelCommandGroup(
                new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket),
                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_scoreFourthSample),
                new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
            ),
            new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }
}
