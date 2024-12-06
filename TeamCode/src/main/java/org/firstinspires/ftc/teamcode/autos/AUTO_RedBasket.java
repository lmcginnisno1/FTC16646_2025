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

@Autonomous(name="Red Basket", preselectTeleOp="Teleop Red", group="Auto Red")
public class AUTO_RedBasket extends Robot_Auto {
    Trajectory m_moveToBasket;
    Trajectory m_intakeSampleTwo;
    Trajectory m_scoreSampleTwo;
    Trajectory m_intakeSampleThree;
    Trajectory m_scoreSampleThree;
    Trajectory m_intakeSampleFour;
    Trajectory m_scoreSampleFour;
    Trajectory m_park;

    @Override
    public void prebuildTasks() {
        //start pose
        setStartingPose(new Pose2d(-41, -63.625, Math.toRadians(180)));
        //trajectory compilation, runs in init to avoid lag at start button press
        m_moveToBasket = m_robot.drivetrain.trajectoryBuilder(getStartingPose(), false)
                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(-135)))
                .build();

        m_intakeSampleTwo = m_robot.drivetrain.trajectoryBuilder(new Pose2d(m_moveToBasket.end().getX(),
                m_moveToBasket.end().getY(), Math.toRadians(-67.5)), true)
                .back(12)
                .build();

        m_scoreSampleTwo = m_robot.drivetrain.trajectoryBuilder(m_intakeSampleTwo.end(), false)
                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(-135)))
                .build();

        m_intakeSampleThree = m_robot.drivetrain.trajectoryBuilder(new Pose2d(m_scoreSampleTwo.end().getX()
            ,m_scoreSampleTwo.end().getY(), Math.toRadians(-85)), true)
                .back(12)
                .build();

        m_scoreSampleThree = m_robot.drivetrain.trajectoryBuilder(m_intakeSampleThree.end(), false)
                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(-135)))
                .build();

        m_intakeSampleFour = m_robot.drivetrain.trajectoryBuilder(new Pose2d(m_scoreSampleThree.end().getX()
                        ,m_scoreSampleThree.end().getY(), Math.toRadians(-105)), true)
                .back(12)
                .build();

        m_scoreSampleFour = m_robot.drivetrain.trajectoryBuilder(m_intakeSampleFour.end(), false)
                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(-135)))
                .build();

        m_park = m_robot.drivetrain.trajectoryBuilder(m_scoreSampleFour.end(), true)
                .splineTo(new Vector2d(-24, -12), Math.toRadians(0))
                .build();
    }

    @Override
    public SequentialCommandGroup buildTasks() {
        SequentialCommandGroup completeTasks = new SequentialCommandGroup();
        completeTasks.addCommands(
                new InstantCommand(()-> GlobalVariables.bucketAuto = true)
                ,preload()
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
                )
                ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
                ,new ParallelCommandGroup(
                    new CMD_BucketLiftReset(m_robot.m_bucketLift)
                    ,new InstantCommand(()-> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
                    ,new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
                    ,new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(67.5))
                )
                ,new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
                ,new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_intakeSampleTwo)
                ,new WaitCommand(100)
                ,new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                ,new WaitCommand(500)
                ,new ParallelCommandGroup(
                    new SequentialCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                        ,new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                    )
                    ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_scoreSampleTwo)
                )
                ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    SequentialCommandGroup thirdSample(){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(50))
                ,new CMD_BucketLiftReset(m_robot.m_bucketLift)
                ,new InstantCommand(()-> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
                ,new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
            )
            ,new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
            ,new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
            ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_intakeSampleThree)
            ,new WaitCommand(100)
            ,new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
            ,new WaitCommand(500)
            ,new ParallelCommandGroup(
                new SequentialCommandGroup(
                    new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                    ,new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                )
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_scoreSampleThree)
            )
            ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    SequentialCommandGroup fourthSample(){
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new RR_TurnCommand(m_robot.drivetrain, Math.toRadians(30))
                        ,new CMD_BucketLiftReset(m_robot.m_bucketLift)
                        ,new InstantCommand(()-> m_robot.m_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
                        ,new CMD_ReadyToIntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake)
                )
                ,new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
                ,new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_intakeSampleFour)
                ,new WaitCommand(250)
                ,new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                ,new WaitCommand(750)
                ,new ParallelCommandGroup(
                    new SequentialCommandGroup(
                        new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
                        ,new CMD_ReadyToDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift)
                    )
                    ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_scoreSampleFour)
                )
                ,new CMD_DeployBucket(m_robot.GlobalVariables, m_robot.m_bucket)
                ,new ParallelCommandGroup(
                    new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_park)
                    ,new CMD_ResetToHome(m_robot.GlobalVariables, m_robot.m_bucketLift,
                        m_robot.m_intakeSubSlide ,m_robot.m_bucket, m_robot.m_subIntake)
                )
                ,new InstantCommand(()-> m_robot.m_climb.raiseFlag())
        );
    }
}
