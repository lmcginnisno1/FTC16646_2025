package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.ConditionalCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;

public class CMD_QuickDump extends SequentialCommandGroup {
    public CMD_QuickDump(GlobalVariables p_variables, SUB_Bucket p_bucket){
        addCommands(
            new SequentialCommandGroup(
                new InstantCommand(()-> p_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketDeploy))
                ,new WaitCommand(330)
                ,new InstantCommand(()-> p_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
            )
            ,new InstantCommand()
        );
    }
}
