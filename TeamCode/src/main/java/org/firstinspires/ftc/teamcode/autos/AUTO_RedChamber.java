package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.Robot_Auto;
import org.firstinspires.ftc.teamcode.commands.CMD_DeployChamber;
import org.firstinspires.ftc.teamcode.commands.CMD_IntakeSub;
import org.firstinspires.ftc.teamcode.commands.CMD_IntakeWall;
import org.firstinspires.ftc.teamcode.commands.CMD_QuickDump;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToDeployChamber;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToIntakeWall;
import org.firstinspires.ftc.teamcode.commands.CMD_ResetToHome;
import org.firstinspires.ftc.teamcode.commands.CMD_StowSub;
import org.firstinspires.ftc.teamcode.commands.CMD_SubmersibleInPosition;
import org.firstinspires.ftc.teamcode.commands.RR_TrajectoryFollowerCommand;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;

@Autonomous(name = "Red Chamber", preselectTeleOp = "Teleop Red", group = "Auto Red")
public class AUTO_RedChamber extends Robot_Auto {
    Trajectory m_placeChamberOne;
    Trajectory m_releaseChamber;
    Trajectory m_lineUpGroundSampleOne;
    Trajectory m_intakeWallSpecimenOne;
    Trajectory m_lineUpChamber;
    Trajectory m_splineToChamber;
    Trajectory m_lineUpWallSpecimenTwo;
    Trajectory m_intakeWallSpecimenTwo;
    Trajectory m_splineToChamberTwo;
    Trajectory m_placeChamberTwo;
    Trajectory m_homeChamberTwo;

    @Override
    public void prebuildTasks() {
        setStartingPose(new Pose2d(5, -63, Math.toRadians(90)));
        m_placeChamberOne = m_robot.drivetrain.trajectoryBuilder(getStartingPose(), false)
                .lineToLinearHeading(new Pose2d(2, -32, Math.toRadians(90)))
                .build();

        m_releaseChamber = m_robot.drivetrain.trajectoryBuilder(m_placeChamberOne.end(), true)
                .back(6)
                .build();

        m_lineUpGroundSampleOne = m_robot.drivetrain.trajectoryBuilder(m_releaseChamber.end(), false)
                .lineToLinearHeading(new Pose2d(50, -46, Math.toRadians(-90)))
                .build();

        m_intakeWallSpecimenOne = m_robot.drivetrain.trajectoryBuilder(m_lineUpGroundSampleOne.end(), false)
                .lineToConstantHeading(new Vector2d(49, -64),
                    SampleMecanumDrive.getVelocityConstraint(40, DriveConstants.MAX_VEL, DriveConstants.TRACK_WIDTH)
                    ,SampleMecanumDrive.getAccelerationConstraint(40))
                .build();

        m_lineUpChamber = m_robot.drivetrain.trajectoryBuilder(m_intakeWallSpecimenOne.end(), true)
                .splineTo(new Vector2d(56, -48), Math.toRadians(45))
                .build();

        m_splineToChamber = m_robot.drivetrain.trajectoryBuilder(m_lineUpChamber.end(), false)
                .splineTo(new Vector2d(24, -48), Math.toRadians(180))
                .splineTo(new Vector2d(-2, -31), Math.toRadians(90))
                .build();

        m_lineUpWallSpecimenTwo = m_robot.drivetrain.trajectoryBuilder(m_splineToChamber.end(), false)
                .lineToLinearHeading(new Pose2d(36, -46, Math.toRadians(-90)))
                .build();

        m_intakeWallSpecimenTwo = m_robot.drivetrain.trajectoryBuilder(m_lineUpWallSpecimenTwo.end(), false)
                .lineToConstantHeading(new Vector2d(36, -64),
                        SampleMecanumDrive.getVelocityConstraint(40, DriveConstants.MAX_VEL, DriveConstants.TRACK_WIDTH)
                        ,SampleMecanumDrive.getAccelerationConstraint(40))
                .build();

        m_splineToChamberTwo = m_robot.drivetrain.trajectoryBuilder(m_lineUpWallSpecimenTwo.end(), false)
                .lineToLinearHeading(new Pose2d(5, -46, Math.toRadians(90)))
                .build();

        m_placeChamberTwo = m_robot.drivetrain.trajectoryBuilder(m_splineToChamberTwo.end(), false)
                .forward(15)
                .build();

        m_homeChamberTwo = m_robot.drivetrain.trajectoryBuilder(m_placeChamberTwo.end(), false)
                .lineToConstantHeading(new Vector2d(37.5, -55))
                .build();
    }

