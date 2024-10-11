package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_IntakeSubSlide;
import org.firstinspires.ftc.teamcode.subsystems.SUB_SubmersibleIntake;

public class CMD_StowSub extends SequentialCommandGroup {
    public CMD_StowSub(GlobalVariables p_variables, SUB_IntakeSubSlide p_intakeSubSlide, SUB_SubmersibleIntake p_subIntake, SUB_Bucket p_bucket){
        addCommands(
            new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_HOME))
            ,new InstantCommand(()-> p_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOff))
            ,new InstantCommand(()-> p_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
            ,new InstantCommand(()-> p_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketHome))
            ,new InstantCommand(()-> p_intakeSubSlide.setTargetPosition(Constants.SubmersibleSlide.kSlideHome))
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.STOW)));
    }
}
