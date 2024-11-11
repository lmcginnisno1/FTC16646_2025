package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.ftclib.command.SubsystemBase;

public class SUB_BucketLift extends SubsystemBase {
    DcMotorEx m_leftBucketLiftMotor;
    DcMotorEx m_rightBucketLiftMotor;
    MotorGroup m_bucketLift;
    OpMode m_opMode;
    public SUB_BucketLift(OpMode p_opMode){
        m_opMode = p_opMode;
        m_leftBucketLiftMotor = m_opMode.hardwareMap.get(DcMotorEx.class, "leftBucketSlide");
        m_leftBucketLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_leftBucketLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m_leftBucketLiftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        m_leftBucketLiftMotor.setTargetPosition(0);
        m_leftBucketLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_leftBucketLiftMotor.setPower(0);

        m_rightBucketLiftMotor = m_opMode.hardwareMap.get(DcMotorEx.class, "rightBucketSlide");
        m_rightBucketLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_rightBucketLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m_rightBucketLiftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        m_rightBucketLiftMotor.setTargetPosition(0);
        m_rightBucketLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_rightBucketLiftMotor.setPower(0);

        m_rightBucketLiftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, new PIDFCoefficients(15, 0, 0,0));
        m_leftBucketLiftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, new PIDFCoefficients(15, 0, 0, 0));
    }

    public void setTargetPosition(int p_pos){
        m_leftBucketLiftMotor.setTargetPosition(p_pos);
        m_rightBucketLiftMotor.setTargetPosition(p_pos);
        m_leftBucketLiftMotor.setPower(1);
        m_rightBucketLiftMotor.setPower(1);
    }

    public int getCurrentPosition(){
        return m_leftBucketLiftMotor.getCurrentPosition();
    }

    public int getTargetPosition(){
        return m_leftBucketLiftMotor.getTargetPosition();
    }

    public void startReset(){
        m_leftBucketLiftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m_leftBucketLiftMotor.setPower(-0.5);
        m_rightBucketLiftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m_rightBucketLiftMotor.setPower(-0.5);
    }

    public void resetEncoder(){
        m_leftBucketLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_leftBucketLiftMotor.setTargetPosition(m_leftBucketLiftMotor.getCurrentPosition());
        m_leftBucketLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_leftBucketLiftMotor.setPower(0);

        m_rightBucketLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_rightBucketLiftMotor.setTargetPosition(m_rightBucketLiftMotor.getCurrentPosition());
        m_rightBucketLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_rightBucketLiftMotor.setPower(0);
    }

    @Override
    public void periodic(){
        m_opMode.telemetry.addData("bucket lift targ. pos.", getTargetPosition());
        m_opMode.telemetry.addData("bucket lift cur. pos.", getCurrentPosition());

        m_opMode.telemetry.addData("right bucket lift targ. pos.", m_rightBucketLiftMotor.getTargetPosition());
        m_opMode.telemetry.addData("right bucket lift cur. pos.", m_rightBucketLiftMotor.getCurrentPosition());

        m_opMode.telemetry.addData("P", m_rightBucketLiftMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION).p);
        m_opMode.telemetry.addData("I", m_rightBucketLiftMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION).i);
        m_opMode.telemetry.addData("D", m_rightBucketLiftMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION).d);
        m_opMode.telemetry.addData("F", m_rightBucketLiftMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION).f);
    }
}