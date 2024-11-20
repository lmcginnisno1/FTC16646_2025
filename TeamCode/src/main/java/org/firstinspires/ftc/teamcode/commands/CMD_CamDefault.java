package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Vision;
import org.firstinspires.ftc.teamcode.visionprocessor.VProcessorDetectBlock;

public class CMD_CamDefault extends CommandBase {
    SUB_Vision m_vision;
    GlobalVariables m_variables;
    VProcessorDetectBlock m_pipeline;
    VProcessorDetectBlock.DetectionResult m_detectionResult;
    OpMode m_opMode;
    public CMD_CamDefault(GlobalVariables p_variables, SUB_Vision p_vision,
                          VProcessorDetectBlock p_pipeline, MecanumDriveSubsystem m_drivetrain, OpMode p_opMode){
        m_vision = p_vision;
        m_variables = p_variables;
        m_pipeline = p_pipeline;
        m_opMode = p_opMode;
        addRequirements(m_vision);
    }

    @Override
    public void initialize(){
        m_vision.setProcessorEnabled(m_pipeline);
        m_vision.resumeStreaming();
    }

    @Override
    public void execute(){
        if(m_variables.isRobotState(GlobalVariables.RobotState.READY_TO_INTAKE)){
            m_detectionResult = m_pipeline.getLastDetectionResult();
            if(m_detectionResult != null){
                m_opMode.telemetry.addData("offset X", m_detectionResult.adjustedOffset.x);
                m_opMode.telemetry.addData("offset Y", m_detectionResult.adjustedOffset.y);
                m_opMode.telemetry.addData("offsetarea", m_detectionResult.area);
            }
        }
    }

    @Override
    public void end(boolean isInterrupted){
        m_vision.stopStreaming();
        m_vision.setProcessorDisabled(m_pipeline);
    }
}
