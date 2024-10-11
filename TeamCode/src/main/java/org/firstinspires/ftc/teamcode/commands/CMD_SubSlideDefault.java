package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.subsystems.SUB_IntakeSubSlide;

public class CMD_SubSlideDefault extends CommandBase {
    GamepadEx m_driverOp;
    GlobalVariables m_variables;
    SUB_IntakeSubSlide m_subSlide;
    public CMD_SubSlideDefault(GamepadEx p_driverOp, GlobalVariables p_variables, SUB_IntakeSubSlide p_subSlide){
        m_driverOp = p_driverOp;
        m_variables = p_variables;
        m_subSlide = p_subSlide;
        addRequirements(m_subSlide);
    }

    @Override
    public void execute(){

        if (m_variables.isRobotState(GlobalVariables.RobotState.READY_TO_INTAKE) && (m_subSlide.getTargetPosition() <
        Constants.SubmersibleSlide.kSlideMaxExtend) && (m_subSlide.getTargetPosition() < Constants.SubmersibleSlide.kSlideHome)){
            if(m_driverOp.gamepad.dpad_left){
                m_subSlide.setTargetPosition(m_subSlide.getTargetPosition()+1);
            }else if(m_driverOp.gamepad.dpad_right){
                m_subSlide.setTargetPosition(m_subSlide.getTargetPosition()-1);
            }
        }
    }
}
