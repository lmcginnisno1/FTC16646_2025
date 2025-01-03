package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants.ClimbConstants;
import org.firstinspires.ftc.teamcode.ftclib.command.SubsystemBase;

public class SUB_Climb extends SubsystemBase {
    DcMotorEx m_climbMotor;
    Servo m_flagServo;
    OpMode m_opMode;
    public SUB_Climb(OpMode p_opMode){
        m_opMode = p_opMode;
        m_climbMotor = m_opMode.hardwareMap.get(DcMotorEx.class, "leftClimb");
        m_climbMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_climbMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        m_flagServo = m_opMode.hardwareMap.get(Servo.class, "flagServo");
        m_flagServo.setPosition(0);
    }

    public void climb(){
        m_climbMotor.setTargetPosition(ClimbConstants.kClimb);
        m_climbMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_climbMotor.setPower(1);
    }

    public void reset(){
        m_climbMotor.setTargetPosition(0);
        m_climbMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_climbMotor.setPower(1);
    }

    public void raiseFlag(){
        m_flagServo.setPosition(0.55);
    }

    @Override
    public void periodic(){
//        m_opMode.telemetry.addData("climb pos", m_climbMotor.getCurrentPosition());
    }
}
