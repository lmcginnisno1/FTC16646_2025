package org.firstinspires.ftc.teamcode.ftclib.command;

import org.firstinspires.ftc.teamcode.ftclib.command.CommandScheduler;
import org.firstinspires.ftc.teamcode.ftclib.command.Robot;

import org.firstinspires.ftc.teamcode.ftclib.command.Subsystem;
import org.firstinspires.ftc.teamcode.ftclib.command.Command;
import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadEx;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;



/**
 * As opposed to the general WPILib-style Robot paradigm, FTCLib also offers a command opmode
 * for individual opmodes.
 *
 * @author Jackson
 */
public abstract class CommandOpMode extends LinearOpMode {

    /**
     * Cancels all previous commands
     */
    public void reset() {
        CommandScheduler.getInstance().reset();
    }

    /**
     * Runs the {@link CommandScheduler} instance
     */
    public void run() {
        CommandScheduler.getInstance().run();
    }

    /**
     * Schedules {@link com.arcrobotics.ftclib.command.Command} objects to the scheduler
     */
    public void schedule(Command... commands) {
        CommandScheduler.getInstance().schedule(commands);
    }

    /**
     * Registers {@link com.arcrobotics.ftclib.command.Subsystem} objects to the scheduler
     */
    public void register(Subsystem... subsystems) {
        CommandScheduler.getInstance().registerSubsystem(subsystems);
    }
    
    public void cancelAll(){
        CommandScheduler.getInstance().cancelAll();
    }
    
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.clearAll();
        initialize();

        // waitForStart();
        while (!opModeIsActive() && !isStopRequested()) {
            waitForStartPeriodic();
            telemetry.update();
        }
        
        afterWaitForStart();

        // run the scheduler
        while (!isStopRequested() && opModeIsActive()) {
            periodic();
            run();
            telemetry.update();
        }

        endOfOpMode();

        reset();
    }

    public abstract void initialize();

    public void waitForStartPeriodic() {
        
    }
    
    public void afterWaitForStart() {
        
    }

    public void endOfOpMode() {
        
    }
    
    public void periodic() {
        
    }
    
    public static void disable() {
        org.firstinspires.ftc.teamcode.ftclib.command.Robot.disable();
    }

    public static void enable() {
        org.firstinspires.ftc.teamcode.ftclib.command.Robot.enable();
    }


}