    @Override
    public SequentialCommandGroup buildTasks() {
        SequentialCommandGroup completeTasks = new SequentialCommandGroup();
        completeTasks.addCommands(
            new InstantCommand(()-> GlobalVariables.bucketAuto = false)
            ,preload()
            ,groundSampleOne()
            ,intakeWallSpecimenOne()
            ,intakeWallSpecimenTwo()
        );
        m_robot.schedule(completeTasks);
        return completeTasks;
    }

    SequentialCommandGroup preload(){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                new CMD_ReadyToDeployChamber(m_robot.GlobalVariables, m_robot.m_bucketLift)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_placeChamberOne)
            )
            ,new WaitCommand(250)
            ,new InstantCommand(()-> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftDeployHighChamber))
            ,new WaitCommand(250)
            ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_releaseChamber)
        );
    }

    SequentialCommandGroup groundSampleOne(){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                new SequentialCommandGroup(
                    new CMD_ResetToHome(m_robot.GlobalVariables, m_robot.m_bucketLift, m_robot.m_intakeSubSlide,
                            m_robot.m_bucket, m_robot.m_subIntake)
                    ,new InstantCommand(()-> m_robot.m_intakeSubSlide.setTargetPosition(150))
                    ,new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketReadyToIntake))
                )
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_lineUpGroundSampleOne)
            )
            ,new InstantCommand(()-> m_robot.m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
            ,new InstantCommand(()-> m_robot.m_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketIntake))
            ,new WaitCommand(250)
            ,new InstantCommand(()-> m_robot.m_intakeSubSlide.setTargetPosition(Constants.SubmersibleSlide.kSlideMaxExtend))
            ,new CMD_SubmersibleInPosition(m_robot.m_intakeSubSlide)
//            ,new InstantCommand(()-> m_robot.m_intakeSubSlide.setTargetPosition(550))
//            ,new CMD_SubmersibleInPosition(m_robot.m_intakeSubSlide)
            ,new WaitCommand(500)
            ,new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
            ,new WaitCommand(500)
            ,new CMD_StowSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket)
            ,new CMD_QuickDump(m_robot.GlobalVariables, m_robot.m_bucket)
        );
    }

    SequentialCommandGroup intakeWallSpecimenOne(){
        return new SequentialCommandGroup(
           new WaitCommand(750)
           ,new ParallelCommandGroup(
               new CMD_ReadyToIntakeWall(m_robot.GlobalVariables, m_robot.m_bucketLift, m_robot.m_bucket)
               ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_intakeWallSpecimenOne)
           )
           ,new WaitCommand(500)
           ,new CMD_IntakeWall(m_robot.GlobalVariables, m_robot.m_bucketLift)
           ,new WaitCommand(100)
           ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_lineUpChamber)
           ,new ParallelCommandGroup(
               new CMD_ReadyToDeployChamber(m_robot.GlobalVariables, m_robot.m_bucketLift)
               ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_splineToChamber)
           )
           ,new WaitCommand(500)
           ,new CMD_DeployChamber(m_robot.GlobalVariables, m_robot.m_bucketLift)
        );
    }

    SequentialCommandGroup intakeWallSpecimenTwo(){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                new CMD_ReadyToIntakeWall(m_robot.GlobalVariables, m_robot.m_bucketLift, m_robot.m_bucket)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_lineUpWallSpecimenTwo)
            )
            ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_intakeWallSpecimenTwo)
            ,new WaitCommand(100)
            ,new ParallelCommandGroup(
                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_splineToChamberTwo)
                ,new CMD_ReadyToDeployChamber(m_robot.GlobalVariables, m_robot.m_bucketLift)
            )
            ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_placeChamberTwo)
            ,new CMD_DeployChamber(m_robot.GlobalVariables, m_robot.m_bucketLift)
            ,new ParallelCommandGroup(
                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_homeChamberTwo)
                ,new CMD_ResetToHome(m_robot.GlobalVariables, m_robot.m_bucketLift,
                m_robot.m_intakeSubSlide, m_robot.m_bucket, m_robot.m_subIntake)
            )
        );
    }
}