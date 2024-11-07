package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Bucket;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;
import org.firstinspires.ftc.teamcode.subsystems.SUB_IntakeSubSlide;
import org.firstinspires.ftc.teamcode.subsystems.SUB_SubmersibleIntake;

public class CMD_FastDeployBucket extends SequentialCommandGroup {
    public CMD_FastDeployBucket(GlobalVariables p_variables, SUB_BucketLift p_bucketLift, SUB_IntakeSubSlide p_intakeSubSlide
            , SUB_SubmersibleIntake p_subIntake, SUB_Bucket p_bucket){
        addCommands(
                new InstantCommand(()-> p_bucket.setBucketServoPosition(Constants.BucketConstants.kBucketHome))
                ,new InstantCommand(()-> p_subIntake.setIntakeSpeed(Constants.SubIntakeConstants.kIntakeOff))
                ,new InstantCommand(()-> p_subIntake.setBucketPosition(Constants.SubIntakeConstants.kBucketHome))
                ,new ParallelCommandGroup(
                    new CMD_SubSlideReset(p_intakeSubSlide)
                    ,new SequentialCommandGroup(
                        new WaitCommand(150)
                        ,new CMD_ReadyToDeploy(p_variables, p_bucketLift)
                    )
                )
        );
    }
}
