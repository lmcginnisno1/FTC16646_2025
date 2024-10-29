package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.ConditionalCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;

public class CMD_Deploy extends SequentialCommandGroup {
    public CMD_Deploy(GlobalVariables p_variables, SUB_BucketLift p_bucketLift, SUB_Bucket p_bucket){
        addCommands(
            new ConditionalCommand(
            new CMD_DeployBucket(p_variables, p_bucket)
            ,new CMD_DeployChamber(p_variables, p_bucketLift)
            ,()-> p_variables.isIntakeState(GlobalVariables.IntakeState.SUBMERSIBLE)
        ));
    }
}
