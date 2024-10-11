/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.firstinspires.ftc.teamcode.ftclib.command;

import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * A state machine representing a complete action to be performed by the robot.  Commands are
 * run by the {@link CommandScheduler}, and can be composed into CommandGroups to allow users to
 * build complicated multi-step actions without the need to roll the state machine logic themselves.
 *
 * <p>Commands are run synchronously from the main robot loop; no multithreading is used, unless
 * specified explicitly from the command implementation.
 *
 * @author Jackson
 */
@SuppressWarnings("PMD.TooManyMethods")
public interface CommandInterface {

    /**
     * The initial subroutine of a command.  Called once when the command is initially scheduled.
     */
    void initialize();

    /**
     * The main body of a command.  Called repeatedly while the command is scheduled.
     */
    void execute();

    /**
     * The action to take when the command ends.  Called when either the command finishes normally,
     * or when it interrupted/canceled.
     *
     * @param interrupted whether the command was interrupted/canceled
     */
    void end(boolean interrupted);

    /**
     * Whether the command has finished.  Once a command finishes, the scheduler will call its
     * end() method and un-schedule it.
     *
     * @return whether the command has finished.
     */
    boolean isFinished();

    /**
     * Specifies the set of subsystems used by this command.  Two commands cannot use the same
     * subsystem at the same time.  If the command is scheduled as interruptible and another
     * command is scheduled that shares a requirement, the command will be interrupted.  Else,
     * the command will not be scheduled.  If no subsystems are required, return an empty set.
     *
     * <p>Note: it is recommended that user implementations contain the requirements as a field,
     * and return that field here, rather than allocating a new set every time this is called.
     *
     * @return the set of subsystems that are required
     */
    Set<Subsystem> getRequirements();

    /**
     * Decorates this command with a timeout.  If the specified timeout is exceeded before the command
     * finishes normally, the command will be interrupted and un-scheduled.  Note that the
     * timeout only applies to the command returned by this method; the calling command is
     * not itself changed.
     *
     * <p>Note: This decorator works by composing this command within a CommandGroup.  The command
     * cannot be used independently after being decorated, or be re-decorated with a different
     * decorator, unless it is manually cleared from the list of grouped commands with
     * {@link CommandGroupBase#clearGroupedCommand(Command)}.  The decorated command can, however, be
     * further decorated without issue.
     *
     * @param millis the timeout duration
     * @return the command with the timeout added
     */
    Command withTimeout(long millis);

    /**
     * Decorates this command with an interrupt condition.  If the specified condition becomes true
     * before the command finishes normally, the command will be interrupted and un-scheduled.
     * Note that this only applies to the command returned by this method; the calling command
     * is not itself changed.
     *
     * <p>Note: This decorator works by composing this command within a CommandGroup.  The command
     * cannot be used independently after being decorated, or be re-decorated with a different
     * decorator, unless it is manually cleared from the list of grouped commands with
     * {@link CommandGroupBase#clearGroupedCommand(Command)}.  The decorated command can, however, be
     * further decorated without issue.
     *
     * @param condition the interrupt condition
     * @return the command with the interrupt condition added
     */
    Command interruptOn(BooleanSupplier condition);

    /**
     * Decorates this command with a runnable to run after the command finishes.
     *
     * <p>Note: This decorator works by composing this command within a CommandGroup.  The command
     * cannot be used independently after being decorated, or be re-decorated with a different
     * decorator, unless it is manually cleared from the list of grouped commands with
     * {@link CommandGroupBase#clearGroupedCommand(Command)}.  The decorated command can, however, be
     * further decorated without issue.
     *
     * @param toRun the Runnable to run
     * @return the decorated command
     */
    Command whenFinished(Runnable toRun);

    /**
     * Decorates this command with a runnable to run before this command starts.
     *
     * <p>Note: This decorator works by composing this command within a CommandGroup.  The command
     * cannot be used independently after being decorated, or be re-decorated with a different
     * decorator, unless it is manually cleared from the list of grouped commands with
     * {@link CommandGroupBase#clearGroupedCommand(Command)}.  The decorated command can, however, be
     * further decorated without issue.
     *
     * @param toRun the Runnable to run
     * @return the decorated command
     */
    Command beforeStarting(Runnable toRun);

