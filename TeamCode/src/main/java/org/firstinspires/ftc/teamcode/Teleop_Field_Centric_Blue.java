package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Teleop Blue", group = "Teleop Blue")
public class Teleop_Field_Centric_Blue extends Teleop_Field_Centric{
    @Override
    public void setSide(){
        m_startHeadingOffset = Math.toRadians(-90);
        m_robot.drivetrain.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(-225)));
        m_robot.setBlueSide();
    }
}