package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;

public class CMD_HandleReadyToDeploy extends CommandBase {
    RobotContainer m_robot;
    public CMD_HandleReadyToDeploy(RobotContainer p_robot){
        m_robot = p_robot;
    }

    @Override
    public void initialize(){
        if(m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.STOW)
            || m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_HOME)
            || m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.READY_TO_DEPLOY)
            || m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.HOME)
        ){}else{return;}

        if(m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.STOW) || m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.HOME)){
            m_robot.schedule(new CMD_ReadyToDeploy(m_robot.GlobalVariables, m_robot.m_bucketLift));
        }

        if(m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.READY_TO_DEPLOY)){
            m_robot.schedule(new CMD_Deploy(m_robot.GlobalVariables, m_robot.m_bucketLift, m_robot.m_bucket));
        }

        if(m_robot.GlobalVariables.isRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_HOME)){
            m_robot.schedule(new CMD_HomeBucket(m_robot.GlobalVariables, m_robot.m_bucket, m_robot.m_bucketLift));
        }
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
