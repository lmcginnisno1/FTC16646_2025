package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;

public class CMD_ReadyToDeployBucket extends SequentialCommandGroup {
    public CMD_ReadyToDeployBucket(GlobalVariables p_variables, SUB_BucketLift p_bucketLift){
        addCommands(
            new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_DEPLOY))
            ,new InstantCommand(()-> p_bucketLift.setTargetPosition(Constants.BucketLift.kLiftReadyToDeployHighBasket))
            ,new CMD_BucketLiftInPosition(p_bucketLift)
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.READY_TO_DEPLOY))
        );
    }
}
