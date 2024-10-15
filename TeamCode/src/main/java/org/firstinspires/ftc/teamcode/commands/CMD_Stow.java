package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.ConditionalCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;
import org.firstinspires.ftc.teamcode.subsystems.SUB_IntakeSubSlide;
import org.firstinspires.ftc.teamcode.subsystems.SUB_SubmersibleIntake;

public class CMD_Stow extends SequentialCommandGroup {
    public CMD_Stow(GlobalVariables p_variables, SUB_IntakeSubSlide p_subSlide,
                    SUB_SubmersibleIntake p_subIntake, SUB_Bucket p_bucket, SUB_BucketLift p_bucketLift){
        addCommands(
            new ConditionalCommand(
                new CMD_StowSub(p_variables, p_subSlide, p_subIntake, p_bucket)
                ,new CMD_StowWall(p_variables, p_bucketLift)
                ,()-> p_variables.isIntakeState(GlobalVariables.IntakeState.SUBMERSIBLE)
            )
        );
    }
}
