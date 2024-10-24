package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.Robot_Auto;
import org.firstinspires.ftc.teamcode.commands.CMD_DeployChamber;
import org.firstinspires.ftc.teamcode.commands.CMD_IntakeWall;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToDeployChamber;
import org.firstinspires.ftc.teamcode.commands.CMD_ReadyToIntakeWall;
import org.firstinspires.ftc.teamcode.commands.RR_TrajectoryFollowerCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;

@Autonomous(name = "Auto Red Chamber", group = "Auto Red", preselectTeleOp = "Teleop Red")
public class AUTO_RedChamber extends Robot_Auto {
    Trajectory m_placeChamberOne;
    Trajectory m_releaseChamber;
    Trajectory m_lineUpGroundSampleOne;
    Trajectory m_strafeFrontSampleOne;
    Trajectory m_pushSampleOne;
    Trajectory m_backUpSampleOne;
    Trajectory m_intakeSpecimenOne;
    Trajectory m_lineUpChamber;
    Trajectory m_splineToChamber;

    @Override
    public void prebuildTasks() {
        setStartingPose(new Pose2d(0, -63, Math.toRadians(90)));
        m_placeChamberOne = m_robot.drivetrain.trajectoryBuilder(getStartingPose(), false)
                .lineToLinearHeading(new Pose2d(0, -33.25, Math.toRadians(90)))
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
                .lineToLinearHeading(new Pose2d(50, -60, Math.toRadians(-90)))
                .build();
        m_backUpSampleOne = m_robot.drivetrain.trajectoryBuilder(m_pushSampleOne.end(), true)
                .lineToLinearHeading(new Pose2d(48, -45, Math.toRadians(-90)))
                .build();
        m_intakeSpecimenOne = m_robot.drivetrain.trajectoryBuilder(m_backUpSampleOne.end(), false)
                .lineToLinearHeading(new Pose2d(48, -61, Math.toRadians(-90)))
                .build();
        m_lineUpChamber = m_robot.drivetrain.trajectoryBuilder(m_intakeSpecimenOne.end(), true)
                .splineTo(new Vector2d(56, -45), Math.toRadians(45))
                .build();
        m_splineToChamber = m_robot.drivetrain.trajectoryBuilder(m_lineUpChamber.end(), false)
                .splineTo(new Vector2d(24, -48), Math.toRadians(180))
                .splineTo(new Vector2d(-12, -33.75), Math.toRadians(90))
                .build();
    }

    @Override
    public SequentialCommandGroup buildTasks() {
        SequentialCommandGroup completeTasks = new SequentialCommandGroup();
        completeTasks.addCommands(
            placeChamberOne()
            ,lineUpGroundSampleOne()
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
            ,new CMD_ReadyToIntakeWall(m_robot.GlobalVariables, m_robot.m_bucketLift, m_robot.m_bucket)
            ,new WaitCommand(2000)
            ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_intakeSpecimenOne)
            ,new WaitCommand(750)
            ,new CMD_IntakeWall(m_robot.GlobalVariables, m_robot.m_bucketLift)
            ,new RR_TrajectoryFollowerCommand(m_robot.drivetrain, m_lineUpChamber)
        );
        return cmds;
    }
}
