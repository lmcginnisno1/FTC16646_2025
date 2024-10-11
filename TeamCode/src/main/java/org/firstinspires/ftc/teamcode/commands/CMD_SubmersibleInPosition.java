package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.SUB_IntakeSubSlide;
import org.firstinspires.ftc.teamcode.Constants.SubmersibleSlide;

public class CMD_SubmersibleInPosition extends CommandBase {
    SUB_IntakeSubSlide m_SubmersibleSlide;
    public CMD_SubmersibleInPosition(SUB_IntakeSubSlide p_SubmersibleSlide){
        m_SubmersibleSlide = p_SubmersibleSlide;
    }

    @Override
    public boolean isFinished(){
        return Math.abs(m_SubmersibleSlide.getCurrentPosition() -
                m_SubmersibleSlide.getTargetPosition()) <= SubmersibleSlide.kSlideTolerance;
    }
}
