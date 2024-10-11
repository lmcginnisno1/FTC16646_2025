package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public class GlobalVariables {
     public static Pose2d m_autonomousEndPose = new Pose2d();
     public enum RobotState {
          HOME
          ,TRANSITIONING_TO_INTAKE
          ,READY_TO_INTAKE
          ,INTAKE
          ,TRANSITION_TO_STOW
          ,STOW
          ,TRANSITIONING_TO_DEPLOY
          ,READY_TO_DEPLOY
          ,DEPLOY
          ,TRANSITIONING_TO_HOME
          ,CLIMB
     }

     RobotState m_robotState = RobotState.HOME;

     public RobotState getRobotState(){
          return m_robotState;
     }

     public void setRobotState(RobotState p_robotState){
          m_robotState = p_robotState;
     }

     public boolean isRobotState(RobotState p_robotState){
          return m_robotState == p_robotState;
     }

     public enum IntakeState{
          WALL
          ,SUBMERSIBLE
     }

     IntakeState m_intakeState = IntakeState.WALL;

     public IntakeState getIntakeState(){
          return m_intakeState;
     }

     public void setIntakeState(IntakeState p_IntakeState){
          m_intakeState = p_IntakeState;
     }

     public boolean isIntakeState(IntakeState p_IntakeState){
          return m_intakeState == p_IntakeState;
     }
}
