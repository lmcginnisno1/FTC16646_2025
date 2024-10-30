package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;

public class CMD_HandleReadyToIntake extends CommandBase {
    private final RobotContainer m_robot;

    public CMD_HandleReadyToIntake(RobotContainer p_robot) {
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
            case HOME:
            case STOW:
                m_robot.schedule(new CMD_ReadyToIntake(
                        m_robot.GlobalVariables,
                        m_robot.m_intakeSubSlide,
                        m_robot.m_subIntake,
                        m_robot.m_bucketLift,
                        m_robot.m_bucket));
                break;
            case READY_TO_INTAKE:
                m_robot.schedule(new CMD_Intake(
                        m_robot.GlobalVariables,
                        m_robot.m_intakeSubSlide,
                        m_robot.m_subIntake,
                        m_robot.m_bucket,
                        m_robot.m_bucketLift));
                break;
            case INTAKE:
                m_robot.schedule(new CMD_Stow(
                        m_robot.GlobalVariables,
                        m_robot.m_intakeSubSlide,
                        m_robot.m_subIntake,
                        m_robot.m_bucket,
                        m_robot.m_bucketLift));
                break;
            default:
                // No action for other states
                break;
        }
    }

    private boolean isValidState(GlobalVariables.RobotState state) {
        return state == GlobalVariables.RobotState.HOME
                || state == GlobalVariables.RobotState.READY_TO_INTAKE
                || state == GlobalVariables.RobotState.INTAKE
                || state == GlobalVariables.RobotState.STOW;
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
