package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;

public class CMD_DeployBucket extends SequentialCommandGroup {
    public CMD_DeployBucket(GlobalVariables p_variables, SUB_BucketLift p_bucketLift, SUB_Bucket p_bucket){
        addCommands(
            new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_DEPLOY))
            ,new InstantCommand(()-> p_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketDeploy))
            ,new WaitCommand(500)
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_HOME))
        );
    }
}
