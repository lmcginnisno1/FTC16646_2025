package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.Constants.SubIntakeConstants;

public class SUB_SubmersibleIntake extends SubsystemBase {
    CRServo m_subIntakeServo;
    Servo m_subBucketServo;
    boolean m_subBucketIntake = true;
    OpMode m_opMode;
    public SUB_SubmersibleIntake(OpMode p_opMode){
        m_opMode = p_opMode;
        m_subIntakeServo = m_opMode.hardwareMap.get(CRServo.class, "SubIntakeServo");
        m_subIntakeServo.setPower(SubIntakeConstants.kIntakeOff);
        m_subBucketServo = m_opMode.hardwareMap.get(Servo.class, "SubBucketServo");
        m_subBucketServo.setPosition(SubIntakeConstants.kBucketHome);
    }

    public void setBucketPosition(double p_pos) {
        m_subBucketServo.setPosition(p_pos);
        if (p_pos == SubIntakeConstants.kBucketReadyToIntake) {m_subBucketIntake = false;}
        else if(p_pos == SubIntakeConstants.kBucketIntake){m_subBucketIntake = true;}
    }

    public boolean isBucketIntake(){
        return m_subBucketIntake;
    }


    public void setIntakeSpeed(double p_speed){
        m_subIntakeServo.setPower(p_speed);
        if(p_speed != 0){
            GlobalVariables.m_intaking = true;
        }else{
            GlobalVariables.m_intaking = false;
        }
    }

    @Override
    public void periodic(){
//        m_opMode.telemetry.addData("intaking", GlobalVariables.m_intaking);
    }
}
