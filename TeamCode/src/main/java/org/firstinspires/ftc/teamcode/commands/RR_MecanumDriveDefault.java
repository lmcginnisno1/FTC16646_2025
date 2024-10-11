package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadKeys;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

/**
 * A command to drive the robot with joystick input.
 */
public class RR_MecanumDriveDefault extends CommandBase {

    MecanumDriveSubsystem m_drivetrain = null;
    private GamepadEx m_driverOP = null;
    private double m_driverOffsetAngle = 0;
    private double m_joystickMin = 0.02;
    public RR_MecanumDriveDefault(MecanumDriveSubsystem p_drive, GamepadEx driverOp,
                                  double driverOffsetAngle, double joystickMin) {
        m_drivetrain = p_drive;
        m_driverOP = driverOp; // gamepad of driver
        m_driverOffsetAngle = driverOffsetAngle;
        m_joystickMin = joystickMin;

        addRequirements(m_drivetrain);
    }

    @Override
    public void execute() {
        double leftY = -m_driverOP.getLeftY(); // speed
        double leftX = m_driverOP.getLeftX(); // strafe
        double rightX = m_driverOP.getRightX(); // turn
        leftY = valueSquared(handleDeadband(leftY, 0.05));
        leftX = valueSquared(handleDeadband(leftX, 0.05));
        rightX = valueCubed(handleDeadband(rightX, 0.05));

        // double heading = m_Odometry.getDegrees() + m_driverOffsetAngle;
        double heading = 0 + m_driverOffsetAngle;

        double speed = leftY, turn = rightX, strafe = leftX;
        // double slowFactor = 0.75;
        // double slowFactor = m_Drivetrain.m_baseRobot.m_globalVariables.getSpeedLimiter();

        // Makes all the left stick vectors have a magnitude of 1, rather than 0.7 in the corners.
        double factor = 0;
        double R = Math.sqrt(leftX * leftX + leftY * leftY);
        if (Math.abs(leftY) > Math.abs(leftX))
            factor = Math.abs(leftY);
        else
            factor = Math.abs(leftX);
        if (factor == 0)
            factor = 1;
        leftX = Range.clip(leftX * R / factor, -1, 1);
        leftY = Range.clip(leftY * R / factor, -1, 1);

        final double slowMax = 0.6;
        double slowMo = m_driverOP.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        if (slowMo > slowMax) {
            speed *= slowMax / slowMo;
            strafe *= slowMax * 1.5 / slowMo;
            turn *= slowMax / slowMo;
        }

        // m_Odometry.telemetry.addData("drive: ", "leftY: %.2f, speed: %.2f, leftX: %.2f, strafe: %.2f", leftY, speed, leftX, strafe);

        // positive strafe, move to the right
        // positive speed, move forward
        // positive turn, turn torward the right
        m_drivetrain.drive(speed, strafe, turn);
    }

    private double handleDeadband(double value, double deadband) {
        double newValue=0;

        if (Math.abs(value) <= Math.abs(deadband)) {
            // in the deadband so return zero
            newValue = 0;
        } else {
            // scale and translate the value [deadband] .. [max]
            newValue = ( Math.abs(value) - deadband )/ ( 1 - deadband);
            // apply the original sign
            newValue = newValue * (Math.abs(value) / value);
        }

        return newValue;
    }

    private double valueSquared(double value) {
        return (value * Math.abs(value));
    }

    private double valueCubed(double value) {
        return (value * value * value);
    }

}


