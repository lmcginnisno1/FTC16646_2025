package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.ConditionalCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;

public class CMD_ReadyToDeploy extends SequentialCommandGroup {
    public CMD_ReadyToDeploy(GlobalVariables p_variables, SUB_BucketLift p_bucketLift){
        addCommands(
            new ConditionalCommand(
                new CMD_ReadyToDeployBucket(p_variables, p_bucketLift)
                ,new CMD_ReadyToDeployChamber(p_variables, p_bucketLift)
                ,()-> p_variables.isIntakeState(GlobalVariables.IntakeState.SUBMERSIBLE)
            )
        );
    }
}
