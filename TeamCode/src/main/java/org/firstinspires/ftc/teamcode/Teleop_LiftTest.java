package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

//@Disabled
@TeleOp(name = "LiftTest", group = "Test")
public class Teleop_LiftTest extends LinearOpMode {

    DcMotorEx m_leftLiftMotor;
    DcMotorEx m_rightLiftMotor;
    ElapsedTime m_runTime;

    @Override
    public void runOpMode() throws InterruptedException {
        m_leftLiftMotor = hardwareMap.get(DcMotorEx.class, "leftBucketSlide");
        m_rightLiftMotor = hardwareMap.get(DcMotorEx.class, "rightBucketSlide");
        m_leftLiftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        m_rightLiftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        m_leftLiftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m_rightLiftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.update();
        }

        m_runTime.reset();
        while (!isStopRequested() && opModeIsActive()) {
            m_leftLiftMotor.setPower(gamepad1.left_stick_y);
            m_rightLiftMotor.setPower(gamepad1.left_stick_y);
        }
    }
}
