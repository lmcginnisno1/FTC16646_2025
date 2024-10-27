package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_IntakeSubSlide;
import org.firstinspires.ftc.teamcode.subsystems.SUB_SubmersibleIntake;

public class CMD_ReadyToIntakeSub extends SequentialCommandGroup {
    public CMD_ReadyToIntakeSub(GlobalVariables p_variables, SUB_IntakeSubSlide p_subSlide,
        SUB_SubmersibleIntake p_subIntake){
        addCommands(
                new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_INTAKE))
                ,new InstantCommand(()-> p_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketReadyToIntake))
                ,new InstantCommand(()-> p_subSlide.setTargetPosition(Constants.SubmersibleSlide.kSlideIntake))
                ,new CMD_SubmersibleInPosition(p_subSlide)
                ,new InstantCommand(()-> p_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOn))
                ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.READY_TO_INTAKE))
        );
    }
}
