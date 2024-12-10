package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_IntakeSubSlide;
import org.firstinspires.ftc.teamcode.subsystems.SUB_SubmersibleIntake;

public class CMD_IntakeSub extends SequentialCommandGroup {
    public CMD_IntakeSub(GlobalVariables p_variables, SUB_IntakeSubSlide p_intakeSubSlide,
            SUB_SubmersibleIntake p_subIntake, SUB_Bucket p_bucket){
        addCommands(
            new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_STOW))
            ,new InstantCommand(()-> p_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeTransfer))
            ,new InstantCommand(()-> p_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketReadyToIntake))
            ,new InstantCommand(()-> p_intakeSubSlide.setTargetPosition(Constants.SubmersibleSlide.kSlideTransfer))
            ,new InstantCommand(()-> p_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketReceive))
            ,new CMD_SubmersibleInPosition(p_intakeSubSlide)
            ,new InstantCommand(()-> p_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketTransfer))
            ,new WaitCommand(330)//1250
            ,new InstantCommand(()-> p_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeReverse))
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.INTAKE))
        );
    }
}
