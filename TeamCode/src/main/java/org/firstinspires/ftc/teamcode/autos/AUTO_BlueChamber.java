package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.profile.VelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.Robot_Auto;
import org.firstinspires.ftc.teamcode.commands.CMD_DeployChamber;
import org.firstinspires.ftc.teamcode.commands.CMD_IntakeWall;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToDeployChamber;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToIntakeWall;
import org.firstinspires.ftc.teamcode.commands.CMD_ResetToHome;
import org.firstinspires.ftc.teamcode.commands.RR_TrajectoryFollowerCommand;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelRaceGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;

@Autonomous(name = "Blue Chamber", group = "Auto Blue", preselectTeleOp = "Teleop Blue")
public class AUTO_BlueChamber extends Robot_Auto {
    Trajectory m_placeChamberOne;
    Trajectory m_releaseChamber;
    Trajectory m_lineUpGroundSampleOne;
    Trajectory m_strafeFrontSampleOne;
    Trajectory m_pushSampleOne;
    Trajectory m_backUpSampleOne;
    Trajectory m_lineUpSampleTwo;
    Trajectory m_pushSampleTwo;
    Trajectory m_backUpSampleTwo;
    Trajectory m_park;

    @Override
    public void prebuildTasks() {
        setStartingPose(new Pose2d(0, -63, Math.toRadians(90)));
        m_placeChamberOne = m_robot.drivetrain.trajectoryBuilder(getStartingPose(), false)
                .lineToLinearHeading(new Pose2d(0, -33, Math.toRadians(90)))
                .build();
        m_releaseChamber = m_robot.drivetrain.trajectoryBuilder(m_placeChamberOne.end(), true)
                .back(6)
                .build();
        m_lineUpGroundSampleOne = m_robot.drivetrain.trajectoryBuilder(m_releaseChamber.end(), true)
                .splineTo(new Vector2d(24, -48), Math.toRadians(0))
                .splineTo(new Vector2d(32, -12), Math.toRadians(90))
                .build();
        m_strafeFrontSampleOne = m_robot.drivetrain.trajectoryBuilder(m_lineUpGroundSampleOne.end(), false)
                .strafeLeft(14)
                .build();
        m_pushSampleOne = m_robot.drivetrain.trajectoryBuilder(m_strafeFrontSampleOne.end(), false)
                .lineToLinearHeading(new Pose2d(50, -55, Math.toRadians(-90)))
                .build();
        m_backUpSampleOne = m_robot.drivetrain.trajectoryBuilder(m_pushSampleOne.end(), true)
                .lineToLinearHeading(new Pose2d(45, -12, Math.toRadians(-90)))
                .build();
        m_lineUpSampleTwo = m_robot.drivetrain.trajectoryBuilder(m_backUpSampleOne.end(), false)
                .strafeLeft(12)
                .build();
        m_pushSampleTwo = m_robot.drivetrain.trajectoryBuilder(m_lineUpSampleTwo.end(), false)
                .lineToLinearHeading(new Pose2d(60, -58, Math.toRadians(-90)))
                .build();
        m_backUpSampleTwo = m_robot.drivetrain.trajectoryBuilder(m_pushSampleTwo.end(), true)
                .back(24)
                .build();
        m_park = m_robot.drivetrain.trajectoryBuilder(m_backUpSampleTwo.end(), false)
                .forward(24)
                .build();
    }

    @Override
    public SequentialCommandGroup buildTasks() {
        SequentialCommandGroup completeTasks = new SequentialCommandGroup();
        completeTasks.addCommands(
                new InstantCommand(()->GlobalVariables.bucketAuto = false)
                ,placeChamberOne()
                ,lineUpGroundSampleOne()
                ,lineUpGroundSampleTwo()
        );
        m_robot.schedule(completeTasks);
        return completeTasks;
    }

    SequentialCommandGroup placeChamberOne(){
        SequentialCommandGroup cmds = new SequentialCommandGroup();
        cmds.addCommands(
                new ParallelCommandGroup(
                        new CMD_ReadyToDeployChamber(m_robot.GlobalVariables, m_robot.m_bucketLift)
                        ,new SequentialCommandGroup(
                        new WaitCommand(500)
                        ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_placeChamberOne)
                )
                )
                ,new WaitCommand(500)
                ,new InstantCommand(()-> m_robot.m_bucketLift.setTargetPosition(Constants.BucketLift.kLiftDeployHighChamber))
                ,new WaitCommand(500)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_releaseChamber)
                ,new CMD_DeployChamber(m_robot.GlobalVariables, m_robot.m_bucketLift)
        );
        return cmds;
    }

    SequentialCommandGroup lineUpGroundSampleOne(){
        SequentialCommandGroup cmds = new SequentialCommandGroup();
        cmds.addCommands(
                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_lineUpGroundSampleOne)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_strafeFrontSampleOne)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_pushSampleOne)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_backUpSampleOne)
        );
        return cmds;
    }

    SequentialCommandGroup lineUpGroundSampleTwo(){
        SequentialCommandGroup cmds = new SequentialCommandGroup();
        cmds.addCommands(
                new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_lineUpSampleTwo)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_pushSampleTwo)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_backUpSampleTwo)
                ,new WaitCommand(5000)
                ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_park)
        );
        return cmds;
    }
}
