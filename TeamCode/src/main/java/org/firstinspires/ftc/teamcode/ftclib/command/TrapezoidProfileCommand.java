package org.firstinspires.ftc.teamcode.ftclib.command;
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import static org.firstinspires.ftc.teamcode.ftclib.first.math.trajectory.TrapezoidProfile.State;
import static org.firstinspires.ftc.teamcode.ftclib.util.ErrorMessages.requireNonNullParam;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ftclib.first.math.trajectory.TrapezoidProfile;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A command that runs a {@link TrapezoidProfile}. Useful for smoothly controlling mechanism motion.
 *
 * <p>This class is provided by the NewCommands VendorDep
 */
public class TrapezoidProfileCommand extends CommandBase {
    private final TrapezoidProfile m_profile;
    private final Consumer<State> m_output;
    private final Supplier<State> m_goal;
    private final Supplier<State> m_currentState;
    private final ElapsedTime m_timer = new ElapsedTime();

    /**
     * Creates a new TrapezoidProfileCommand that will execute the given {@link TrapezoidProfile}.
     * Output will be piped to the provided consumer function.
     *
     * @param profile The motion profile to execute.
     * @param output The consumer for the profile output.
     * @param goal The supplier for the desired state
     * @param currentState The current state
     * @param requirements The subsystems required by this command.
     */
    public TrapezoidProfileCommand(
            TrapezoidProfile profile,
            Consumer<State> output,
            Supplier<State> goal,
            Supplier<State> currentState,
            Subsystem... requirements) {
        m_profile = requireNonNullParam(profile, "profile", "TrapezoidProfileCommand");
        m_output = requireNonNullParam(output, "output", "TrapezoidProfileCommand");
        m_goal = goal;
        m_currentState = currentState;
        addRequirements(requirements);
    }

    @Override
    public void initialize() {
        m_timer.reset();
    }

    @Override
    @SuppressWarnings("removal")
    public void execute() {
        m_output.accept(m_profile.calculate(m_timer.time(), m_goal.get(), m_currentState.get()));
    }

    @Override
    public void end(boolean interrupted) {
//        m_timer.stop();
    }

    @Override
    public boolean isFinished() {
        return m_timer.time() >= m_profile.totalTime();
    }
}