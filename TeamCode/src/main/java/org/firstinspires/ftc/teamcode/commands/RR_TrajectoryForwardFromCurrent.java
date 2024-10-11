package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

public class RR_TrajectoryForwardFromCurrent extends CommandBase {
    MecanumDriveSubsystem m_drivetrain;
    double m_distance;
    boolean m_reversed;
    public RR_TrajectoryForwardFromCurrent(MecanumDriveSubsystem p_drivetrain, double p_distance,
                                           boolean p_reversed){
        m_drivetrain = p_drivetrain;
        m_distance = p_distance;
        m_reversed = p_reversed;
    }

    @Override
    public void initialize(){
        Trajectory m_driveForward = m_drivetrain.trajectoryBuilder(m_drivetrain.getPoseEstimate(), m_reversed)
                .forward(m_distance)
                .build();

        m_drivetrain.followTrajectory(m_driveForward);
    }

    @Override
    public void execute(){
        m_drivetrain.update();
    }

    @Override
    public boolean isFinished(){
        return Thread.currentThread().isInterrupted() || !m_drivetrain.isBusy();
    }
}