    /**
     * Decorates this command with a set of commands to run after it in sequence.  Often more
     * convenient/less-verbose than constructing a new {@link SequentialCommandGroup} explicitly.
     *
     * <p>Note: This decorator works by composing this command within a CommandGroup.  The command
     * cannot be used independently after being decorated, or be re-decorated with a different
     * decorator, unless it is manually cleared from the list of grouped commands with
     * {@link CommandGroupBase#clearGroupedCommand(Command)}.  The decorated command can, however, be
     * further decorated without issue.
     *
     * @param next the commands to run next
     * @return the decorated command
     */
    Command andThen(Command... next);

    /**
     * Decorates this command with a set of commands to run parallel to it, ending when the calling
     * command ends and interrupting all the others.  Often more convenient/less-verbose than
     * constructing a new {@link ParallelDeadlineGroup} explicitly.
     *
     * <p>Note: This decorator works by composing this command within a CommandGroup.  The command
     * cannot be used independently after being decorated, or be re-decorated with a different
     * decorator, unless it is manually cleared from the list of grouped commands with
     * {@link CommandGroupBase#clearGroupedCommand(Command)}.  The decorated command can, however, be
     * further decorated without issue.
     *
     * @param parallel the commands to run in parallel
     * @return the decorated command
     */
    Command deadlineWith(Command... parallel);

    /**
     * Decorates this command with a set of commands to run parallel to it, ending when the last
     * command ends.  Often more convenient/less-verbose than constructing a new
     * {@link ParallelCommandGroup} explicitly.
     *
     * <p>Note: This decorator works by composing this command within a CommandGroup.  The command
     * cannot be used independently after being decorated, or be re-decorated with a different
     * decorator, unless it is manually cleared from the list of grouped commands with
     * {@link CommandGroupBase#clearGroupedCommand(Command)}.  The decorated command can, however, be
     * further decorated without issue.
     *
     * @param parallel the commands to run in parallel
     * @return the decorated command
     */
    Command alongWith(Command... parallel);

    /**
     * Decorates this command with a set of commands to run parallel to it, ending when the first
     * command ends.  Often more convenient/less-verbose than constructing a new
     * {@link ParallelRaceGroup} explicitly.
     *
     * <p>Note: This decorator works by composing this command within a CommandGroup.  The command
     * cannot be used independently after being decorated, or be re-decorated with a different
     * decorator, unless it is manually cleared from the list of grouped commands with
     * {@link CommandGroupBase#clearGroupedCommand(Command)}.  The decorated command can, however, be
     * further decorated without issue.
     *
     * @param parallel the commands to run in parallel
     * @return the decorated command
     */
    Command raceWith(Command... parallel);

    /**
     * Decorates this command to run perpetually, ignoring its ordinary end conditions.  The decorated
     * command can still be interrupted or canceled.
     *
     * <p>Note: This decorator works by composing this command within a CommandGroup.  The command
     * cannot be used independently after being decorated, or be re-decorated with a different
     * decorator, unless it is manually cleared from the list of grouped commands with
     * {@link CommandGroupBase#clearGroupedCommand(Command)}.  The decorated command can, however, be
     * further decorated without issue.
     *
     * @return the decorated command
     */
    Command perpetually();

    /**
     * Decorates this command to run "by proxy" by wrapping it in a {@link ProxyScheduleCommand}.
     * This is useful for "forking off" from command groups when the user does not wish to extend
     * the command's requirements to the entire command group.
     *
     * @return the decorated command
     */
    Command asProxy();

    /**
     * Whether the command requires a given subsystem.
     *
     * @param requirement the subsystem to inquire about
     * @return whether the subsystem is required
     */
    boolean hasRequirement(Subsystem requirement);

    /**
     * Schedules this command.
     *
     * @param interruptible whether this command can be interrupted by another command that
     *                      shares one of its requirements
     */
    void schedule(boolean interruptible);

    /**
     * Schedules this command, defaulting to interruptible.
     */
    void schedule();

    /**
     * Cancels this command.  Will call the command's interrupted() method.
     * Commands will be canceled even if they are not marked as interruptible.
     */
    void cancel();

    /**
     * Whether or not the command is currently scheduled.  Note that this does not detect whether
     * the command is being run by a CommandGroup, only whether it is directly being run by
     * the scheduler.
     *
     * @return Whether the command is scheduled.
     */
    boolean isScheduled();

    /**
     * Whether the given command should run when the robot is disabled.  Override to return true
     * if the command should run when disabled.
     *
     * @return whether the command should run when the robot is disabled
     */
    boolean runsWhenDisabled();

    String getName();

}
