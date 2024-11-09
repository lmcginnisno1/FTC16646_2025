package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.subsystems.SUB_SubmersibleIntake;

public class CMD_SubIntakeDefault extends CommandBase {
    GamepadEx m_driverOp;
    SUB_SubmersibleIntake m_subIntake;
    GlobalVariables m_variables;
    boolean m_intaking = false;
    public CMD_SubIntakeDefault(GamepadEx p_driverOp, SUB_SubmersibleIntake p_subIntake, GlobalVariables p_variables){
        m_driverOp = p_driverOp;
        m_subIntake = p_subIntake;
        m_variables = p_variables;
        addRequirements(m_subIntake);
    }

    @Override
    public void execute(){
        if(m_intaking){
            if(m_driverOp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5){
                m_subIntake.setIntakeSpeed(-m_driverOp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
            }else if (m_variables.isRobotState(GlobalVariables.RobotState.READY_TO_INTAKE)){
                m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn);
            }else if(m_variables.isRobotState(GlobalVariables.RobotState.INTAKE)){
                m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeReverse);
            }else{
                m_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOff);
            }
        }
    }
}
