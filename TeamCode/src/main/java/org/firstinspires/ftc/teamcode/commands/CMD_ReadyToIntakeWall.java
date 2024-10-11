package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;

public class CMD_ReadyToIntakeWall extends SequentialCommandGroup {
    public CMD_ReadyToIntakeWall(GlobalVariables p_variables, SUB_BucketLift p_bucketLift, SUB_Bucket p_bucket){
        addCommands(
            new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_INTAKE))
            ,new InstantCommand(()-> p_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
            ,new InstantCommand(()-> p_bucketLift.setTargetPosition(Constants.BucketLift.kLiftReadyToIntakeWall))
            ,new CMD_BucketLiftInPosition(p_bucketLift)
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.READY_TO_INTAKE))
        );
    }
}
