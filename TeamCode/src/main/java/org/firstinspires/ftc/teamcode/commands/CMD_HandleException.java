package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;

public class CMD_HandleException extends CommandBase {
    RobotContainer m_robot;
    public CMD_HandleException(RobotContainer p_robot){
        m_robot = p_robot;
    }

    @Override
    public void initialize(){
        if(
            m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.HOME)
            || m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.READY_TO_INTAKE)
            || m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.INTAKE)
            || m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.STOW)
        ){
        }else{
            //unallowable state, exit
            return;
        }

        if(m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.HOME)){
            m_robot.schedule(
                new CMD_ReadyToDeploy(m_robot.GlobalVariables, m_robot.m_bucketLift)
            );
        }

        if(m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.STOW)){
            m_robot.schedule(new CMD_IntakeSub(m_robot.GlobalVariables, m_robot.m_intakeSubSlide, m_robot.m_subIntake, m_robot.m_bucket));
        }

        if(m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.READY_TO_INTAKE)){
            m_robot.schedule(new CMD_ResetToHome(m_robot.GlobalVariables, m_robot.m_bucketLift,
                    m_robot.m_intakeSubSlide, m_robot.m_bucket, m_robot.m_subIntake));
        }

        if(m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.INTAKE)){
            m_robot.schedule(new CMD_ReadyToIntake(m_robot.GlobalVariables, m_robot.m_intakeSubSlide,
                m_robot.m_subIntake, m_robot.m_bucketLift, m_robot.m_bucket));
        }
    }
}
