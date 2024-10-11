package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.ConditionalCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;
import org.firstinspires.ftc.teamcode.subsystems.SUB_IntakeSubSlide;
import org.firstinspires.ftc.teamcode.subsystems.SUB_SubmersibleIntake;

public class CMD_ReadyToIntake extends SequentialCommandGroup {
    public CMD_ReadyToIntake(GlobalVariables p_variables, SUB_IntakeSubSlide p_subSlide,
               SUB_SubmersibleIntake p_subIntake, SUB_BucketLift p_bucketLift, SUB_Bucket p_bucket){
        addCommands(
            new ConditionalCommand(
                new CMD_ReadyToIntakeSub(p_variables, p_subSlide, p_subIntake)
                ,new CMD_ReadyToIntakeWall(p_variables, p_bucketLift, p_bucket)
                ,()-> p_variables.isIntakeState(GlobalVariables.IntakeState.SUBMERSIBLE)
            )
        );
    }
}
