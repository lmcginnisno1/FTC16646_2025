package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.GlobalVariables;
import org.firstinspires.ftc.teamcode.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.ftclib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.SUB_Climb;

public class CMD_Climb extends SequentialCommandGroup {
    public CMD_Climb(GlobalVariables p_variables, SUB_Climb p_climb){
        addRequirements(p_climb);
        addCommands(
            new InstantCommand(()-> p_climb.climb())
            ,new InstantCommand(()-> p_variables.setRobotState(GlobalVariables.RobotState.CLIMB))
        );
    }
}
