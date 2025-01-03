package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelCommandGroup;

public class CMD_HandleReadyToDeploy extends CommandBase {
    private final RobotContainer m_robot;

    public CMD_HandleReadyToDeploy(RobotContainer p_robot) {
        this.m_robot = p_robot;
    }

    @Override
    public void initialize() {
        GlobalVariables.RobotState currentState = m_robot.GlobalVariables.getRobotState();

        // Check if robot is in a valid state to proceed
        if (!isValidState(currentState)) {
            return;
        }

        // Handle different robot states
        switch (currentState) {
            case STOW:
            case HOME:
                m_robot.schedule(new CMD_ReadyToDeploy(m_robot.GlobalVariables, m_robot.m_bucketLift));
                break;
            case READY_TO_DEPLOY:
                m_robot.schedule(new CMD_Deploy(m_robot.GlobalVariables, m_robot.m_bucketLift, m_robot.m_bucket));
                break;
            case TRANSITIONING_TO_HOME:
                m_robot.schedule(new CMD_ResetToHome(m_robot.GlobalVariables, m_robot.m_bucketLift,
                        m_robot.m_intakeSubSlide, m_robot.m_bucket, m_robot.m_subIntake));
                break;
            case INTAKE:
                m_robot.schedule(new CMD_FastDeployBucket(m_robot.GlobalVariables, m_robot.m_bucketLift,
                    m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket));
                break;
            default:
                // No action for other states
                break;
        }
    }

    private boolean isValidState(GlobalVariables.RobotState state) {
        return state == GlobalVariables.RobotState.STOW
                || state == GlobalVariables.RobotState.TRANSITIONING_TO_HOME
                || state == GlobalVariables.RobotState.READY_TO_DEPLOY
                || state == GlobalVariables.RobotState.HOME
                || state == GlobalVariables.RobotState.INTAKE;
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}