package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@Config
@TeleOp(name="Encoder Test", group = "EncoderTest")
public class EnocderTest extends LinearOpMode {
    private static ElapsedTime m_runTime = new ElapsedTime();
    int m_motorOneCount = 0;
    int m_motorTwoCount = 0;
    int m_motorThreeCount = 0;
    int m_motorFourCount = 0;
    public static double m_motorSpeed = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx m_motorOne = hardwareMap.get(DcMotorEx.class, "MotorOne");
        DcMotorEx m_motorTwo = hardwareMap.get(DcMotorEx.class, "MotorTwo");
        DcMotorEx m_motorThree = hardwareMap.get(DcMotorEx.class, "MotorThree");
        DcMotorEx m_motorFour = hardwareMap.get(DcMotorEx.class, "MotorFour");
        m_motorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_motorTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_motorThree.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_motorFour.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_motorOne.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m_motorTwo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m_motorThree.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m_motorFour.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("speed", m_motorSpeed);
            telemetry.addData("MotorOne", m_motorOneCount);
            telemetry.addData("MotorTwo", m_motorTwoCount);
            telemetry.addData("MotorThree", m_motorThreeCount);
            telemetry.addData("MotorFour", m_motorFourCount);
            telemetry.update();
        }
        m_motorOne.setPower(m_motorSpeed);
        m_motorTwo.setPower(m_motorSpeed);
        m_motorThree.setPower(m_motorSpeed);
        m_motorFour.setPower(m_motorSpeed);
        m_runTime.reset();
        while (!isStopRequested() && opModeIsActive()) {
            if(m_runTime.time() < 30){
                m_motorOneCount = m_motorOne.getCurrentPosition();
                m_motorTwoCount = m_motorTwo.getCurrentPosition();
                m_motorThreeCount = m_motorThree.getCurrentPosition();
                m_motorFourCount = m_motorFour.getCurrentPosition();
            }else{
                m_motorOne.setPower(0);
                m_motorTwo.setPower(0);
                m_motorThree.setPower(0);
                m_motorFour.setPower(0);
            }
            telemetry.addData("MotorOne", m_motorOneCount);
            telemetry.addData("MotorTwo", m_motorTwoCount);
            telemetry.addData("MotorThree", m_motorThreeCount);
            telemetry.addData("MotorFour", m_motorFourCount);
            telemetry.update();
        }
    }
}
