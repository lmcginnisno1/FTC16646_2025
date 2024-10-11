package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;

public class CMD_DeployChamber extends SequentialCommandGroup {
    public CMD_DeployChamber(GlobalVariables p_variables, SUB_BucketLift p_bucketLift){
        addCommands(
            new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_DEPLOY))
            ,new InstantCommand(()-> p_bucketLift.setTargetPosition(Constants.BucketLift.kLiftDeployHighChamber))
            ,new CMD_BucketLiftInPosition(p_bucketLift)
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_HOME))
            ,new CMD_BucketLiftReset(p_bucketLift)
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.HOME))
        );
    }
}
