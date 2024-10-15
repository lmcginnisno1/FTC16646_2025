package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.ftclib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.ftclib.command.TrapezoidProfileSubsystem;
import org.firstinspires.ftc.teamcode.ftclib.first.math.trajectory.TrapezoidProfile;

public class SUB_BucketLiftTrapezoid extends TrapezoidProfileSubsystem {
    DcMotorEx m_leftBucketLiftMotor;
    DcMotorEx m_rightBucketLiftMotor;
    MotorGroup m_bucketLift;
    OpMode m_opMode;
    public SUB_BucketLiftTrapezoid(OpMode p_opMode){
        super(
            new TrapezoidProfile.Constraints(50, 50)
            ,0
            ,0.02
        );
        m_opMode = p_opMode;
        m_leftBucketLiftMotor = m_opMode.hardwareMap.get(DcMotorEx.class, "leftBucketSlide");
        m_rightBucketLiftMotor = m_opMode.hardwareMap.get(DcMotorEx.class, "rightBucketSlide");
        m_leftBucketLiftMotor.setVelocityPIDFCoefficients(10, 0, 0, 12);
        m_rightBucketLiftMotor.setVelocityPIDFCoefficients(10, 0, 0, 12);
        m_leftBucketLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m_rightBucketLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m_leftBucketLiftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(10,0,0,0));
        m_rightBucketLiftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(10, 0, 0, 0));
        m_leftBucketLiftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, new PIDFCoefficients(5,0,0,0));
        m_rightBucketLiftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, new PIDFCoefficients(5, 0, 0, 0));
        m_leftBucketLiftMotor.setTargetPosition(m_leftBucketLiftMotor.getCurrentPosition());
        m_rightBucketLiftMotor.setTargetPosition(m_rightBucketLiftMotor.getCurrentPosition());
        m_leftBucketLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_rightBucketLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m_leftBucketLiftMotor.setPower(1);
        m_rightBucketLiftMotor.setPower(1);
        enable();
    }

    @Override
    public void periodic(){
        super.periodic();
    }

    @Override
    protected void useState(TrapezoidProfile.State state) {
        m_leftBucketLiftMotor.setTargetPosition((int) state.position);
        m_rightBucketLiftMotor.setTargetPosition((int) state.position);
    }
}