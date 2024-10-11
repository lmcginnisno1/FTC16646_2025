package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.SUB_BucketLift;
import org.firstinspires.ftc.teamcode.Constants.BucketLift;

public class CMD_BucketLiftInPosition extends CommandBase {
    SUB_BucketLift m_bucketLift;
    public CMD_BucketLiftInPosition(SUB_BucketLift p_bucketLift){
        m_bucketLift = p_bucketLift;
    }

    @Override
    public boolean isFinished(){
        return m_bucketLift.atPosition();
    }
}
