package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;
import org.firstinspires.ftc.teamcode.subsystems.SUB_IntakeSubSlide;
import org.firstinspires.ftc.teamcode.subsystems.SUB_SubmersibleIntake;

public class CMD_ResetToHome extends SequentialCommandGroup {
    public CMD_ResetToHome(GlobalVariables p_variables, SUB_BucketLift p_bucketLift, SUB_IntakeSubSlide p_subSlide
        , SUB_Bucket p_bucket, SUB_SubmersibleIntake p_subIntake){
        addCommands(
            new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.TRANSITIONING_TO_HOME))
            ,new InstantCommand(()-> p_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
            ,new InstantCommand(()-> p_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketHome))
            ,new InstantCommand(()-> p_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOff))
            ,new ParallelCommandGroup(
                new CMD_BucketLiftReset(p_bucketLift)
                ,new CMD_SubSlideReset(p_subSlide)
            )
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.HOME))
        );
    }
}
