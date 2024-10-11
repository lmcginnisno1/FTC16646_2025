package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;

public class CMD_HomeBucket extends SequentialCommandGroup {
    public CMD_HomeBucket(GlobalVariables p_variables, SUB_Bucket p_bucket, SUB_BucketLift p_bucketLift){
        addCommands(
            new InstantCommand(()-> p_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
            ,new CMD_BucketLiftReset(p_bucketLift)
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.HOME)));
    }
}
