package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.ftclib.command.SubsystemBase;

public class SUB_BucketLift extends SubsystemBase {
    MotorGroup m_bucketLift;
    Motor m_leftBucketLift;
    Motor m_rightBucketLift;
    OpMode m_opMode;
    int m_targetPos = 0;
    public SUB_BucketLift(OpMode p_opMode){
        m_opMode = p_opMode;
        m_leftBucketLift = new Motor(m_opMode.hardwareMap, "leftBucketSlide", Motor.GoBILDA.RPM_435);
        m_rightBucketLift = new Motor(m_opMode.hardwareMap, "rightBucketSlide", Motor.GoBILDA.RPM_435);
        m_bucketLift = new MotorGroup(
            m_leftBucketLift
            ,m_rightBucketLift
        );
        m_bucketLift.setInverted(true);
        m_bucketLift.resetEncoder();
        m_bucketLift.setTargetPosition(0);
        m_bucketLift.setRunMode(Motor.RunMode.PositionControl);
        m_bucketLift.setPositionCoefficient(10);
        m_bucketLift.setFeedforwardCoefficients(0, 0.5, 0);
        m_bucketLift.set(1);
    }

    public void setTargetPosition(int p_pos){
        m_bucketLift.setTargetPosition(p_pos);
        m_targetPos = p_pos;
    }

    public boolean atPosition(){
        return m_bucketLift.atTargetPosition();
    }

    public int getCurrentPosition() {
        return m_leftBucketLift.getCurrentPosition();
    }

    public void startReset(){
    }

    public void resetEncoder(){
    }

    @Override
    public void periodic(){
        m_opMode.telemetry.addData("current pos", getCurrentPosition());
        m_opMode.telemetry.addData("atTarget", atPosition());
    }
}
