package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.ftclib.command.SubsystemBase;

public class SUB_IntakeSubSlide extends SubsystemBase {
    DcMotorEx m_intakeSubSlide;
    OpMode m_opMode;
    public SUB_IntakeSubSlide(OpMode p_opMode){
        m_opMode = p_opMode;
        m_intakeSubSlide = m_opMode.hardwareMap.get(DcMotorEx.class, "submersibleSlide");
        m_intakeSubSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_intakeSubSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m_intakeSubSlide.setTargetPosition(0);
        m_intakeSubSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_intakeSubSlide.setPower(1);
    }

    public void setTargetPosition(int p_pos){
        m_intakeSubSlide.setTargetPosition(p_pos);
    }

    public int getTargetPosition(){
        return m_intakeSubSlide.getTargetPosition();
    }

    public int getCurrentPosition(){
        return m_intakeSubSlide.getCurrentPosition();
    }

    public void extend(){
        if(getTargetPosition() < Constants.SubmersibleSlide.kSlideMaxExtend){
            m_intakeSubSlide.setTargetPosition(Constants.SubmersibleSlide.kSlideMaxExtend);
        }
    }
    public void retract(){
        if(getTargetPosition() > Constants.SubmersibleSlide.kSlideHome){
            m_intakeSubSlide.setTargetPosition(1000);
        }
    }

    public void startReset(){
        m_intakeSubSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m_intakeSubSlide.setPower(-.5);
    }

    public void resetEncoder(){
        m_intakeSubSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_intakeSubSlide.setTargetPosition(5);
        m_intakeSubSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_intakeSubSlide.setPower(1);
    }

    @Override
    public void periodic(){
        m_opMode.telemetry.addData("current pos", getCurrentPosition());
        m_opMode.telemetry.addData("target pos", getTargetPosition());
    }
}
