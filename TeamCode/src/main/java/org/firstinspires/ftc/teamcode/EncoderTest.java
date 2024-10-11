package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.ftclib.util.Timing.Timer;

import java.util.concurrent.TimeUnit;

@Disabled
@Autonomous(name="Encoder Test Auto")
public class EncoderTest extends OpMode {
     DcMotor leftFront, rightFront, leftRear, rightRear;
     Timer Timer = new Timer(5000, TimeUnit.MILLISECONDS);
     @Override
     public void init() {
          leftFront = this.hardwareMap.get(DcMotorEx.class, "leftFront");
          rightFront = this.hardwareMap.get(DcMotorEx.class, "rightFront");
          leftRear = this.hardwareMap.get(DcMotorEx.class, "leftRear");
          rightRear = this.hardwareMap.get(DcMotorEx.class, "rightRear");
          leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
          rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
          leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
          rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
          leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
          rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
          leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
          rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
          leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
          rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
          leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
          rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
          leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
          leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
          leftFront.setPower(.5);
          leftRear.setPower(.5);
          rightFront.setPower(.5);
          rightRear.setPower(.5);
          Timer.start();
     }

     @Override
     public void loop() {
        if(!Timer.done()){
           this.telemetry.addData("LF", leftFront.getCurrentPosition());
           this.telemetry.addData("RF", rightFront.getCurrentPosition());
           this.telemetry.addData("LR", leftRear.getCurrentPosition());
           this.telemetry.addData("RR", rightRear.getCurrentPosition());
        }else{
             leftFront.setPower(0);
             leftRear.setPower(0);
             rightFront.setPower(0);
             rightRear.setPower(0);
        }
     }
}
