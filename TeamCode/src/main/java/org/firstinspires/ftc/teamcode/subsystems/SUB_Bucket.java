package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ftclib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.Constants.BucketConstants;

public class SUB_Bucket extends SubsystemBase {
    Servo m_bucketServo;
    boolean m_bucketHome = true;
    public SUB_Bucket(OpMode p_opMode){
        m_bucketServo = p_opMode.hardwareMap.get(Servo.class, "BucketServo");
        m_bucketServo.setPosition(BucketConstants.kBucketHome);
    }

    public void setBucketServoPosition(double p_pos) {
        m_bucketServo.setPosition(p_pos);
        if (p_pos == BucketConstants.kBucketHome) {
            m_bucketHome = true;
        } else {
            m_bucketHome = false;
        }
    }

    public boolean isBucketHome(){
        return m_bucketHome;
    }
}
