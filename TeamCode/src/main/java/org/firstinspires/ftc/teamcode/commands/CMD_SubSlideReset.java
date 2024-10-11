package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.*;

public class CMD_SubSlideReset extends CommandBase {
    private SUB_IntakeSubSlide m_subSlide;
    private boolean m_isFinished = false;
    private boolean m_isMoving = true;
    private int m_previousEncoderValue;

    private ElapsedTime m_runTime = new ElapsedTime();

    public CMD_SubSlideReset(SUB_IntakeSubSlide p_subSlide){
        m_subSlide = p_subSlide;
        addRequirements(m_subSlide);
    }

    @Override
    public void initialize() {
        m_isFinished = false;
        m_previousEncoderValue = m_subSlide.getCurrentPosition() + 10;
        m_subSlide.startReset();
        m_isMoving = true;
    }

    @Override
    public void execute(){
        if (m_isMoving) {
            if (m_previousEncoderValue <= m_subSlide.getCurrentPosition()) {
                m_isMoving = false;
                m_runTime.reset();
            }
            m_previousEncoderValue = m_subSlide.getCurrentPosition();
        } else {
            // wait for the elevator is stabilize before finish
            if (m_runTime.milliseconds() > 200) m_isFinished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return m_isFinished;
    }

    @Override
    public void end(boolean interrupted){
        m_subSlide.resetEncoder();
    }
}